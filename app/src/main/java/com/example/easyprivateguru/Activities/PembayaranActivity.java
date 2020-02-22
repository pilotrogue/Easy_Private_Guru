package com.example.easyprivateguru.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.easyprivateguru.Adapters.PembayaranRVAdapter;
import com.example.easyprivateguru.DummyGenerator;
import com.example.easyprivateguru.Models.Pembayaran;
import com.example.easyprivateguru.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PembayaranActivity extends AppCompatActivity {
    RecyclerView rvPembayaran;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembayaran);

        init();

        DummyGenerator dg = new DummyGenerator();
        PembayaranRVAdapter adapter = new PembayaranRVAdapter(this, dg.getPembayaranDummy());

        rvPembayaran.setAdapter(adapter);
        rvPembayaran.setLayoutManager(new LinearLayoutManager(this));
    }

    private void init(){
        rvPembayaran = findViewById(R.id.rvPembayaran);
    }
}
