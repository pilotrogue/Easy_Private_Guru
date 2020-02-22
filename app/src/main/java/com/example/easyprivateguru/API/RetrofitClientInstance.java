package com.example.easyprivateguru.API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {
    private Retrofit retrofit;
    private static final String BASE_URL = "IP Address masing masing";

    public ApiInterface getRetrofit(){
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
}
