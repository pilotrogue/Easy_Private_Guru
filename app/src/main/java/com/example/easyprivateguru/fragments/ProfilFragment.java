package com.example.easyprivateguru.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ActionMenuView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.easyprivateguru.UserHelper;
import com.example.easyprivateguru.activities.LoginActivity;
import com.example.easyprivateguru.R;
import com.example.easyprivateguru.activities.SettingsActivity;
import com.example.easyprivateguru.models.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import de.hdodenhof.circleimageview.CircleImageView;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ProfilFragment extends Fragment {
    private CircleImageView civPic;
    private TextView tvEmail, tvName, tvRole, tvPemesananDiterima, tvPemesananAktif;
    private RatingBar ratingBar;
    private ImageView ivSettings;

    private Button btnSignOut;
    private GoogleSignInAccount account;
    private Context mContext;

    private UserHelper userHelper;
    private User currUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profil, container, false);

        init(v);
        showProfile();

        ivSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, SettingsActivity.class);
                mContext.startActivity(i);
            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        return v;
    }

    private void init(View v){
        mContext = v.getContext();
        userHelper = new UserHelper(mContext);
        currUser = userHelper.retrieveUser();
        account = GoogleSignIn.getLastSignedInAccount(mContext);
        Log.d(TAG, "init: profile Uri: "+account.getPhotoUrl().toString());

        ivSettings = v.findViewById(R.id.ivEdit);

        civPic = v.findViewById(R.id.civProfilePic);

        tvEmail = v.findViewById(R.id.tvEmail);
        tvName = v.findViewById(R.id.tvName);
        tvRole = v.findViewById(R.id.tvRole);
        tvPemesananDiterima = v.findViewById(R.id.tvPemesananDiterima);
        tvPemesananAktif = v.findViewById(R.id.tvPemesananAktif);

        ratingBar = v.findViewById(R.id.rbRating);

        btnSignOut = v.findViewById(R.id.btnSignOut);
    }

    private void showProfile(){
        userHelper.putIntoImage(currUser.getAvatar(), civPic);
        tvEmail.setText(currUser.getEmail());
        tvName.setText(currUser.getName());

        int role = currUser.getRole();
        if(role == 1){
            tvRole.setText("Murid");
        }else{
            tvRole.setText("Guru");
        }

    }

    private void showDialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
        alertDialog.setTitle("Sign Out");
        alertDialog.setMessage("Apakah Anda ingin sign out?");
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Sign out", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                signOut();
            }
        });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Kembali", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void signOut(){
        userHelper.removeUser();
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
