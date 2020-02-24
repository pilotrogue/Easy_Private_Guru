package com.example.easyprivateguru.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.easyprivateguru.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class ProfilFragment extends Fragment {
    TextView tvProfile;
    GoogleSignInAccount account;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profil, container, false);
        Context mContext = v.getContext();

        init(v);
        account = GoogleSignIn.getLastSignedInAccount(mContext);

        if(account != null){
            tvProfile.setText("Email \t: "+account.getEmail()+"\n"+"Name \t: "+account.getDisplayName());
        }else {
            tvProfile.setText("No one signed in");
        }

        return v;
    }

    private void init(View v){
        tvProfile = v.findViewById(R.id.tvProfile);
    }
}
