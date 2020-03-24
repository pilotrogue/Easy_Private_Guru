package com.example.easyprivateguru.fragments;

import android.app.ProgressDialog;
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
import com.example.easyprivateguru.R;
import com.example.easyprivateguru.api.ApiInterface;
import com.example.easyprivateguru.api.RetrofitClientInstance;
import com.example.easyprivateguru.models.Pemesanan;
import com.example.easyprivateguru.models.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PemesananFragment extends Fragment {
    private RecyclerView rvPesanan;
    private RetrofitClientInstance rci = new RetrofitClientInstance();
    private ApiInterface apiInterface = rci.getApiInterface();

    private GoogleSignInAccount account;

    private static final String TAG = "PemesananFragment";

    private ArrayList<Pemesanan> pemesanans = new ArrayList<>();
    private User currUser;

    private View v;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_pesanan, container, false);

        init(v);
        callGuru();

        return v;
    }

    private void init(View v){
        rvPesanan = v.findViewById(R.id.rvPesanan);
        mContext = v.getContext();

        account = GoogleSignIn.getLastSignedInAccount(mContext);
    }

    private void callGuru(){
        Call<User> call = apiInterface.getGuru(account.getEmail());

        ProgressDialog p = rci.getProgressDialog(mContext);
        p.show();

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.d(TAG, "onResponse: "+response.message());
                p.dismiss();
                if (!response.isSuccessful()){
                    return;
                }
                currUser = response.body();
                callPemesanans(currUser);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                p.dismiss();
                Log.d(TAG, "onFailure: "+t.getMessage());
                return;
            }
        });
    }

    private void callPemesanans(User u){
        Call<ArrayList<Pemesanan>> call = apiInterface.getPemesananByIdGuru(u.getId());

        ProgressDialog p = rci.getProgressDialog(mContext);
        p.show();

        call.enqueue(new Callback<ArrayList<Pemesanan>>() {
            @Override
            public void onResponse(Call<ArrayList<Pemesanan>> call, Response<ArrayList<Pemesanan>> response) {
                p.dismiss();
                if(!response.isSuccessful()){
                    Log.d(TAG, "onResponse: "+ response.message());
                    return;
                }
                retrievePemesanan(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Pemesanan>> call, Throwable t) {
                p.dismiss();
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
    }

    private void retrievePemesanan(ArrayList<Pemesanan> p){
        pemesanans = p;

        PesananRVAdapter adapter = new PesananRVAdapter(mContext, pemesanans);
        rvPesanan.setAdapter(adapter);
        rvPesanan.setLayoutManager(new LinearLayoutManager(mContext));
    }
}
