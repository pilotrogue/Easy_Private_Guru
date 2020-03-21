package com.example.easyprivateguru.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.easyprivateguru.DummyGenerator;
import com.example.easyprivateguru.adapters.MuridSayaRVAdapter;
import com.example.easyprivateguru.models.Jenjang;
import com.example.easyprivateguru.models.MataPelajaran;
import com.example.easyprivateguru.models.Pemesanan;
import com.example.easyprivateguru.models.User;
import com.example.easyprivateguru.R;

import java.util.ArrayList;

public class MuridSayaActivity extends AppCompatActivity {
    RecyclerView rvMuridSaya;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_murid_saya);

        init();
        DummyGenerator dg = new DummyGenerator();
        MuridSayaRVAdapter adapter = new MuridSayaRVAdapter(this, dg.getPesananDummy());
        rvMuridSaya.setAdapter(adapter);
        rvMuridSaya.setLayoutManager(new LinearLayoutManager(this));
    }

    private void init(){
        rvMuridSaya = findViewById(R.id.rvMuridSaya);
    }

//    private ArrayList<Pemesanan> getPesananDummy(){
//        User user1 = new User("Lala", "lala@email.com", "Jakarta Barat", 1);
//        User user2 = new User("Lulu", "lulu@email.com", "Jakarta Utara", 1);
//        User user3 = new User("Lili", "lili@email.com", "Jakarta Timur", 1);
//        User user4 = new User("Rega", "rega@email.com", "Jakarta Pusat", 2);
//
//        Jenjang j1 = new Jenjang("SD", 200000, 75000);
//
//        MataPelajaran m1 = new MataPelajaran("Matematika", j1);
//        MataPelajaran m2 = new MataPelajaran("IPA", j1);
//        MataPelajaran m3 = new MataPelajaran("Bahasa Inggris", j1);
//
//        Pemesanan p1 = new Pemesanan(user1, user4, m1);
//        Pemesanan p2 = new Pemesanan(user2, user4, m2);
//        Pemesanan p3 = new Pemesanan(user3, user4, m3);
//
//        ArrayList<Pemesanan> pesanans = new ArrayList<>();
//
//        pesanans.add(p1);
//        pesanans.add(p2);
//        pesanans.add(p3);
//
//        return pesanans;
//    }
}
