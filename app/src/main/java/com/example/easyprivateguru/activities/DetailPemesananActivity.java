package com.example.easyprivateguru.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easyprivateguru.R;
import com.example.easyprivateguru.UserHelper;
import com.example.easyprivateguru.api.ApiInterface;
import com.example.easyprivateguru.api.RetrofitClientInstance;
import com.example.easyprivateguru.models.Alamat;
import com.example.easyprivateguru.models.MataPelajaran;
import com.example.easyprivateguru.models.Pemesanan;
import com.example.easyprivateguru.models.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailPemesananActivity extends AppCompatActivity{
    private RetrofitClientInstance rci = new RetrofitClientInstance();
    private ApiInterface apiInterface = rci.getApiInterface();
    private UserHelper userHelper;
    private Intent currIntent;
    private Pemesanan currPemesanan;
    private boolean mLocationPermission = false;

    private CircleImageView civProfilePic;
    private TextView tvNoTelp, tvNamaMurid, tvAlamatMurid, tvMapel, tvJenjang, tvStatus;
    private Button btnTerima, btnTolak;
    private GoogleMap gMap;
    private LinearLayout llCommandRow, llBtnNoTelp, llBtnNavigation;

    private static final String TAG = "DetailPemesananActivity";
    private static final int DEFAULT_MAP_ZOOM = 10;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pemesanan);

        init();

        int idPemesanan = currIntent.getIntExtra("idPemesanan", 0);
        callPemesananById(idPemesanan);
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        View v = super.onCreateView(parent, name, context, attrs);
        return v;
    }

    private void init(){
        Log.d(TAG, "init: called");
        userHelper = new UserHelper(this);

        currIntent = getIntent();

        civProfilePic = findViewById(R.id.civProfilePic);
        llBtnNoTelp = findViewById(R.id.llBtnNoTelp);
        llBtnNavigation = findViewById(R.id.llBtnNavigation);

        tvNoTelp = findViewById(R.id.tvNoTelp);
        tvNamaMurid = findViewById(R.id.tvNamaMurid);
        tvAlamatMurid = findViewById(R.id.tvAlamatMurid);
        tvMapel = findViewById(R.id.tvMapel);
        tvJenjang = findViewById(R.id.tvJenjang);
        tvStatus = findViewById(R.id.tvStatus);

        llCommandRow = findViewById(R.id.llCommandRow);
        btnTerima = findViewById(R.id.btnTerimaPemesanan);
        btnTolak = findViewById(R.id.btnTolakPemesanan);

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

    private void getDevicelocation() {
        Log.d(TAG, "getDevicelocation: called");
        FusedLocationProviderClient mflfc = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (mLocationPermission) {
                Task location = mflfc.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: task: success");
                            Location currentLocation = (Location) task.getResult();

                        }
                        else {
                            Log.d(TAG, "onComplete: Failed");
                        }
                    }
                });

            }
        }catch (SecurityException e){
            Log.d(TAG, "getDevicelocation: "+e.getMessage());
            e.printStackTrace();
        }

    }

    private void askLocationPermission(){
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this.getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            mLocationPermission = true;
        }else{
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermission = false;
        switch (requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:
                for(int i = 0; i < grantResults.length; i++) {
                    if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                        mLocationPermission = false;
                        return;
                    }
                }
                mLocationPermission = true;
                break;
        }
    }

    private void callPemesananById(int id){
        Log.d(TAG, "callPemesananById: called");
        Call<Pemesanan> call = apiInterface.getPemesananById(id);
        ProgressDialog p = rci.getProgressDialog(this, "Menampilkan pemesanan");
        p.show();
        call.enqueue(new Callback<Pemesanan>() {
            @Override
            public void onResponse(Call<Pemesanan> call, Response<Pemesanan> response) {
                p.dismiss();
                Log.d(TAG, "onResponse: "+response.message());
                if(!response.isSuccessful()){
                    return;
                }

                currPemesanan = response.body();
                retrievePemesanan(currPemesanan);
            }

            @Override
            public void onFailure(Call<Pemesanan> call, Throwable t) {
                p.dismiss();
                Log.d(TAG, "onFailure: "+t.getMessage());
                return;
            }
        });
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

    private void retrievePemesanan(Pemesanan pemesanan){
        Log.d(TAG, "retrievePemesanan: called");
        User murid = pemesanan.getMurid();

        //Menampilkan alamat pada Google Map
        Alamat currAlamat = murid.getAlamat();
        LatLng muridLocation = new LatLng(currAlamat.getLatitude(), currAlamat.getLongitude());

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

        //Menampilkan mata pelajaran dan jenjang
        MataPelajaran mapel = pemesanan.getMataPelajaran();
        tvMapel.setText(mapel.getNamaMapel());
        tvJenjang.setText(mapel.getJenjang().getNamaJenjang());

        //Menghilangkan tombol terima dan tolak
        llCommandRow.setVisibility(View.GONE);

        //Menghilangkan tombol telp dan navigasi
        llBtnNavigation.setVisibility(View.GONE);
        llBtnNoTelp.setVisibility(View.GONE);

        //Menentukan status
        int statusInt = pemesanan.getStatus();
        String statusStr = "";
        switch (statusInt){
            case 0:
                statusStr = "Pemesanan belum diterima";
                llCommandRow.setVisibility(View.VISIBLE);
                llBtnNavigation.setVisibility(View.INVISIBLE);
                llBtnNoTelp.setVisibility(View.INVISIBLE);
                break;
            case 1:
                statusStr = "Pemesanan telah diterima";
                llBtnNavigation.setVisibility(View.VISIBLE);
                llBtnNoTelp.setVisibility(View.VISIBLE);
                break;
            case 2:
                statusStr = "Pemesanan telah ditolak";
                break;
            case 3:
                statusStr = "Pemesanan telah selesai";
                break;
            default:
                statusStr = "Hmm... sepertinya ada yang salah";
                break;
        }

        //Menampilkan pesan status
        tvStatus.setText(statusStr);

        //Menerima pemesanan
        btnTerima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callUpdatePemesanan(1);
            }
        });

        //Menolak pemesanan
        btnTolak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callUpdatePemesanan(2);
            }
        });
    }

    //Menolak atau menerima pemesanan
    private void callUpdatePemesanan(int status){
        Log.d(TAG, "callUpdatePemesanan: called");
        ProgressDialog p = rci.getProgressDialog(this);
        Call<Pemesanan> call = apiInterface.updatePemesanan(
                currPemesanan.getIdPemesanan(),
                status
        );
        p.show();
        call.enqueue(new Callback<Pemesanan>() {
            @Override
            public void onResponse(Call<Pemesanan> call, Response<Pemesanan> response) {
                p.dismiss();
                Log.d(TAG, "onResponse: "+response.message());
                if(!response.isSuccessful()){
                    return;
                }

                String toastStr = "";

                if(status == 1){
                    toastStr = "Yeay! Pemesanan telah berhasil diterima!";
                }else if(status == 2){
                    toastStr = "Pemesanan berhasil ditolak.";
                }
                Toast.makeText(DetailPemesananActivity.this, toastStr, Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onFailure(Call<Pemesanan> call, Throwable t) {
                p.dismiss();
                String toastStr = "Hmmm, sepertinya ada yang salah";
                Toast.makeText(DetailPemesananActivity.this, toastStr, Toast.LENGTH_LONG).show();
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
}
