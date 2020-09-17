package com.easyprivate.easyprivateguru.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.easyprivate.easyprivateguru.R;

public class SettingsActivity extends AppCompatActivity {
    LinearLayout llProfil, llJadwal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        init();

        llProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingsActivity.this, EditProfileActivity.class);
                startActivity(i);
            }
        });

        llJadwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingsActivity.this, EditJadwalActivity.class);
                startActivity(i);
            }
        });
    }

    private void init(){
        llProfil = findViewById(R.id.llProfil);
        llJadwal = findViewById(R.id.llJadwal);
    }
}
