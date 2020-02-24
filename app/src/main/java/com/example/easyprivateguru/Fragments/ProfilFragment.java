package com.example.easyprivateguru.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.easyprivateguru.Activities.LoginActivity;
import com.example.easyprivateguru.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class ProfilFragment extends Fragment {
    TextView tvProfile;
    Button btnSignOut;
    GoogleSignInAccount account;
    Context mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profil, container, false);

        init(v);
        account = GoogleSignIn.getLastSignedInAccount(mContext);

        if(account != null){
            tvProfile.setText("Email \t: "+account.getEmail()+"\n"+"Name \t: "+account.getDisplayName());
        }else {
            tvProfile.setText("No one signed in");
        }

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        return v;
    }

    private void init(View v){
        tvProfile = v.findViewById(R.id.tvProfile);
        btnSignOut = v.findViewById(R.id.btnSignOut);
        mContext = v.getContext();
    }

    private void signOut(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        GoogleSignInClient client = GoogleSignIn.getClient(mContext, gso);
        client.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(mContext, "Sign out successful", Toast.LENGTH_LONG).show();
                Intent i = new Intent(mContext, LoginActivity.class);
                mContext.startActivity(i);
                getActivity().finish();
            }
        });
    }
}
