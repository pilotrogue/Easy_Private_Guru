package com.example.easyprivateguru.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easyprivateguru.CustomUtility;
import com.example.easyprivateguru.R;
import com.example.easyprivateguru.UserHelper;
import com.example.easyprivateguru.api.ApiInterface;
import com.example.easyprivateguru.api.RetrofitClientInstance;
import com.example.easyprivateguru.models.Alamat;
import com.example.easyprivateguru.models.JadwalAvailable;
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
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailPemesananActivity extends AppCompatActivity{
    private RetrofitClientInstance rci = new RetrofitClientInstance();
    private ApiInterface apiInterface = rci.getApiInterface();
    private UserHelper userHelper;
    private CustomUtility customUtility;
    private Intent currIntent;
    private Pemesanan currPemesanan;
    private Integer conflictCount = 0;
    private boolean mLocationPermission = false;

    private CircleImageView civProfilePic;
    private TextView tvNoTelp, tvNamaMurid, tvAlamatMurid, tvMapel, tvJenjang, tvStatus, tvAlert;
    private Button btnTerima, btnTolak;
    private GoogleMap gMap;
    private LinearLayout llCommandRow, llBtnNoTelp, llBtnNavigation, llHariJam, llWarning;
    private RecyclerView rvHariJam;

    private static final String DIRECTION_URI_STR = "https://www.google.com/maps/dir/?api=1&destination=";

    private static final String TAG = "DetailPemesananActivity";
    private static final int DEFAULT_MAP_ZOOM = 13;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final String ALERT_MESSAGE = "pemesanan dengan jadwal serupa";

    private boolean hasBeenPaused = false;
    public static Activity detailPemesananActivity;

    @Override
    protected void onResume() {
        super.onResume();
        if(hasBeenPaused){
            hasBeenPaused = false;
            callCountConflictedPemesanan();
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
        setContentView(R.layout.activity_detail_pemesanan);

        detailPemesananActivity = this;
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
        customUtility = new CustomUtility(this);

        currIntent = getIntent();

        civProfilePic = findViewById(R.id.civProfilePic);
        llBtnNoTelp = findViewById(R.id.llBtnNoTelp);
        llBtnNavigation = findViewById(R.id.llBtnNavigation);

        rvHariJam = findViewById(R.id.rvHariJam);

        tvNoTelp = findViewById(R.id.tvNoTelp);
        tvNamaMurid = findViewById(R.id.tvNamaMurid);
        tvAlamatMurid = findViewById(R.id.tvAlamatMurid);
        tvMapel = findViewById(R.id.tvMapel);
        tvJenjang = findViewById(R.id.tvJenjang);
        tvStatus = findViewById(R.id.tvStatus);

        llHariJam = findViewById(R.id.llHariJam);

        llWarning = findViewById(R.id.llWarning);
        tvAlert = findViewById(R.id.tvAlert);

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
        if(murid.getAvatar() != null){
            customUtility.putIntoImage(murid.getAvatar(), civProfilePic);
        }

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

        //Menampilkan jadwal
        ArrayList<JadwalAvailable> jadwalAvailable = new ArrayList<>();
        for(int i = 0; i < pemesanan.getJadwalPemesananPerminggu().size(); i++){
            JadwalAvailable ja = pemesanan.getJadwalPemesananPerminggu().get(i).getJadwalAvailable();
            jadwalAvailable.add(ja);
            attachHariJam(ja);
        }

//        HariJamRVAdapter hariJamRVAdapter = new HariJamRVAdapter(this, jadwalAvailable);
//        rvHariJam.setAdapter(hariJamRVAdapter);
//        rvHariJam.setLayoutManager(new LinearLayoutManager(this));

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
                showDialogTerima();
            }
        });

        //Menolak pemesanan
        btnTolak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogTolak();
            }
        });

        callCountConflictedPemesanan();
    }

    private void attachHariJam(JadwalAvailable ja){
        View v = LayoutInflater.from(this).inflate(R.layout.item_card_schedule, llHariJam, false);

        TextView tvHari, tvJam;

        tvHari = v.findViewById(R.id.tvHari);
        tvJam = v.findViewById(R.id.tvJam);

        tvHari.setText(ja.getHari());

        String startStr = customUtility.reformatDateTime(ja.getStart(), "HH:mm:ss", "HH:mm");
        String endStr = customUtility.reformatDateTime(ja.getEnd(), "HH:mm:ss", "HH:mm");
        String jamStr = startStr + " - " + endStr;
        tvJam.setText(jamStr);

        llHariJam.addView(v);
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
                    ArrayList<Integer> jadwalAvailableArrayList = new ArrayList<>();
                    for(int i = 0; i < currPemesanan.getJadwalPemesananPerminggu().size(); i++){
                        jadwalAvailableArrayList.add(currPemesanan.getJadwalPemesananPerminggu().get(i).getJadwalAvailable().getIdJadwalAvailable());
                    }
                    callUpdateJadwalAvailable(jadwalAvailableArrayList);
                }else if(status == 2){
                    toastStr = "Pemesanan berhasil ditolak.";
                    Toast.makeText(DetailPemesananActivity.this, toastStr, Toast.LENGTH_LONG).show();
                    finish();
                }
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

    //Get konflik pemesanan
    private void callCountConflictedPemesanan(){
        Call<Integer> call = apiInterface.getCountConflictedPemesanan(currPemesanan.getIdPemesanan());
        ProgressDialog progressDialog = rci.getProgressDialog(this);
        progressDialog.show();
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                Log.d(TAG, "onResponse: "+response.message());
                progressDialog.dismiss();
                if(!response.isSuccessful()){
                    return;
                }

                conflictCount = response.body();
                if(conflictCount > 0){
                    llWarning.setVisibility(View.VISIBLE);

                    SpannableString spanString = new SpannableString(conflictCount + " " +ALERT_MESSAGE);

                    //Underline
                    spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);

                    //Bold
                    spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);

                    tvAlert.setText(spanString);

                    llWarning.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(DetailPemesananActivity.this, ConflictedPemesananActivity.class);
                            i.putExtra("idPemesanan", currPemesanan.getIdPemesanan());
                            startActivity(i);
                        }
                    });
                }else{
                    llWarning.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
                t.printStackTrace();
                progressDialog.dismiss();
            }
        });
    }

    private void callUpdateJadwalAvailable(ArrayList<Integer> idTerisi){
        Call<Void> call = apiInterface.updateJadwalAvailable(null, null, idTerisi);
        ProgressDialog progressDialog = rci.getProgressDialog(this);
        progressDialog.show();
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d(TAG, "onResponse: "+response.message());

                if(!response.isSuccessful()){
                    return;
                }

                String toastStr = "Hore! Pemesanan telah berhasil diterima!";
                Toast.makeText(DetailPemesananActivity.this, toastStr, Toast.LENGTH_LONG).show();
                finish();

                konfirmasiIntent();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
                t.printStackTrace();
            }
        });
    }

    private void konfirmasiIntent(){
        Intent i = new Intent(this, KonfirmasiJadwalActivity.class);
        i.putExtra("idPemesanan", currPemesanan.getIdPemesanan());
        startActivity(i);
    }

    private void showDialogTerima(){
        String dialogMessage = "";

        if(conflictCount > 0){
            dialogMessage = "Menerima pemesanan ini akan membatalkan "+conflictCount+" pemesanan dengan jadwal serupa.";
        }else{
            dialogMessage = "Apakah Anda yakin ingin menerima pemesanan?";
        }

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Terima pemesanan?");
        alertDialog.setMessage(dialogMessage);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Terima", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callUpdatePemesanan(1);
            }
        });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Kembali", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void showDialogTolak(){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Tolak pemesanan?");
        alertDialog.setMessage("Pemesanan yang ditolak tidak bisa diterima kembali");
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Tolak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callUpdatePemesanan(2);
            }
        });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Kembali", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void openPhone(String phoneNumber){
        Intent i = new Intent(Intent.ACTION_DIAL);
        i.setData(Uri.parse("tel:"+phoneNumber));

        if(i.resolveActivity(getPackageManager()) != null){
            startActivity(i);
        }
    }

    private void openDirections(LatLng latLng){
        String directionUriStr = DIRECTION_URI_STR + latLng.latitude + "," + latLng.longitude;
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(directionUriStr));
        startActivity(i);
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
}
