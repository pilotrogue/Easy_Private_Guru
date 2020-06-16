package com.example.easyprivateguru.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.location.Address;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.easyprivateguru.CustomUtility;
import com.example.easyprivateguru.EventQueryHandler;
import com.example.easyprivateguru.R;
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

public class KonfirmasiJadwalActivity extends AppCompatActivity {
    private RetrofitClientInstance rci = new RetrofitClientInstance();
    private ApiInterface apiInterface = rci.getApiInterface();
    private CustomUtility customUtility;

    private LinearLayout llJadwal;
    private TextView greetingTv, messageTv;
    private Button btnTambahJadwalGoogleCalendar, btnKembali;

    private final static String PESAN = "telah menjadi murid Anda. <br> Di bawah ini adalah jadwal murid Anda:";

    private ArrayList<JadwalPemesananPerminggu> jadwalPemesananPermingguArrayList = new ArrayList<>();
    private static final String TAG = "KonfirmasiJadwalActivit";

    private boolean googleCalendarHasBeenAdded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfirmasi_jadwal);
        init();

        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        callJadwalPemesananPerminggu();
    }

    private void init(){
        customUtility = new CustomUtility(this);
        llJadwal = findViewById(R.id.llJadwal);

        messageTv = findViewById(R.id.tvPesan);

        btnTambahJadwalGoogleCalendar = findViewById(R.id.btnTambahJadwalGoogleCalendar);
        btnKembali = findViewById(R.id.btnKembali);
    }

    private void callJadwalPemesananPerminggu(){
        Integer idPemesanan = getIntent().getIntExtra("idPemesanan", 0);
        Call<ArrayList<JadwalPemesananPerminggu>> call = apiInterface.getJadwalPemesananPerminggu(idPemesanan, null, null,null);

        ProgressDialog progressDialog = rci.getProgressDialog(this, "Menampilkan jadwal");
        progressDialog.show();
        call.enqueue(new Callback<ArrayList<JadwalPemesananPerminggu>>() {
            @Override
            public void onResponse(Call<ArrayList<JadwalPemesananPerminggu>> call, Response<ArrayList<JadwalPemesananPerminggu>> response) {
                progressDialog.dismiss();
                Log.d(TAG, "onResponse: "+response.message());
                if(!response.isSuccessful()){
                    return;
                }

                jadwalPemesananPermingguArrayList = response.body();
                Log.d(TAG, "onResponse: jadwalPemesananPermingguArrayList.size: "+jadwalPemesananPermingguArrayList.size());

                retrieveJadwalPemesananPerminggu();
            }

            @Override
            public void onFailure(Call<ArrayList<JadwalPemesananPerminggu>> call, Throwable t) {
                progressDialog.dismiss();
                Log.d(TAG, "onFailure: "+t.getMessage());
                t.printStackTrace();
            }
        });
    }

    private void retrieveJadwalPemesananPerminggu(){
        Pemesanan pem = jadwalPemesananPermingguArrayList.get(0).getPemesanan();
        String pesanStr = "<b>" +pem.getMurid().getName() + "</b> " + PESAN;

        messageTv.setText(Html.fromHtml(pesanStr));

        for(int i = 0; i < jadwalPemesananPermingguArrayList.size(); i++){
            JadwalAvailable ja = jadwalPemesananPermingguArrayList.get(i).getJadwalAvailable();

            View v = LayoutInflater.from(this).inflate(R.layout.item_card_schedule, llJadwal, false);
            TextView hariTv = v.findViewById(R.id.tvHari);
            TextView jamTv = v.findViewById(R.id.tvJam);

            hariTv.setText(ja.getHari());

            String jamStart = customUtility.reformatDateTime(ja.getStart(), "HH:mm:ss", "HH:mm");
            String jamEnd = customUtility.reformatDateTime(ja.getEnd(), "HH:mm:ss", "HH:mm");

            jamTv.setText(jamStart+" s/d "+jamEnd);
            llJadwal.addView(v);
        }

        btnTambahJadwalGoogleCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!googleCalendarHasBeenAdded){
                    addJadwalToGoogleCalendar();
                    googleCalendarHasBeenAdded = true;
                    btnTambahJadwalGoogleCalendar.setClickable(false);
                }
            }
        });
    }

    private void addJadwalToGoogleCalendar(){
        Pemesanan pem = jadwalPemesananPermingguArrayList.get(0).getPemesanan();
        Log.d(TAG, "addJadwalToGoogleCalendar: pem.first_meet: "+pem.getFirstMeet());

        //Set title
        String titleStr = EventQueryHandler.DEFAULT_TITLE + " " + pem.getMurid().getName();

        //Set murid
        User murid = pem.getMurid();

        //Set event location
        String eventLocationStr = "";
        Alamat alamatMurid = murid.getAlamat();
        Address addressMurid = customUtility.getAddress(alamatMurid.getLatitude(), alamatMurid.getLongitude());
        if(addressMurid == null){
            eventLocationStr = alamatMurid.getAlamatLengkap();
        }else{
            eventLocationStr = addressMurid.getSubLocality()+", "+addressMurid.getLocality()+", "+addressMurid.getSubAdminArea()+", "+addressMurid.getAdminArea()+", "+addressMurid.getCountryName();
        }

        for(int i = 0; i < jadwalPemesananPermingguArrayList.size(); i++){
            EventQueryHandler eqh = new EventQueryHandler(this);
            JadwalPemesananPerminggu jpp = jadwalPemesananPermingguArrayList.get(i);
            String currHari = jpp.getJadwalAvailable().getHari();

            String yearStart = customUtility.reformatDateTime(pem.getFirstMeet(), "yyyy-MM-dd HH:mm:ss", "yyyy");
            String monthStart = customUtility.reformatDateTime(pem.getFirstMeet(), "yyyy-MM-dd HH:mm:ss", "MM");
            String dayStart = customUtility.reformatDateTime(pem.getFirstMeet(), "yyyy-MM-dd HH:mm:ss", "dd");
            String hourStart = customUtility.reformatDateTime(jpp.getJadwalAvailable().getStart(), "HH:mm:ss", "HH");
            String minuteStart = customUtility.reformatDateTime(jpp.getJadwalAvailable().getStart(), "HH:mm:ss", "mm");
            String secondStart = customUtility.reformatDateTime(jpp.getJadwalAvailable().getStart(), "HH:mm:ss", "ss");

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
//            eqh.setMuridUser(murid);

            //Menambah id jadwal pemesanan perminggu
            eqh.setIdJadwalPemesananPerminggu(jpp.getIdJadwalPemesananPerminggu());

            Log.d(TAG, "addJadwalToGoogleCalendar: idJadwalPemesananPerminggu: "+jpp.getIdJadwalPemesananPerminggu());
            eqh.insertEvent(this, startDateLong, titleStr, EventQueryHandler.DEFAULT_DESCRIPTION, eventLocationStr);
        }
    }
}
