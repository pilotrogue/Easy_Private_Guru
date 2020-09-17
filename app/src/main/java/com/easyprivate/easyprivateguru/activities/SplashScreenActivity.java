package com.easyprivate.easyprivateguru.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.easyprivate.easyprivateguru.R;
import com.easyprivate.easyprivateguru.UserHelper;
import com.easyprivate.easyprivateguru.api.ApiInterface;
import com.easyprivate.easyprivateguru.api.RetrofitClientInstance;
import com.easyprivate.easyprivateguru.models.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

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

    private Button btnRetry, btnGantiAkun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        userHelper = new UserHelper(this);
        account = GoogleSignIn.getLastSignedInAccount(this);

        btnRetry = findViewById(R.id.btnRetry);
        btnGantiAkun = findViewById(R.id.btnGantiAkun);

        btnRetry.setVisibility(View.GONE);
        btnGantiAkun.setVisibility(View.GONE);

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

        btnGantiAkun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginIntent();
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
                loginIntent();
                Log.d(TAG, "onFailure: "+t.getMessage());
//                Toast.makeText(SplashScreenActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    //Fungsi buat ngarahin user ke halaman home
    private void homeIntent(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private void loginIntent(){
        if(account!=null) {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();

            GoogleSignInClient client = GoogleSignIn.getClient(this, gso);
            client.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            });
            ;
        }else{
            Intent iz = new Intent(SplashScreenActivity.this, LoginActivity.class);
            startActivity(iz);
            finish();
        }
    }

    //Fungsi buat ngarahin user ke halaman daftar
    private void daftarIntent(){
        Intent i = new Intent(this, DaftarActivity.class);
        startActivity(i);
        finish();
    }
}
