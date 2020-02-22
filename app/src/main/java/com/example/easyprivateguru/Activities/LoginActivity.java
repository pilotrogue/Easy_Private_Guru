package com.example.easyprivateguru.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.easyprivateguru.R;
import com.google.android.gms.common.SignInButton;

public class LoginActivity extends AppCompatActivity {

    private Button btnSignIn;
//    private SignInButton btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    private void init(){
        btnSignIn = findViewById(R.id.btn_sign_in);
    }

    private void signIn(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
