package com.easyprivate.easyprivateguru.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easyprivate.easyprivateguru.CustomUtility;
import com.easyprivate.easyprivateguru.R;
import com.easyprivate.easyprivateguru.UserHelper;
import com.easyprivate.easyprivateguru.api.ApiInterface;
import com.easyprivate.easyprivateguru.api.RetrofitClientInstance;
import com.easyprivate.easyprivateguru.models.Alamat;
import com.easyprivate.easyprivateguru.models.JadwalPemesananPerminggu;
import com.easyprivate.easyprivateguru.models.MataPelajaran;
import com.easyprivate.easyprivateguru.models.Pemesanan;
import com.easyprivate.easyprivateguru.models.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailJadwalActivity extends AppCompatActivity {
    private Intent currIntent;
    private UserHelper userHelper;
    private CustomUtility customUtility;
    private RetrofitClientInstance rci = new RetrofitClientInstance();
    private ApiInterface apiInterface = rci.getApiInterface();
    private JadwalPemesananPerminggu currJadwalPemesananPerminggu;

    private boolean mLocationPermission = false;

    private CircleImageView civProfilePic;
    private TextView tvNoTelp, tvNamaMurid, tvAlamatMurid, tvWaktuBelajar, tvMapel, tvJenjang;
    private GoogleMap gMap;
    private LinearLayout llBtnNoTelp, llBtnNavigation;

    private boolean hasBeenPaused = false;

    private static final String TAG = "DetailJadwalActivity";
    private static final int DEFAULT_MAP_ZOOM = 15;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;

    @Override
    protected void onResume() {
        super.onResume();
        if(hasBeenPaused){
            hasBeenPaused = false;
            callJadwalPemesananPerminggu(currIntent.getIntExtra("idJadwalPemesananPerminggu", 0));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        hasBeenPaused = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_jadwal);

        init();

        Intent currIntent = getIntent();
        int idJadwalPemesananPerminggu = currIntent.getIntExtra("idJadwalPemesananPerminggu", 0);

        callJadwalPemesananPerminggu(idJadwalPemesananPerminggu);
    }

    private void init(){
        userHelper = new UserHelper(this);
        customUtility = new CustomUtility(this);
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

    private void callJadwalPemesananPerminggu(int id){
        Call<JadwalPemesananPerminggu> call = apiInterface.getJadwalPemesananPermingguById(id);
        ProgressDialog progressDialog = rci.getProgressDialog(this, "Menampilkan jadwal...");
        call.enqueue(new Callback<JadwalPemesananPerminggu>() {
            @Override
            public void onResponse(Call<JadwalPemesananPerminggu> call, Response<JadwalPemesananPerminggu> response) {
                Log.d(TAG, "onResponse: "+response.message());
                if(!response.isSuccessful()){
                    return;
                }

                currJadwalPemesananPerminggu = response.body();
                retrieveJadwalPemesananPerminggu();
            }

            @Override
            public void onFailure(Call<JadwalPemesananPerminggu> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
                t.printStackTrace();
            }
        });
    }

    private void retrieveJadwalPemesananPerminggu(){
        Pemesanan pemesanan = currJadwalPemesananPerminggu.getPemesanan();
        User murid = pemesanan.getMurid();

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
        customUtility.putIntoImage(murid.getAvatar(), civProfilePic);

        //Menampilkan nomor telepon pada button
        llBtnNoTelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPhone(murid.getNoHandphone());
            }
        });

        //Menampilkan nama
        tvNamaMurid.setText(murid.getName());

        //Menampilkan alamat
        Address address = customUtility.getAddress(currAlamat.getLatitude(), currAlamat.getLongitude());

        String alamatStr = "";
        if(address == null){
            alamatStr = currAlamat.getAlamatLengkap();
        }else{
            alamatStr = address.getSubLocality()+", "+address.getLocality()+", "+address.getSubAdminArea()+", "+address.getAdminArea()+", "+address.getCountryName();
        }

        tvAlamatMurid.setText(alamatStr);

        //Menampilkan waktu ajar
        String startStr = customUtility.reformatDateTime(currJadwalPemesananPerminggu.getJadwalAvailable().getStart(), "HH:mm:ss", "HH:mm");
        String endStr = customUtility.reformatDateTime(currJadwalPemesananPerminggu.getJadwalAvailable().getEnd(), "HH:mm:ss", "HH:mm");
        String timeStr = currJadwalPemesananPerminggu.getJadwalAvailable().getHari() + ", "+startStr + " s/d " + endStr;
        tvWaktuBelajar.setText(timeStr);

        //Menampilkan mata pelajaran dan jenjang
        MataPelajaran mapel = pemesanan.getMataPelajaran();
        tvMapel.setText(mapel.getNamaMapel());
        tvJenjang.setText(mapel.getJenjang().getNamaJenjang());
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
}
