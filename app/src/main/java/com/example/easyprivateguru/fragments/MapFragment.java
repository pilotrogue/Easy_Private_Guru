package com.example.easyprivateguru.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.easyprivateguru.R;
import com.example.easyprivateguru.models.Alamat;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.zip.Inflater;

public class MapFragment extends Fragment{
    private GoogleMap gMap;
    private Context mContext;
    private Alamat alamat;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mContext = view.getContext();

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                gMap = googleMap;
                LatLng latLng = new LatLng(alamat.getLatitude(), alamat.getLongitude());

                //Menambahkan pin
                gMap.addMarker(new MarkerOptions().position(latLng).title(alamat.getAlamatLengkap()));

                //Menggerakkan layar pada koordinat alamat
                gMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                //Memperbesar map
                gMap.moveCamera(CameraUpdateFactory.zoomTo(10));
            }
        });

        return view;
    }

    public MapFragment(Alamat alamat) {
        this.alamat = alamat;
    }
}
