package com.easyprivate.easyprivateguru.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.easyprivate.easyprivateguru.R;
import com.easyprivate.easyprivateguru.api.ApiInterface;
import com.easyprivate.easyprivateguru.api.RetrofitClientInstance;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DaftarActivity extends AppCompatActivity {
    private GoogleSignInAccount account;
    private RetrofitClientInstance rci = new RetrofitClientInstance();
    private ApiInterface apiInterface = rci.getApiInterface();
    private static final String TAG = "DaftarActivity";

    private TextView tvGreeting;
    private Button btnDaftar, btnGantiAkun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);

        init();

        tvGreeting.setText("Halo, "+account.getDisplayName()+"!");

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

        btnGantiAkun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
    }

    private void init(){
        btnDaftar = findViewById(R.id.btnDaftar);
        btnGantiAkun = findViewById(R.id.btnGantiAkun);
        tvGreeting = findViewById(R.id.tvGreetings);

        account = GoogleSignIn.getLastSignedInAccount(DaftarActivity.this);
    }

    private void signOut(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        GoogleSignInClient client = GoogleSignIn.getClient(DaftarActivity.this, gso);
        client.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(DaftarActivity.this, "Sign out successful", Toast.LENGTH_LONG).show();
                Intent i = new Intent(DaftarActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: called");

        account = GoogleSignIn.getLastSignedInAccount(DaftarActivity.this);
        if(account!=null){
            loginGuru(account);
        }
    }

    //Memeriksa apakah guru tergolong valid
    private void loginGuru(GoogleSignInAccount acc){
        Log.d(TAG, "loginGuru: called");
        String emailStr = acc.getEmail();

        Call<Integer> call = apiInterface.isGuruValid(emailStr);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                Log.d(TAG, "onResponse: "+response.message());
                if(!response.isSuccessful()){
                    return;
                }

                int guruStatus = response.body();
                Log.d(TAG, "onResponse: guruStatus: "+guruStatus);
                if (guruStatus == 1){
                    Toast.makeText(DaftarActivity.this, "Selamat datang!", Toast.LENGTH_LONG);
                    homeIntent();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
                Toast.makeText(DaftarActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
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

    //Membuka website pendaftaran
    private void signUp(){
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(RetrofitClientInstance.BASE_URL+"login/"));
        startActivity(i);
    }
}
