package com.example.easyprivateguru.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.easyprivateguru.R;
import com.example.easyprivateguru.UserHelper;
import com.example.easyprivateguru.api.ApiInterface;
import com.example.easyprivateguru.api.RetrofitClientInstance;
import com.example.easyprivateguru.models.Alamat;
import com.example.easyprivateguru.models.JadwalAjar;
import com.example.easyprivateguru.models.MataPelajaran;
import com.example.easyprivateguru.models.Pemesanan;
import com.example.easyprivateguru.models.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class DetailJadwalActivity extends AppCompatActivity {
    private Intent currIntent;
    private UserHelper userHelper;
    private RetrofitClientInstance rci = new RetrofitClientInstance();
    private ApiInterface apiInterface = rci.getApiInterface();
    private JadwalAjar currJadwalAjar;

    private boolean mLocationPermission = false;

    private CircleImageView civProfilePic;
    private TextView tvNoTelp, tvNamaMurid, tvAlamatMurid, tvWaktuBelajar, tvMapel, tvJenjang;
    private GoogleMap gMap;
    private LinearLayout llBtnNoTelp, llBtnNavigation;

    private static final String TAG = "DetailJadwalActivity";
    private static final int DEFAULT_MAP_ZOOM = 10;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_jadwal);

        init();

        Intent currIntent = getIntent();
        int idJadwalAjar = currIntent.getIntExtra("idJadwalAjar", 0);

        callJadwalById(idJadwalAjar);
    }

    private void init(){
        userHelper = new UserHelper(this);
        currIntent = getIntent();

        civProfilePic = findViewById(R.id.civProfilePic);
        llBtnNoTelp = findViewById(R.id.llBtnNoTelp);
        llBtnNavigation = findViewById(R.id.llBtnNavigation);

        tvNoTelp = findViewById(R.id.tvNoTelp);
        tvNamaMurid = findViewById(R.id.tvNamaMurid);
        tvAlamatMurid = findViewById(R.id.tvAlamatMurid);
        tvWaktuBelajar = findViewById(R.id.tvWaktuBelajar);
        tvMapel = findViewById(R.id.tvMapel);
        tvJenjang = findViewById(R.id.tvJenjang);

        initMap();
    }

    private void initMap(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragMap);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                gMap = googleMap;
                gMap.getUiSettings().setMapToolbarEnabled(false);
            }
        });
    }

    private void callJadwalById(int id){
        Call<JadwalAjar> call = apiInterface.getJadwalAjarById(id);
        ProgressDialog progressDialog = rci.getProgressDialog(this, "Menampilkan jadwal Anda");
        progressDialog.show();
        call.enqueue(new Callback<JadwalAjar>() {
            @Override
            public void onResponse(Call<JadwalAjar> call, Response<JadwalAjar> response) {
                Log.d(TAG, "onResponse: "+response.message());
                if(!response.isSuccessful()){
                    return;
                }
                currJadwalAjar = response.body();
                retrieveJadwalAjar();
            }

            @Override
            public void onFailure(Call<JadwalAjar> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
                return;
            }
        });
    }

    private void openPhone(String phoneNumber){
        Intent i = new Intent(Intent.ACTION_DIAL);
        i.setData(Uri.parse("tel:"+phoneNumber));

        if(i.resolveActivity(getPackageManager()) != null){
            startActivity(i);
        }
    }

    private void openDirections(LatLng latLng){
        String directionUriStr = "https://www.google.com/maps/dir/?api=1&destination="+latLng.latitude+","+latLng.longitude;
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(directionUriStr));
        startActivity(i);
    }

    private void mapMoveCamera(LatLng latLng){
        //Menggerakkan layar pada koordinat alamat
        gMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        //Memperbesar map
        gMap.moveCamera(CameraUpdateFactory.zoomTo(DEFAULT_MAP_ZOOM));
        LatLng latLng1 = gMap.getCameraPosition().target;
    }

    private void mapAddMarker(LatLng latLng, String locationNameStr){
        //Menambahkan pin merah
        gMap.addMarker(new MarkerOptions().position(latLng).title(locationNameStr));
    }

    private void retrieveJadwalAjar(){
        User murid = currJadwalAjar.getPemesanan().getMurid();
        Pemesanan pemesanan = currJadwalAjar.getPemesanan();

        //Menampilkan alamat pada Google Map
        Alamat currAlamat = murid.getAlamat();
        LatLng muridLocation = new LatLng(currAlamat.getLatitude(), currAlamat.getLongitude());

        if(pemesanan.getStatus() != 1){
            llBtnNavigation.setVisibility(View.INVISIBLE);
            llBtnNoTelp.setVisibility(View.INVISIBLE);
        }

        llBtnNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDirections(muridLocation);
            }
        });

        mapAddMarker(muridLocation, currAlamat.getAlamatLengkap());
        mapMoveCamera(muridLocation);

        //Menampilkan profile picture
        userHelper.putIntoImage(murid.getAvatar(), civProfilePic);

        //Menampilkan nomor telepon pada button
        tvNoTelp.setText("Hubungi "+murid.getNoHandphone());
        llBtnNoTelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPhone(murid.getNoHandphone());
            }
        });

        //Menampilkan nama
        tvNamaMurid.setText(murid.getName());

        //Menampilkan alamat
        tvAlamatMurid.setText(murid.getAlamat().getAlamatLengkap());

        //Menampilkan waktu ajar
        tvWaktuBelajar.setText(reformatDate(currJadwalAjar.getWaktuAjar()));

        //Menampilkan mata pelajaran dan jenjang
        MataPelajaran mapel = pemesanan.getMataPelajaran();
        tvMapel.setText(mapel.getNamaMapel());
        tvJenjang.setText(mapel.getJenjang().getNamaJenjang());
    }

    private String reformatDate(String dateStr){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            Date date = sdf.parse(dateStr);

            sdf.applyPattern("EEEE, dd MMMM yyyy");
            String tanggal = sdf.format(date);

            sdf.applyPattern("HH:mm");
            String waktu = sdf.format(date);

            return tanggal+", "+waktu;
        }catch (ParseException e){
            Log.d(TAG, "reformatDate: "+ e.getMessage());
            return "Error";
        }
    }
}
