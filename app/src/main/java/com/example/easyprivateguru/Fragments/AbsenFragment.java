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

import com.example.easyprivateguru.Adapters.JadwalRVAdapter;
import com.example.easyprivateguru.DummyGenerator;
import com.example.easyprivateguru.R;

public class AbsenFragment extends Fragment {
    RecyclerView rvAbsen;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_absen, container, false);
        Context mContext = v.getContext();

        init(v);

        DummyGenerator dg = new DummyGenerator();
        JadwalRVAdapter adapter = new JadwalRVAdapter(mContext, dg.getAbsenDummy());
        rvAbsen.setAdapter(adapter);
        rvAbsen.setLayoutManager(new LinearLayoutManager(mContext));

        return v;
    }

    private void init(View v){
        rvAbsen = v.findViewById(R.id.rvAbsen);
    }
}
