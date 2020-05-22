package com.example.easyprivateguru.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.easyprivateguru.R;
import com.example.easyprivateguru.UserHelper;
import com.example.easyprivateguru.api.ApiInterface;
import com.example.easyprivateguru.api.RetrofitClientInstance;
import com.example.easyprivateguru.models.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreenActivity extends AppCompatActivity {
    private GoogleSignInAccount account;
    private static final String TAG = "SplashScreenActivity";

    //API
    private RetrofitClientInstance rci = new RetrofitClientInstance();
    private ApiInterface apiInterface = rci.getApiInterface();

    private UserHelper userHelper;

    private Button btnRetry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        userHelper = new UserHelper(this);
        account = GoogleSignIn.getLastSignedInAccount(this);
        btnRetry = findViewById(R.id.btnRetry);
        btnRetry.setVisibility(View.GONE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(account!=null){
                    callGuru(account.getEmail());
                }else{
                    Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }, 1000);

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRetry.setVisibility(View.GONE);
                if(account!=null){
                    callGuru(account.getEmail());
                }else{
                    Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void callGuru(String emailStr){
        Call<User> call = apiInterface.getGuru(emailStr);
        ProgressDialog p = rci.getProgressDialog(this, "Mengunduh informasi Anda");
        p.show();

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.d(TAG, "onResponse: "+response.message());
                p.dismiss();
                if (!response.isSuccessful()){
                    return;
                }

                User u = response.body();
                userHelper.storeUser(u);

                Toast.makeText(SplashScreenActivity.this, "Selamat datang!", Toast.LENGTH_LONG).show();
                homeIntent();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                p.dismiss();
                btnRetry.setVisibility(View.VISIBLE);
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
