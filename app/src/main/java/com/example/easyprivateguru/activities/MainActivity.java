package com.example.easyprivateguru.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.easyprivateguru.UserHelper;
import com.example.easyprivateguru.fragments.AbsenFragment;
import com.example.easyprivateguru.fragments.HomeFragment;
import com.example.easyprivateguru.fragments.PemesananFragment;
import com.example.easyprivateguru.fragments.ProfilFragment;
import com.example.easyprivateguru.R;
import com.example.easyprivateguru.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bnv;
    private UserHelper uh;
    private static final String TAG = "MainActivity";
    private boolean backHasBeenPressed = false;

    @Override
    public void onBackPressed() {
        if(backHasBeenPressed) {
            super.onBackPressed();
        }else{
            Toast.makeText(this, "Tekan kembali untuk keluar", Toast.LENGTH_LONG).show();
            backHasBeenPressed = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    backHasBeenPressed = false;
                }
            }, 1500);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get current user
        uh = new UserHelper(this);
        Log.d(TAG, "onCreate: user.email: "+uh.retrieveUser().getEmail());

        bnv = findViewById(R.id.nav_bottom);

        //Default fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_main, new HomeFragment(), "currFrag").commit();

        //Nav onclick listener
        BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = getSupportFragmentManager().findFragmentByTag("currFrag");

                switch (item.getItemId()){
                    //Home
                    case R.id.nav_home:
                        selectedFragment = new HomeFragment();
                        break;
                    //Pesanan
                    case R.id.nav_pemesanan:
                        selectedFragment = new PemesananFragment();
                        break;
                    //QR Scanner
                    case R.id.nav_qr:
                        selectedFragment = new AbsenFragment();
                        break;
                    //Profil
                    case R.id.nav_profile:
                        selectedFragment = new ProfilFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_main, selectedFragment, "currFrag").commit();
                return true;
            }
        };

        bnv.setOnNavigationItemSelectedListener(navListener);
    }
}
