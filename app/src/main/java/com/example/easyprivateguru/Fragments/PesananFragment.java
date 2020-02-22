package com.example.easyprivateguru.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyprivateguru.Adapters.PesananRVAdapter;
import com.example.easyprivateguru.DummyGenerator;
import com.example.easyprivateguru.Models.Jenjang;
import com.example.easyprivateguru.Models.MataPelajaran;
import com.example.easyprivateguru.Models.Pesanan;
import com.example.easyprivateguru.Models.User;
import com.example.easyprivateguru.R;

import java.util.ArrayList;

public class PesananFragment extends Fragment {
    RecyclerView rvPesanan;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pesanan, container, false);
        Context mContext = v.getContext();

        init(v);

        DummyGenerator dg = new DummyGenerator();

        PesananRVAdapter adapter = new PesananRVAdapter(mContext, dg.getPesananDummy());
        rvPesanan.setAdapter(adapter);
        rvPesanan.setLayoutManager(new LinearLayoutManager(mContext));

        return v;
    }

    private void init(View v){
        rvPesanan = v.findViewById(R.id.rvPesanan);
    }
}
