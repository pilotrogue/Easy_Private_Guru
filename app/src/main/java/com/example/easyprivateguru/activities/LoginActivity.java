package com.example.easyprivateguru.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.easyprivateguru.R;
import com.example.easyprivateguru.api.ApiInterface;
import com.example.easyprivateguru.api.RetrofitClientInstance;
import com.example.easyprivateguru.models.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    //Ini button buat sign in
    private SignInButton btnSignIn;

    //Buat nyimpen akun user yang login
    private GoogleSignInAccount account;
    private GoogleSignInOptions gso;
    private GoogleSignInClient client;

    //API
    private RetrofitClientInstance rci = new RetrofitClientInstance();
    private ApiInterface apiInterface = rci.getApiInterface();

    //Tag buat debug aja, gak penting penting amat
    private static final String TAG = "LoginActivity";

    //Ini gue gak tahu buat apa wkwk, tapi ini perlu
    private int RC_SIGN_IN = 0;

    //Sebelum mulai activity, sistem ngecek dulu apakah udah ada yang pernah login atau belum
    @Override
    protected void onStart() {
        super.onStart();
        account = GoogleSignIn.getLastSignedInAccount(this);
        client = GoogleSignIn.getClient(this, gso);

        if(account != null){
            guruValidation(account.getEmail());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

        //Supaya button bisa dipencet
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    //Nyambungin elemen xml ke java class
    private void init(){
        btnSignIn = findViewById(R.id.btn_sign_in);
        btnSignIn.setSize(SignInButton.SIZE_WIDE);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
    }

    //Fungsi munculin pop up pilih akun
    private void signIn(){
        Intent signInIntent = client.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    //Fungsi buat nerima akun yang dipilih user
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                account = task.getResult(ApiException.class);
                loginGuru(account);
            }catch (ApiException e){
                //Jaga jaga kalo ada yang error
                Log.v(TAG, e.getMessage());

                //Kalo error nya hanya karena user menekan tombol back, tidak akan muncul toast
                if(e.getStatusCode() != 12501) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    //Memeriksa apakah guru tergolong valid
    private void loginGuru(GoogleSignInAccount acc){
        Log.d(TAG, "loginGuru: called");
        String emailStr = acc.getEmail();

        guruValidation(emailStr);
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
                    Toast.makeText(LoginActivity.this, "Selamat datang!", Toast.LENGTH_LONG).show();
                    homeIntent();
                }else {
                    daftarIntent();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
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
