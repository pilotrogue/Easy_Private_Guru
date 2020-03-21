package com.example.easyprivateguru.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyprivateguru.adapters.PesananRVAdapter;
import com.example.easyprivateguru.DummyGenerator;
import com.example.easyprivateguru.R;
import com.example.easyprivateguru.api.ApiInterface;
import com.example.easyprivateguru.api.RetrofitClientInstance;
import com.example.easyprivateguru.models.Pemesanan;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PemesananFragment extends Fragment {
    private RecyclerView rvPesanan;
    private RetrofitClientInstance rci = new RetrofitClientInstance();
    private ApiInterface apiInterface = rci.getApiInterface();

    private static final String TAG = "PemesananFragment";

    private ArrayList<Pemesanan> pemesanans = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pesanan, container, false);
        Context mContext = v.getContext();

        init(v);
        callPemesanans(v);

        return v;
    }

    private void init(View v){
        rvPesanan = v.findViewById(R.id.rvPesanan);
    }

    private void callPemesanans(View view){
        Call<ArrayList<Pemesanan>> call = apiInterface.getAllPemesanan();
        call.enqueue(new Callback<ArrayList<Pemesanan>>() {
            @Override
            public void onResponse(Call<ArrayList<Pemesanan>> call, Response<ArrayList<Pemesanan>> response) {
                if(!response.isSuccessful()){
                    Log.d(TAG, "onResponse: "+ response.message());
                    return;
                }
                retrievePemesanan(response.body(), view);
            }

            @Override
            public void onFailure(Call<ArrayList<Pemesanan>> call, Throwable t) {
//                pemesanans = null;
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
    }

    private void retrievePemesanan(ArrayList<Pemesanan> p, View view){
        pemesanans = p;

        PesananRVAdapter adapter = new PesananRVAdapter(view.getContext(), pemesanans);
        rvPesanan.setAdapter(adapter);
        rvPesanan.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }
}
