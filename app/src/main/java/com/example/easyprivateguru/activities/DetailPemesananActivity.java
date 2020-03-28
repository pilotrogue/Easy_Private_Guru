package com.example.easyprivateguru.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.easyprivateguru.R;
import com.example.easyprivateguru.UserHelper;
import com.example.easyprivateguru.api.ApiInterface;
import com.example.easyprivateguru.api.RetrofitClientInstance;
import com.example.easyprivateguru.models.MataPelajaran;
import com.example.easyprivateguru.models.Pemesanan;
import com.example.easyprivateguru.models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailPemesananActivity extends AppCompatActivity {
    private RetrofitClientInstance rci = new RetrofitClientInstance();
    private ApiInterface apiInterface = rci.getApiInterface();
    private UserHelper userHelper;

    private TextView tvNamaMurid, tvAlamatMurid, tvMapel, tvJenjang, tvStatus;
    private Button btnTerima, btnTolak;

    private Intent currIntent;
    private static final String TAG = "DetailPemesananActivity";

    private Pemesanan currPemesanan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pemesanan);

        init();

        int idPemesanan = currIntent.getIntExtra("idPemesanan", 0);
        callPemesananById(idPemesanan);
    }

    private void init(){
        Log.d(TAG, "init: called");
        userHelper = new UserHelper(this);

        currIntent = getIntent();

        tvNamaMurid = findViewById(R.id.tvNamaMurid);
        tvAlamatMurid = findViewById(R.id.tvAlamatMurid);
        tvMapel = findViewById(R.id.tvMapel);
        tvJenjang = findViewById(R.id.tvJenjang);
        tvStatus = findViewById(R.id.tvStatus);

        btnTerima = findViewById(R.id.btnTerimaPemesanan);
        btnTolak = findViewById(R.id.btnTolakPemesanan);
    }

    private void callPemesananById(int id){
        Log.d(TAG, "callPemesananById: called");
        Call<Pemesanan> call = apiInterface.getPemesananById(id);
        ProgressDialog p = rci.getProgressDialog(this, "Menampilkan pemesanan");
        p.show();
        call.enqueue(new Callback<Pemesanan>() {
            @Override
            public void onResponse(Call<Pemesanan> call, Response<Pemesanan> response) {
                p.dismiss();
                Log.d(TAG, "onResponse: "+response.message());
                if(!response.isSuccessful()){
                    return;
                }

                currPemesanan = response.body();
                retrievePemesanan(currPemesanan);
            }

            @Override
            public void onFailure(Call<Pemesanan> call, Throwable t) {
                p.dismiss();
                Log.d(TAG, "onFailure: "+t.getMessage());
                return;
            }
        });
    }

    private void retrievePemesanan(Pemesanan pemesanan){
        Log.d(TAG, "retrievePemesanan: called");
        User murid = pemesanan.getMurid();
        tvNamaMurid.setText(murid.getName());
        tvAlamatMurid.setText(murid.getAlamat().get(0).getAlamatLengkap());

        MataPelajaran mapel = pemesanan.getMataPelajaran();
        tvMapel.setText(mapel.getNamaMapel());
        tvJenjang.setText(mapel.getJenjang().getNamaJenjang());

        tvStatus.setText("Status: "+pemesanan.getStatus());

        //Menerima pemesanan
        btnTerima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callUpdatePemesanan(1);
            }
        });

        //Menolak pemesanan
        btnTolak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callUpdatePemesanan(2);
            }
        });
    }

    private void callUpdatePemesanan(int status){
        Log.d(TAG, "callUpdatePemesanan: called");
        ProgressDialog p = rci.getProgressDialog(this);
        Call<Pemesanan> call = apiInterface.updatePemesanan(currPemesanan.getIdPemesanan(), status);
        p.show();
        call.enqueue(new Callback<Pemesanan>() {
            @Override
            public void onResponse(Call<Pemesanan> call, Response<Pemesanan> response) {
                p.dismiss();
                Log.d(TAG, "onResponse: "+response.message());
                if(!response.isSuccessful()){
                    return;
                }

                finish();
            }

            @Override
            public void onFailure(Call<Pemesanan> call, Throwable t) {
                p.dismiss();
                Log.d(TAG, "onFailure: "+t.getMessage());
                return;
            }
        });
    }
}
