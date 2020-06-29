package com.example.easyprivateguru.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.easyprivateguru.CustomUtility;
import com.example.easyprivateguru.EventQueryHandler;
import com.example.easyprivateguru.R;
import com.example.easyprivateguru.UserHelper;
import com.example.easyprivateguru.adapters.JadwalRVAdapter;
import com.example.easyprivateguru.api.ApiInterface;
import com.example.easyprivateguru.api.RetrofitClientInstance;
import com.example.easyprivateguru.models.Alamat;
import com.example.easyprivateguru.models.JadwalAvailable;
import com.example.easyprivateguru.models.JadwalPemesananPerminggu;
import com.example.easyprivateguru.models.Pemesanan;
import com.example.easyprivateguru.models.User;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JadwalActivity extends AppCompatActivity {
    private RetrofitClientInstance rci = new RetrofitClientInstance();
    private ApiInterface apiInterface = rci.getApiInterface();
    private UserHelper userHelper;
    private User currUser;

    private RecyclerView rvJadwal;
    private LinearLayout llBtnSync;
    private Cursor mCursor;
    private static final String TAG = "JadwalActivity";
    private ArrayList<JadwalAvailable> jadwalAvailableArrayList = new ArrayList<>();

    private boolean hasBeenRefreshed = true;

    @Override
    public void onResume() {
        super.onResume();
        if(hasBeenRefreshed == false){
            userHelper = new UserHelper(this);
            currUser = userHelper.retrieveUser();
            jadwalAvailableArrayList.clear();
            rvJadwal.setAdapter(null);

            callJadwalAvailable(currUser.getId());
            hasBeenRefreshed = true;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        hasBeenRefreshed = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jadwal);

        init();
        callJadwalAvailable(currUser.getId());
    }

    private void init() {
        rvJadwal = findViewById(R.id.rvJadwal);
        llBtnSync = findViewById(R.id.llBtnSync);

        mCursor = getCursor();
        userHelper = new UserHelper(this);
        currUser = userHelper.retrieveUser();
    }

    private void callJadwalAvailable(Integer id_guru){
        ProgressDialog progressDialog = rci.getProgressDialog(this, "Menampilkan jadwal kamu");
        progressDialog.show();
        Call<ArrayList<JadwalAvailable>> call = apiInterface.getJadwalAvailable(id_guru, 2, null, null, null);
        call.enqueue(new Callback<ArrayList<JadwalAvailable>>() {
            @Override
            public void onResponse(Call<ArrayList<JadwalAvailable>> call, Response<ArrayList<JadwalAvailable>> response) {
                Log.d(TAG, "onResponse: "+response.message());
                progressDialog.dismiss();
                if(!response.isSuccessful()){
                    return;
                }

                jadwalAvailableArrayList = response.body();
                retrieveJadwalAvailable();
            }

            @Override
            public void onFailure(Call<ArrayList<JadwalAvailable>> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
                t.printStackTrace();
                progressDialog.dismiss();
            }
        });
    }

    private void retrieveJadwalAvailable(){
        if(isSynced()){
            llBtnSync.setVisibility(View.GONE);
        }else{
            llBtnSync.setVisibility(View.VISIBLE);
            llBtnSync.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addJadwalToGoogleCalendar();
                }
            });
        }

        JadwalRVAdapter jadwalRVAdapter = new JadwalRVAdapter(this, jadwalAvailableArrayList);
        rvJadwal.setAdapter(jadwalRVAdapter);
        rvJadwal.setLayoutManager(new LinearLayoutManager(this));
    }

    //Memeriksa apakah jadwal sudah disinkronisasi atau belum
    private boolean isSynced(){
        for(int i = 0; i < jadwalAvailableArrayList.size(); i++){
            JadwalPemesananPerminggu jpp = jadwalAvailableArrayList.get(i).getJadwalPemesananPerminggu();
            if(jpp.getIdEvent() == null){
                return false;
            }
        }
        return true;
    }

    private Cursor getCursor(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "getCursor: called");
            Cursor cursor = getContentResolver().query(CalendarContract.Events.CONTENT_URI, null, null, null, null);
            return cursor;
        }else{
            Log.d(TAG, "getCursor: no permission!");
            Toast.makeText(this, "No permission!", Toast.LENGTH_LONG).show();
            return null;
        }
    }

    private void addJadwalToGoogleCalendar(){
        //Memasukkan jadwal pemesanan perminggu ke dalam array
        ArrayList<JadwalPemesananPerminggu> jadwalPemesananPermingguArrayList = new ArrayList<>();
        for(int i = 0; i < jadwalAvailableArrayList.size(); i++){
            JadwalPemesananPerminggu jpp = jadwalAvailableArrayList.get(i).getJadwalPemesananPerminggu();
            if(jpp != null){
                jadwalPemesananPermingguArrayList.add(jpp);
            }
        }

        //looping jadwal pemesanan perminggu untuk dimasukkan ke dalam google calendar
        for(int i = 0; i < jadwalPemesananPermingguArrayList.size(); i++){
            JadwalPemesananPerminggu jadwalPemesananPerminggu = jadwalPemesananPermingguArrayList.get(i);
            if(jadwalPemesananPerminggu.getIdEvent() == null){
                Pemesanan pem = jadwalPemesananPermingguArrayList.get(i).getPemesanan();
                JadwalAvailable ja = jadwalAvailableArrayList.get(i);
                Log.d(TAG, "addJadwalToGoogleCalendar: pem.first_meet: "+pem.getFirstMeet());

                //Set title
                String titleStr = EventQueryHandler.DEFAULT_TITLE + " " + pem.getMurid().getName();

                //Set murid
                User murid = pem.getMurid();

                //Set event location
                String eventLocationStr = "";
                Alamat alamatMurid = murid.getAlamat();

                CustomUtility customUtility = new CustomUtility(this);
                Address addressMurid = customUtility.getAddress(alamatMurid.getLatitude(), alamatMurid.getLongitude());
                if(addressMurid == null){
                    eventLocationStr = alamatMurid.getAlamatLengkap();
                }else{
                    eventLocationStr = addressMurid.getSubLocality()+", "+addressMurid.getLocality()+", "+addressMurid.getSubAdminArea()+", "+addressMurid.getAdminArea()+", "+addressMurid.getCountryName();
                }

                EventQueryHandler eqh = new EventQueryHandler(this);
                JadwalPemesananPerminggu jpp = jadwalPemesananPermingguArrayList.get(i);
                String currHari = ja.getHari();

                String yearStart = customUtility.reformatDateTime(pem.getFirstMeet(), "yyyy-MM-dd HH:mm:ss", "yyyy");
                String monthStart = customUtility.reformatDateTime(pem.getFirstMeet(), "yyyy-MM-dd HH:mm:ss", "MM");
                String dayStart = customUtility.reformatDateTime(pem.getFirstMeet(), "yyyy-MM-dd HH:mm:ss", "dd");
                String hourStart = customUtility.reformatDateTime(ja.getStart(), "HH:mm:ss", "HH");
                String minuteStart = customUtility.reformatDateTime(ja.getStart(), "HH:mm:ss", "mm");
                String secondStart = customUtility.reformatDateTime(ja.getStart(), "HH:mm:ss", "ss");

                Log.d(TAG, "addJadwalToGoogleCalendar: monthStart: "+monthStart);

                //Get calendar first meet
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, Integer.parseInt(yearStart));
                calendar.set(Calendar.MONTH, Integer.parseInt(monthStart) - 1);
                calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dayStart));
                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hourStart));
                calendar.set(Calendar.MINUTE, Integer.parseInt(minuteStart));
                calendar.set(Calendar.SECOND, Integer.parseInt(secondStart));

                long startDateLong = calendar.getTimeInMillis();

                while(!currHari.toLowerCase().equals(customUtility.hariIntToString(calendar.get(Calendar.DAY_OF_WEEK)))){
                    startDateLong = calendar.getTimeInMillis();
                    startDateLong += (1000*60*60*24);
                    calendar.setTimeInMillis(startDateLong);
                    Log.d(TAG, "addJadwalToGoogleCalendar: "+calendar.getTime().toString());
                }

                //Menambah murid ke dalam daftar hadir
                //eqh.setMuridUser(murid);

                //Menambah id jadwal pemesanan perminggu
                eqh.setIdJadwalPemesananPerminggu(jpp.getIdJadwalPemesananPerminggu());

                Log.d(TAG, "addJadwalToGoogleCalendar: idJadwalPemesananPerminggu: "+jpp.getIdJadwalPemesananPerminggu());
                eqh.insertEvent(this, startDateLong, titleStr, EventQueryHandler.DEFAULT_DESCRIPTION, eventLocationStr);
            }
        }
        llBtnSync.setVisibility(View.GONE);
        llBtnSync.setOnClickListener(null);
        hasBeenRefreshed = false;
        onResume();
    }
}
