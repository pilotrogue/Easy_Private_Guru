package com.easyprivate.easyprivateguru.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    //Ini button buat sign in
    private SignInButton btnSignIn;

    //Ini button buat register
    private Button btnDaftar;

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

    private UserHelper userHelper;

    //Sebelum mulai activity, sistem ngecek dulu apakah udah ada yang pernah login atau belum
    @Override
    protected void onStart() {
        super.onStart();

        userHelper = new UserHelper(this);
        account = GoogleSignIn.getLastSignedInAccount(this);
        client = GoogleSignIn.getClient(this, gso);

        if(account != null){
            callGuru(account.getEmail());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

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

        btnDaftar = findViewById(R.id.btnDaftar);
    }

    //Membuka website pendaftaran
    private void signUp(){
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(RetrofitClientInstance.BASE_URL+"login/"));
        startActivity(i);
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
//                String name = account.getAccount().name;
//                Log.d(TAG, "onActivityResult: account name: "+name);

                String emailStr = account.getEmail();
                callGuru(emailStr);
            }catch (ApiException e){
                //Jaga jaga kalo ada yang error
                e.printStackTrace();

                //Kalo error nya hanya karena user menekan tombol back, tidak akan muncul toast
                if(e.getStatusCode() != 12501) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }
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

                Toast.makeText(LoginActivity.this, "Selamat datang!", Toast.LENGTH_LONG).show();
                if(u.getRole() == 2) {
                    Log.d(TAG, "onResponse: role: Guru sudah terdaftar");
                    homeIntent();
                }else{
                    Log.d(TAG, "onResponse: role: Guru belum terdaftar");
                    daftarIntent();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                p.dismiss();
                daftarIntent();
                Log.d(TAG, "onFailure: "+t.getMessage());
//                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
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
