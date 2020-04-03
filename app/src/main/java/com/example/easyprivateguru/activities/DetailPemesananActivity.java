package com.example.easyprivateguru.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.easyprivateguru.R;
import com.example.easyprivateguru.UserHelper;
import com.example.easyprivateguru.api.ApiInterface;
import com.example.easyprivateguru.api.RetrofitClientInstance;
import com.example.easyprivateguru.fragments.MapFragment;
import com.example.easyprivateguru.models.Alamat;
import com.example.easyprivateguru.models.MataPelajaran;
import com.example.easyprivateguru.models.Pemesanan;
import com.example.easyprivateguru.models.User;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailPemesananActivity extends AppCompatActivity {
    private RetrofitClientInstance rci = new RetrofitClientInstance();
    private ApiInterface apiInterface = rci.getApiInterface();
    private UserHelper userHelper;

    private CircleImageView civProfilePic;
    private TextView tvNamaMurid, tvAlamatMurid, tvMapel, tvJenjang, tvStatus;
    private Button btnTerima, btnTolak;
    private FrameLayout flMaps;

    private Intent currIntent;
    private static final String TAG = "DetailPemesananActivity";

    private Pemesanan currPemesanan;

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

        tvNamaMurid = findViewById(R.id.tvNamaMurid);
        tvAlamatMurid = findViewById(R.id.tvAlamatMurid);
        tvMapel = findViewById(R.id.tvMapel);
        tvJenjang = findViewById(R.id.tvJenjang);
        tvStatus = findViewById(R.id.tvStatus);

        btnTerima = findViewById(R.id.btnTerimaPemesanan);
        btnTolak = findViewById(R.id.btnTolakPemesanan);

        flMaps = findViewById(R.id.flMaps);
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

                //Menampilkan maps dengan alamatnya
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flMaps, new MapFragment(currPemesanan.getMurid().getAlamat().get(0)), "mapFrag")
                        .commit();
            }

            @Override
            public void onFailure(Call<Pemesanan> call, Throwable t) {
                p.dismiss();
                Log.d(TAG, "onFailure: "+t.getMessage());
                return;
            }
        });
    }

    private void retrievePemesanan(Pemesanan pemesanan){
        Log.d(TAG, "retrievePemesanan: called");
        User murid = pemesanan.getMurid();
        Picasso.get()
                .load(murid.getAvatar())
                .placeholder(R.drawable.account_default)
                .error(R.drawable.account_default)
                .noFade()
                .into(civProfilePic);
        tvNamaMurid.setText(murid.getName());
        tvAlamatMurid.setText(murid.getAlamat().get(0).getAlamatLengkap());

        MataPelajaran mapel = pemesanan.getMataPelajaran();
        tvMapel.setText(mapel.getNamaMapel());
        tvJenjang.setText(mapel.getJenjang().getNamaJenjang());

        int statusInt = pemesanan.getStatus();
        String statusStr = "";
        switch (statusInt){
            case 0:
                statusStr = "Pemesanan belum diterima";
                break;
            case 1:
                statusStr = "Pemesanan telah diterima";
                break;
            case 2:
                statusStr = "Pemesanan telah ditolak";
                break;
            case 3:
                statusStr = "Pemesanan telah selsai";
                break;
            default:
                statusStr = "Hmm... sepertinya ada yang salah";
                break;
        }

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

        flMaps.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                ViewGroup.MarginLayoutParams flParams = (ViewGroup.MarginLayoutParams) flMaps.getLayoutParams();
                flParams.height = 300;

                ViewGroup.MarginLayoutParams civParams = (ViewGroup.MarginLayoutParams) civProfilePic.getLayoutParams();
                civParams.topMargin = 250;
            }
        });
    }

    private void callUpdatePemesanan(int status){
        Log.d(TAG, "callUpdatePemesanan: called");
        ProgressDialog p = rci.getProgressDialog(this);
        Call<Pemesanan> call = apiInterface.updatePemesanan(currPemesanan.getIdPemesanan(), status);
        p.show();
        call.enqueue(new Callback<Pemesanan>() {
            @Override
            public void onResponse(Call<Pemesanan> call, Response<Pemesanan> response) {
                p.dismiss();
                Log.d(TAG, "onResponse: "+response.message());
                if(!response.isSuccessful()){
                    return;
                }

                finish();
            }

            @Override
            public void onFailure(Call<Pemesanan> call, Throwable t) {
                p.dismiss();
                Log.d(TAG, "onFailure: "+t.getMessage());
                return;
            }
        });
    }
}
