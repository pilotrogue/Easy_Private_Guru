package com.easyprivate.easyprivateguru.api;

import android.app.ProgressDialog;
import android.content.Context;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {
    private Retrofit retrofit;

    //Local
//    public static final String BASE_URL = "http://192.168.1.28/easyprivate/public/";

    //Hosting
    public static final String BASE_URL = "http://easyprivate.skripsijtik.net/";

    public static final String DEFAULT_TITLE = "Tunggu sebentar ya!";
    public static final String DEFAULT_MESSAGE = "Menyambungkan dengan server";

    public ApiInterface getApiInterface(){
        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL+"api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        return apiInterface;
    }

    public ProgressDialog getProgressDialog(Context mContext){
        ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setTitle(DEFAULT_TITLE);
        progressDialog.setMessage(DEFAULT_MESSAGE);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);

        return progressDialog;
    }

    public ProgressDialog getProgressDialog(Context mContext, String title, String message){
        ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);

        return progressDialog;
    }

    public ProgressDialog getProgressDialog(Context mContext, String message){
        ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setTitle(DEFAULT_TITLE);
        progressDialog.setMessage(message);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);

        return progressDialog;
    }
}
