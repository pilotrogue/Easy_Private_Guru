package com.example.easyprivateguru.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.widget.Toast;

import com.example.easyprivateguru.R;
import com.example.easyprivateguru.UserHelper;
import com.example.easyprivateguru.adapters.JadwalRVAdapter;
import com.example.easyprivateguru.api.ApiInterface;
import com.example.easyprivateguru.api.RetrofitClientInstance;
import com.example.easyprivateguru.models.JadwalAjar;
import com.example.easyprivateguru.models.JadwalPemesananPerminggu;
import com.example.easyprivateguru.models.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JadwalActivity extends AppCompatActivity {
    private RetrofitClientInstance rci = new RetrofitClientInstance();
    private ApiInterface apiInterface = rci.getApiInterface();
    private UserHelper userHelper;
    private User currUser;

    private RecyclerView rvJadwal;
    private Cursor mCursor;
    private static final String TAG = "JadwalActivity";
    private ArrayList<JadwalAjar> jadwalAjars = new ArrayList<>();
    private ArrayList<JadwalPemesananPerminggu> jadwalPemesananPermingguArrayList = new ArrayList<>();

    private boolean hasBeenRefreshed = true;

    @Override
    public void onResume() {
        super.onResume();
        if(hasBeenRefreshed == false){
            userHelper = new UserHelper(this);
            currUser = userHelper.retrieveUser();
            jadwalAjars.clear();
            rvJadwal.setAdapter(null);

            callJadwalPemesananPerminggu(currUser.getId());
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
        callJadwalPemesananPerminggu(currUser.getId());
    }

    private void init() {
        rvJadwal = findViewById(R.id.rvJadwal);
        mCursor = getCursor();
        userHelper = new UserHelper(this);
        currUser = userHelper.retrieveUser();
    }

    private void callJadwalPemesananPerminggu(Integer id_guru){
        ProgressDialog progressDialog = rci.getProgressDialog(this, "Menampilkan jadwal kamu");
        progressDialog.show();
        Call<ArrayList<JadwalPemesananPerminggu>> call = apiInterface.getJadwalPemesananPerminggu(null, id_guru, 1);
        call.enqueue(new Callback<ArrayList<JadwalPemesananPerminggu>>() {
            @Override
            public void onResponse(Call<ArrayList<JadwalPemesananPerminggu>> call, Response<ArrayList<JadwalPemesananPerminggu>> response) {
                Log.d(TAG, "onResponse: "+response.message());
                progressDialog.dismiss();
                if(!response.isSuccessful()){
                    return;
                }
                jadwalPemesananPermingguArrayList = response.body();
                retrieveJadwalPemesananPerminggu();
            }

            @Override
            public void onFailure(Call<ArrayList<JadwalPemesananPerminggu>> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
                t.printStackTrace();
                progressDialog.dismiss();
            }
        });
    }

    private void retrieveJadwalPemesananPerminggu(){
        JadwalRVAdapter jadwalRVAdapter = new JadwalRVAdapter(this, jadwalPemesananPermingguArrayList);
        rvJadwal.setAdapter(jadwalRVAdapter);
        rvJadwal.setLayoutManager(new LinearLayoutManager(this));
    }

    private ArrayList<JadwalAjar> getJadwals(){
        Log.d(TAG, "getJadwals: called");
        int idIndex = mCursor.getColumnIndex(CalendarContract.Events._ID),
                titleIndex = mCursor.getColumnIndex(CalendarContract.Events.TITLE),
                descriptionIndex = mCursor.getColumnIndex(CalendarContract.Events.DESCRIPTION),
                eventLocationIndex = mCursor.getColumnIndex(CalendarContract.Events.EVENT_LOCATION);

        String idStr, titleStr, descriptionStr, eventLocationStr;
        ArrayList<JadwalAjar> jadwalAjars = new ArrayList<>();
        mCursor.moveToFirst();
        while (mCursor.moveToNext()){
            if (mCursor != null){
                idStr = mCursor.getString(idIndex);
                titleStr = mCursor.getString(titleIndex);
                descriptionStr = mCursor.getString(descriptionIndex);
                eventLocationStr = mCursor.getString(eventLocationIndex);
                Log.d(TAG, "getJadwals: idStr: "+idStr);

                JadwalAjar jadwalAjar = new JadwalAjar(Integer.parseInt(idStr));
                jadwalAjars.add(jadwalAjar);
            }else {
                Log.d(TAG, "getJadwals: No events");
                JadwalAjar jadwalAjar = new JadwalAjar(0);
                jadwalAjars.add(jadwalAjar);
            }
        }

        return jadwalAjars;
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

    //    private void callJadwalAjar(){
//        ProgressDialog progressDialog = rci.getProgressDialog(this, "Menampilkan jadwal kamu");
//        Call<ArrayList<JadwalAjar>> call = apiInterface.getJadwalAjarByIdGuru(currUser.getId());
//        progressDialog.show();
//        call.enqueue(new Callback<ArrayList<JadwalAjar>>() {
//            @Override
//            public void onResponse(Call<ArrayList<JadwalAjar>> call, Response<ArrayList<JadwalAjar>> response) {
//                progressDialog.dismiss();
//                Log.d(TAG, "onResponse: "+response.message());
//                if(!response.isSuccessful()){
//                    return;
//                }
//
//                jadwalAjars = response.body();
//                retrieveJadwalAjar();
//            }
//
//            @Override
//            public void onFailure(Call<ArrayList<JadwalAjar>> call, Throwable t) {
//                progressDialog.dismiss();
//                Log.d(TAG, "onFailure: "+t.getMessage());
//                return;
//            }
//        });
//    }
//
//    private void retrieveJadwalAjar(){
//        JadwalRVAdapter adapter = new JadwalRVAdapter(this, jadwalAjars);
//        rvJadwal.setAdapter(adapter);
//        rvJadwal.setLayoutManager(new LinearLayoutManager(this));
//    }
}
