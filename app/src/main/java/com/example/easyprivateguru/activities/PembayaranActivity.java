package com.example.easyprivateguru.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.easyprivateguru.adapters.PembayaranRVAdapter;
import com.example.easyprivateguru.R;

public class PembayaranActivity extends AppCompatActivity {
    RecyclerView rvPembayaran;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembayaran);

        init();
    }

    private void init(){
        rvPembayaran = findViewById(R.id.rvPembayaran);
    }
}
