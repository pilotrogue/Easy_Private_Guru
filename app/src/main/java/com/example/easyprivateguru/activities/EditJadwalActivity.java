package com.example.easyprivateguru.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CheckBox;

import com.example.easyprivateguru.R;

public class EditJadwalActivity extends AppCompatActivity {
    private CheckBox cbSenin, cbSelasa, cbRabu, cbKamis, cbJumat, cbSabtu, cbMinggu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_jadwal);
        init();
    }

    private void init(){
        cbSenin = findViewById(R.id.cbSenin);
        cbSelasa = findViewById(R.id.cbSelasa);
        cbRabu = findViewById(R.id.cbRabu);
        cbKamis = findViewById(R.id.cbKamis);
        cbJumat = findViewById(R.id.cbJumat);
        cbSabtu = findViewById(R.id.cbSabtu);
        cbMinggu = findViewById(R.id.cbMinggu);
    }
}
