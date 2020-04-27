package com.example.easyprivateguru.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import com.example.easyprivateguru.DummyGenerator;
import com.example.easyprivateguru.UserHelper;
import com.example.easyprivateguru.adapters.MuridSayaRVAdapter;
import com.example.easyprivateguru.api.ApiInterface;
import com.example.easyprivateguru.api.RetrofitClientInstance;
import com.example.easyprivateguru.models.Jenjang;
import com.example.easyprivateguru.models.MataPelajaran;
import com.example.easyprivateguru.models.Pemesanan;
import com.example.easyprivateguru.models.User;
import com.example.easyprivateguru.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MuridSayaActivity extends AppCompatActivity {
    private RecyclerView rvMuridSaya;
    private ArrayList<Pemesanan> pemesanans = new ArrayList<>();
    private RetrofitClientInstance rci = new RetrofitClientInstance();
    private ApiInterface apiInterface = rci.getApiInterface();

    private UserHelper userHelper;
    private User currUser;

    private static final String TAG = "MuridSayaActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_murid_saya);

        init();
        callPemesanans();
    }

    private void init(){
        Log.d(TAG, "init: called");
        rvMuridSaya = findViewById(R.id.rvMuridSaya);
        userHelper = new UserHelper(this);
        currUser = userHelper.retrieveUser();
    }

    private void callPemesanans(){
        Call<ArrayList<Pemesanan>> call = apiInterface.getPemesananFiltered(null, currUser.getId(), null, null);
        ProgressDialog progressDialog = rci.getProgressDialog(this, "Menampilkan murid Anda");
        progressDialog.show();
        call.enqueue(new Callback<ArrayList<Pemesanan>>() {
            @Override
            public void onResponse(Call<ArrayList<Pemesanan>> call, Response<ArrayList<Pemesanan>> response) {
                Log.d(TAG, "onResponse: "+response.message());
                progressDialog.dismiss();
                if(!response.isSuccessful()){
                    return;
                }

                pemesanans = response.body();
                retrievePemesanans();
            }

            @Override
            public void onFailure(Call<ArrayList<Pemesanan>> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
                progressDialog.dismiss();
                return;
            }
        });
    }

    private void retrievePemesanans(){
        MuridSayaRVAdapter adapter = new MuridSayaRVAdapter(this, pemesanans);
        rvMuridSaya.setAdapter(adapter);
        rvMuridSaya.setLayoutManager(new LinearLayoutManager(this));
    }
}
