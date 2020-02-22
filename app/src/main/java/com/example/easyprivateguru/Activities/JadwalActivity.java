package com.example.easyprivateguru.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.easyprivateguru.Adapters.JadwalRVAdapter;
import com.example.easyprivateguru.DummyGenerator;
import com.example.easyprivateguru.R;

public class JadwalActivity extends AppCompatActivity {
    RecyclerView rvJadwal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jadwal);

        init();

        DummyGenerator dg = new DummyGenerator();
        JadwalRVAdapter adapter = new JadwalRVAdapter(this, dg.getAbsenDummy());
        rvJadwal.setAdapter(adapter);
        rvJadwal.setLayoutManager(new LinearLayoutManager(this));
    }

    private void init(){
        rvJadwal = findViewById(R.id.rvJadwal);
    }
}
