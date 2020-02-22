package com.example.easyprivateguru.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.easyprivateguru.Adapters.MuridSayaRVAdapter;
import com.example.easyprivateguru.Models.Jenjang;
import com.example.easyprivateguru.Models.MataPelajaran;
import com.example.easyprivateguru.Models.Pesanan;
import com.example.easyprivateguru.Models.User;
import com.example.easyprivateguru.R;

import java.util.ArrayList;

public class MuridSayaActivity extends AppCompatActivity {
    RecyclerView rvMuridSaya;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_murid_saya);

        init();
        MuridSayaRVAdapter adapter = new MuridSayaRVAdapter(this, getPesananDummy());
        rvMuridSaya.setAdapter(adapter);
        rvMuridSaya.setLayoutManager(new LinearLayoutManager(this));
    }

    private void init(){
        rvMuridSaya = findViewById(R.id.rvMuridSaya);
    }

    private ArrayList<Pesanan> getPesananDummy(){
        User user1 = new User("Lala", "lala@email.com", "Jakarta Barat", 1);
        User user2 = new User("Lulu", "lulu@email.com", "Jakarta Utara", 1);
        User user3 = new User("Lili", "lili@email.com", "Jakarta Timur", 1);
        User user4 = new User("Rega", "rega@email.com", "Jakarta Pusat", 2);

        Jenjang j1 = new Jenjang("SD", 200000, 75000);

        MataPelajaran m1 = new MataPelajaran("Matematika", j1);
        MataPelajaran m2 = new MataPelajaran("IPA", j1);
        MataPelajaran m3 = new MataPelajaran("Bahasa Inggris", j1);

        Pesanan p1 = new Pesanan(user1, user4, m1);
        Pesanan p2 = new Pesanan(user2, user4, m2);
        Pesanan p3 = new Pesanan(user3, user4, m3);

        ArrayList<Pesanan> pesanans = new ArrayList<>();

        pesanans.add(p1);
        pesanans.add(p2);
        pesanans.add(p3);

        return pesanans;
    }
}
