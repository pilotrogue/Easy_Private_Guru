package com.example.easyprivateguru.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.easyprivateguru.R;
import com.example.easyprivateguru.api.ApiInterface;
import com.example.easyprivateguru.api.RetrofitClientInstance;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreenActivity extends AppCompatActivity {
    GoogleSignInAccount account;
    private static final String TAG = "SplashScreenActivity";

    //API
    private RetrofitClientInstance rci = new RetrofitClientInstance();
    private ApiInterface apiInterface = rci.getApiInterface();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
    }

    @Override
    protected void onStart() {
        super.onStart();
        account = GoogleSignIn.getLastSignedInAccount(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(account!=null){
                    guruValidation(account.getEmail());
                }else{
                    Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    startActivity(i);
                }
            }
        }, 1000);
    }

    private void guruValidation(String emailStr){
        Log.d(TAG, "guruValidation: called");
        Call<Integer> call = apiInterface.isGuruValid(emailStr);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                Log.d(TAG, "onResponse: "+response.message());
                if(!response.isSuccessful()){
                    return;
                }

                int guruStatus = response.body();
                if (guruStatus == 1){
                    Toast.makeText(SplashScreenActivity.this, "Selamat datang!", Toast.LENGTH_LONG).show();
                    homeIntent();
                }else {
                    daftarIntent();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
                Toast.makeText(SplashScreenActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                return;
            }
        });
    }

    //Fungsi buat ngarahin user ke halaman home
    private void homeIntent(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    //Fungsi buat ngarahin user ke halaman daftar
    private void daftarIntent(){
        Intent i = new Intent(this, DaftarActivity.class);
        startActivity(i);
        finish();
    }
}
