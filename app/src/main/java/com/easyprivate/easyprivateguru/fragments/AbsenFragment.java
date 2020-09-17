package com.easyprivate.easyprivateguru.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.easyprivate.easyprivateguru.UserHelper;
import com.easyprivate.easyprivateguru.activities.QRScannerActivity;
import com.easyprivate.easyprivateguru.adapters.AbsenRVAdapter;
import com.easyprivate.easyprivateguru.R;
import com.easyprivate.easyprivateguru.api.ApiInterface;
import com.easyprivate.easyprivateguru.api.RetrofitClientInstance;
import com.easyprivate.easyprivateguru.models.Absen;
import com.easyprivate.easyprivateguru.models.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AbsenFragment extends Fragment {
    RetrofitClientInstance rci = new RetrofitClientInstance();
    ApiInterface apiInterface  = rci.getApiInterface();

    private UserHelper userHelper;
    private User currUser;
    private ArrayList<Absen> absens = new ArrayList<>();

    private RecyclerView rvAbsen;
    private LinearLayout llBtnScanner;

    private Context mContext;

    private boolean hasBeenRefreshed = true;

    private static final String TAG = "AbsenFragment";
    private static final Integer REQUEST_CODE = 123;

    @Override
    public void onResume() {
        super.onResume();
        if(hasBeenRefreshed == false){
            absens.clear();
            rvAbsen.setAdapter(null);

            currUser = userHelper.retrieveUser();
            callAbsen();
            hasBeenRefreshed = true;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        hasBeenRefreshed = false;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_absen, container, false);
        mContext = v.getContext();

        init(v);
        callAbsen();

        llBtnScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, QRScannerActivity.class);
                mContext.startActivity(i);
            }
        });

        return v;
    }

    private void init(View v){
        rvAbsen = v.findViewById(R.id.rvAbsen);
        llBtnScanner = v.findViewById(R.id.llBtnQRScanner);

        userHelper = new UserHelper(mContext);
        currUser = userHelper.retrieveUser();
    }

    private void callAbsen(){
        ProgressDialog progressDialog = rci.getProgressDialog(mContext, "Menampilkan daftar absen");
        Call<ArrayList<Absen>> call = apiInterface.getAbsen(null, null, null, null, currUser.getId(), null);

        progressDialog.show();
        call.enqueue(new Callback<ArrayList<Absen>>() {
            @Override
            public void onResponse(Call<ArrayList<Absen>> call, Response<ArrayList<Absen>> response) {
                progressDialog.dismiss();
                Log.d(TAG, "onResponse: "+response.message());
                if(!response.isSuccessful()){
                    return;
                }

                absens = response.body();
                Log.d(TAG, "onResponse: absens.size: "+absens.size());
                retrieveAbsens();
            }

            @Override
            public void onFailure(Call<ArrayList<Absen>> call, Throwable t) {
                progressDialog.dismiss();
                Log.d(TAG, "onFailure: "+t.getMessage());
                return;
            }
        });
    }

    private void retrieveAbsens(){
        AbsenRVAdapter adapter = new AbsenRVAdapter(mContext, absens);
        rvAbsen.setAdapter(adapter);
        rvAbsen.setLayoutManager(new LinearLayoutManager(mContext));
    }
}
