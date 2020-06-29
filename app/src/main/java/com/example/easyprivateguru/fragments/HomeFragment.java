package com.example.easyprivateguru.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.easyprivateguru.activities.AddEventActivity;
import com.example.easyprivateguru.activities.DetailPembayaranActivity;
import com.example.easyprivateguru.activities.JadwalActivity;
import com.example.easyprivateguru.activities.MuridSayaActivity;
import com.example.easyprivateguru.activities.PembayaranActivity;
import com.example.easyprivateguru.activities.QRCodeActivity;
import com.example.easyprivateguru.R;

public class HomeFragment extends Fragment {

    private LinearLayout llJadwal, llMuridSaya, llPembayaran;
    private Button btnShowQR, btnAddEvent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        Context mContext = v.getContext();

        init(v);

        llPembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext, "Pembayaran", Toast.LENGTH_LONG).show();
                Intent i = new Intent(mContext, DetailPembayaranActivity.class);
                startActivity(i);
            }
        });

        llJadwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext, "Jadwal", Toast.LENGTH_LONG).show();
                Intent i = new Intent(mContext, JadwalActivity.class);
                startActivity(i);
            }
        });

        llMuridSaya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext, "Murid Saya", Toast.LENGTH_LONG).show();
                Intent i = new Intent(mContext, MuridSayaActivity.class);
                startActivity(i);
            }
        });

        btnShowQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, QRCodeActivity.class);
                startActivity(i);
            }
        });

        btnAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, AddEventActivity.class);
                startActivity(i);
            }
        });

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void init(View v){
        llPembayaran = v.findViewById(R.id.llPembayaran);
        llJadwal = v.findViewById(R.id.llJadwal);
        llMuridSaya = v.findViewById(R.id.llMuridSaya);

        btnShowQR = v.findViewById(R.id.btnShowQR);

        btnAddEvent = v.findViewById(R.id.btnAddEvent);
    }
}
