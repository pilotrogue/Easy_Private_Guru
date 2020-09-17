package com.easyprivate.easyprivateguru.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.easyprivate.easyprivateguru.R;
import com.easyprivate.easyprivateguru.UserHelper;
import com.easyprivate.easyprivateguru.adapters.DetailPembayaranRVAdapter;
import com.easyprivate.easyprivateguru.api.ApiInterface;
import com.easyprivate.easyprivateguru.api.RetrofitClientInstance;
import com.easyprivate.easyprivateguru.models.AbsenPembayaran;
import com.easyprivate.easyprivateguru.models.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailPembayaranActivity extends AppCompatActivity {
    private ArrayList<AbsenPembayaran> absenPembayaranArrayList = new ArrayList<>();
    private RecyclerView rvDetailPembayaran;

    private RetrofitClientInstance rci = new RetrofitClientInstance();
    private ApiInterface apiInterface = rci.getApiInterface();
    private User currUser;
    private Intent currIntent;

    private static final String TAG = "DetailPembayaranActivit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pembayaran);

        init();
        callDetailAbsenPembayaran();
    }

    private void init(){
        rvDetailPembayaran = findViewById(R.id.rvPembayaran);

        UserHelper uh = new UserHelper(this);
        currUser = uh.retrieveUser();

        currIntent = getIntent();
    }

    private void callDetailAbsenPembayaran(){
        Call<ArrayList<AbsenPembayaran>> call = apiInterface.getAbsenPembayaran(
                null,
                currUser.getId(),
                null,
                null,
                null,
                null,
                null,
                null
        );

        ProgressDialog pd = rci.getProgressDialog(this, "Memuat pembayaran");
        pd.show();

        call.enqueue(new Callback<ArrayList<AbsenPembayaran>>() {
            @Override
            public void onResponse(Call<ArrayList<AbsenPembayaran>> call, Response<ArrayList<AbsenPembayaran>> response) {
                Log.d(TAG, "onResponse: "+response.message());
                pd.dismiss();
                if(!response.isSuccessful()){
                    return;
                }

                absenPembayaranArrayList = response.body();
                retrieveAbsenPembayaran();
            }

            @Override
            public void onFailure(Call<ArrayList<AbsenPembayaran>> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
                t.printStackTrace();
                pd.dismiss();
            }
        });
    }

    private void retrieveAbsenPembayaran(){
        DetailPembayaranRVAdapter adapter = new DetailPembayaranRVAdapter(this, absenPembayaranArrayList);
        rvDetailPembayaran.setAdapter(adapter);
        rvDetailPembayaran.setLayoutManager(new LinearLayoutManager(this));
    }
}
