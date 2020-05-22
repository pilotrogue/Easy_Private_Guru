package com.example.easyprivateguru.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easyprivateguru.CustomUtility;
import com.example.easyprivateguru.R;
import com.example.easyprivateguru.adapters.ConflictedPemesananRVAdapter;
import com.example.easyprivateguru.api.ApiInterface;
import com.example.easyprivateguru.api.RetrofitClientInstance;
import com.example.easyprivateguru.models.Alamat;
import com.example.easyprivateguru.models.JadwalAvailable;
import com.example.easyprivateguru.models.MataPelajaran;
import com.example.easyprivateguru.models.Pemesanan;
import com.google.android.gms.common.api.ApiException;

import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ConflictedPemesananActivity extends AppCompatActivity {
    private RetrofitClientInstance rci = new RetrofitClientInstance();
    private ApiInterface apiInterface = rci.getApiInterface();

    private RecyclerView rvPemesananKonflik;
    private LinearLayout llDetail;
    private CircleImageView civPic;
    private TextView tvNamaMurid, tvMapel, tvAlamatMurid;
    private Button btnTerima;

    private ArrayList<Pemesanan> conflictedPemesananArrayList = new ArrayList<>();

    private Pemesanan currPemesanan;
    private static final String TAG = "ConflictedPemesananActi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conflicted_pemesanan);

        init();
//        attachJadwalAvailable();
        callCurrentPemesanan();
        callConflictedPemesanan();
    }

    private void init(){
        rvPemesananKonflik = findViewById(R.id.rvPesananKonflik);
        llDetail = findViewById(R.id.llDetail);

        civPic = findViewById(R.id.civPic);

        btnTerima = findViewById(R.id.btnTerima);
        tvNamaMurid = findViewById(R.id.tvNamaMurid);
        tvMapel = findViewById(R.id.tvMapel);
        tvAlamatMurid = findViewById(R.id.tvAlamatMurid);
    }

    private void retrieveCurrPemesanan(){
        if(currPemesanan.getJadwalPemesananPerminggu() == null){
            return;
        }

        tvNamaMurid.setText(currPemesanan.getMurid().getName());

        CustomUtility customUtility = new CustomUtility(this);
        customUtility.putIntoImage(currPemesanan.getMurid().getAvatar(), civPic);

        Alamat currAlamat = currPemesanan.getMurid().getAlamat();
        Address address = customUtility.getAddress(currAlamat.getLatitude(), currAlamat.getLongitude());

        String alamatStr = "";
        if(address == null){
            alamatStr = currAlamat.getAlamatLengkap();
        }else{
            alamatStr = address.getSubLocality()+", "+address.getLocality()+", "+address.getSubAdminArea()+", "+address.getAdminArea()+", "+address.getCountryName();
        }
        tvAlamatMurid.setText(alamatStr);

        MataPelajaran mataPelajaran = currPemesanan.getMataPelajaran();
        tvMapel.setText(mataPelajaran.getNamaMapel() + ", " +"Kelas "+currPemesanan.getKelas());

        Log.d(TAG, "retrieveCurrPemesanan: jadwalPemesananPerminggu.size: "+currPemesanan.getJadwalPemesananPerminggu().size());

        for(int i = 0; i < currPemesanan.getJadwalPemesananPerminggu().size(); i++){
            JadwalAvailable ja = currPemesanan.getJadwalPemesananPerminggu().get(i).getJadwalAvailable();
            Log.d(TAG, "attachJadwalAvailable: jadwalAvailable: "+ja.getHari()+", "+ja.getStart()+" - "+ja.getEnd());

            View v = LayoutInflater.from(this).inflate(R.layout.item_card_schedule, llDetail, false);

            TextView hariTv, jamTv;
            hariTv = v.findViewById(R.id.tvHari);
            jamTv = v.findViewById(R.id.tvJam);

            hariTv.setText(ja.getHari());

            String start, end;
            start = customUtility.reformatDateTime(ja.getStart(), "HH:mm:ss", "HH:mm");
            end = customUtility.reformatDateTime(ja.getEnd(), "HH:mm:ss", "HH:mm");

            jamTv.setText(start + " - " + end);

            llDetail.addView(v);
        }
        btnTerima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogTerima();
            }
        });
    }

    private void callCurrentPemesanan(){
        Call<Pemesanan> call = apiInterface.getPemesananById(getIntent().getIntExtra("idPemesanan", 0));
        ProgressDialog progressDialog = rci.getProgressDialog(this, "Menampilkan pemesanan");
        progressDialog.show();
        call.enqueue(new Callback<Pemesanan>() {
            @Override
            public void onResponse(Call<Pemesanan> call, Response<Pemesanan> response) {
                progressDialog.dismiss();
                if(!response.isSuccessful()){
                    return;
                }

                currPemesanan = response.body();
                retrieveCurrPemesanan();
            }

            @Override
            public void onFailure(Call<Pemesanan> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
                t.printStackTrace();
                progressDialog.dismiss();
            }
        });
    }

    private void konfirmasiIntent(){
        Intent i = new Intent(this, KonfirmasiJadwalActivity.class);
        i.putExtra("idPemesanan", currPemesanan.getIdPemesanan());
        startActivity(i);
    }

    private void callConflictedPemesanan(){
        Call<ArrayList<Pemesanan>> call = apiInterface.getConflictedPemesanan(getIntent().getIntExtra("idPemesanan", 0));
        ProgressDialog progressDialog = rci.getProgressDialog(this, "Menampilkan pemesanan");
        progressDialog.show();
        call.enqueue(new Callback<ArrayList<Pemesanan>>() {
            @Override
            public void onResponse(Call<ArrayList<Pemesanan>> call, Response<ArrayList<Pemesanan>> response) {
                Log.d(TAG, "onResponse: "+response.message());
                progressDialog.dismiss();
                if(!response.isSuccessful()){
                    return;
                }

                conflictedPemesananArrayList = response.body();
                retrieveConflictedPemesanan();
            }

            @Override
            public void onFailure(Call<ArrayList<Pemesanan>> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
                t.printStackTrace();
                progressDialog.dismiss();
            }
        });
    }

    private void showDialogTerima(){
        String dialogMessage = "Menerima pemesanan ini akan membatalkan pemesanan dengan jadwal serupa.";
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

    private void callUpdatePemesanan(int status){
        Call<Pemesanan> call = apiInterface.updatePemesanan(currPemesanan.getIdPemesanan(), status);

        String message = "";

        if(status == 1){
            message = "Menerima pemesanan...";
        }else if (status == 2){
            message = "Menolak pemesanan...";
        }

        ProgressDialog progressDialog = rci.getProgressDialog(this, message);
        progressDialog.show();

        call.enqueue(new Callback<Pemesanan>() {
            @Override
            public void onResponse(Call<Pemesanan> call, Response<Pemesanan> response) {
                Log.d(TAG, "onResponse: "+response.message());
                progressDialog.dismiss();

                if (!response.isSuccessful()){
                    return;
                }

                String successMessage = "";

                if(status == 1){
                    successMessage = "Pemesanan berhasil diterima";
                }else if (status == 2){
                    successMessage = "Pemesanan berhasil ditolak";
                }

                Toast.makeText(ConflictedPemesananActivity.this, successMessage, Toast.LENGTH_LONG).show();
                DetailPemesananActivity.detailPemesananActivity.finish();
                finish();
                konfirmasiIntent();
            }

            @Override
            public void onFailure(Call<Pemesanan> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ConflictedPemesananActivity.this, "Hmmm... sepertinya ada yang salah", Toast.LENGTH_LONG).show();
                Log.d(TAG, "onFailure: "+t.getMessage());
                t.printStackTrace();

            }
        });
    }

    private void retrieveConflictedPemesanan(){
        ConflictedPemesananRVAdapter adapter = new ConflictedPemesananRVAdapter(this, conflictedPemesananArrayList);
        adapter.addActivities(this);
        adapter.addActivities(DetailPemesananActivity.detailPemesananActivity);
        rvPemesananKonflik.setAdapter(adapter);
        rvPemesananKonflik.setLayoutManager(new LinearLayoutManager(this));
    }
}
