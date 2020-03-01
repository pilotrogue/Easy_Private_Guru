package com.example.easyprivateguru.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyprivateguru.Activities.QRScannerActivity;
import com.example.easyprivateguru.Adapters.JadwalRVAdapter;
import com.example.easyprivateguru.DummyGenerator;
import com.example.easyprivateguru.R;

public class AbsenFragment extends Fragment {
    RecyclerView rvAbsen;
    ImageButton iBtnQRScanner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_absen, container, false);
        Context mContext = v.getContext();

        init(v);

        iBtnQRScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, QRScannerActivity.class);
                mContext.startActivity(i);
            }
        });

        DummyGenerator dg = new DummyGenerator();
        JadwalRVAdapter adapter = new JadwalRVAdapter(mContext, dg.getAbsenDummy());
        rvAbsen.setAdapter(adapter);
        rvAbsen.setLayoutManager(new LinearLayoutManager(mContext));

        return v;
    }

    private void init(View v){
        rvAbsen = v.findViewById(R.id.rvAbsen);
        iBtnQRScanner = v.findViewById(R.id.iBtnQRScanner);
    }
}
