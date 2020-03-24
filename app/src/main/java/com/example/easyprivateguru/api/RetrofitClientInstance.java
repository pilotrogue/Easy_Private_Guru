package com.example.easyprivateguru.api;

import android.app.ProgressDialog;
import android.content.Context;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {
    private Retrofit retrofit;
    private static final String BASE_URL = "http://192.168.1.21/easyprivate/public/api/";

    public ApiInterface getApiInterface(){
        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        return apiInterface;
    }

    public String getBaseUrl(){
        return BASE_URL;
    }

    public ProgressDialog getProgressDialog(Context mContext){
        ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setTitle("Mohon tunggu");
        progressDialog.setMessage("Menghubungkan dengan server");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);

        return progressDialog;
    }
}
