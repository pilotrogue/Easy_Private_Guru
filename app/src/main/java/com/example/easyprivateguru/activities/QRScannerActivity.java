package com.example.easyprivateguru.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.example.easyprivateguru.CustomUtility;
import com.example.easyprivateguru.R;
import com.example.easyprivateguru.UserHelper;
import com.example.easyprivateguru.api.ApiInterface;
import com.example.easyprivateguru.api.RetrofitClientInstance;
import com.example.easyprivateguru.models.Absen;
import com.example.easyprivateguru.models.JadwalPemesananPerminggu;
import com.example.easyprivateguru.models.Pemesanan;
import com.example.easyprivateguru.models.TanggalPengganti;
import com.example.easyprivateguru.models.User;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.zip.Inflater;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QRScannerActivity extends AppCompatActivity {
    private ZXingScannerView qrScanner;
    private RetrofitClientInstance rci = new RetrofitClientInstance();
    private ApiInterface apiInterface = rci.getApiInterface();

    private UserHelper userHelper;
    private User currUser;
    private CustomUtility customUtility;

    private static final String TAG = "QRScannerActivity";

    private boolean pemesananIsVerified = false;
    private Pemesanan currPemesanan, calledPemesanan;
    private ArrayList<TanggalPengganti> tanggalPengganti = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        qrScanner = new ZXingScannerView(this);
        setContentView(qrScanner);
    }

    @Override
    protected void onResume() {
        super.onResume();
        qrScanner.setResultHandler(new ZXingScannerView.ResultHandler() {
            @Override
            public void handleResult(Result result) {
                String resultStr = result.getText();
                retrievePemesanan(resultStr);
//                onBackPressed();
            }
        });
        qrScanner.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        qrScanner.stopCamera();
    }

    private void init(){
        userHelper = new UserHelper(this);
        customUtility = new CustomUtility(this);
        currUser = userHelper.retrieveUser();
    }

    private void retrievePemesanan(String pemesananJsonStr){
        currPemesanan = customUtility.jsonToPemesanan(pemesananJsonStr);
        Log.d(TAG, "retrievePemesanan: pemesananJsonStr: "+pemesananJsonStr);

        //Jika qr code valid
        if(currPemesanan!=null){
            Log.d(TAG, "retrievePemesanan: id_pemesanan: "+currPemesanan.getIdPemesanan());
            pemesananIsVerified = verifyPemesanan(currPemesanan);
            if(pemesananIsVerified){
                //pemesanan verified
                callPemesananFiltered(currPemesanan);
            }else{
               //pemesanan non verified
                Toast.makeText(this, "Hmmm, Anda memindai QR code yang salah", Toast.LENGTH_LONG).show();
            }
        }

        //Jika qr code tidak valid
        else{
            Log.d(TAG, "retrievePemesanan: QR code invalid");
            Toast.makeText(this, "Waduh, QR Code tidak valid", Toast.LENGTH_LONG).show();
            showDialogPemesananNotFound();
        }
    }

    private boolean verifyPemesanan(Pemesanan pem){
        if(pem.getStatus() == 1){
            if(currPemesanan.getIdGuru() == currUser.getId()){
                return true;
            }
        }
        return false;
    }

    private void callPemesananFiltered(Pemesanan p){
        Call<ArrayList<Pemesanan>> call = apiInterface.getPemesananFiltered(p.getIdPemesanan(), p.getIdGuru(), p.getIdMurid(), p.getStatus());
        ProgressDialog progressDialog = rci.getProgressDialog(this);
        progressDialog.show();
        call.enqueue(new Callback<ArrayList<Pemesanan>>() {
            @Override
            public void onResponse(Call<ArrayList<Pemesanan>> call, Response<ArrayList<Pemesanan>> response) {
                Log.d(TAG, "callPemesananFiltered: onResponse: "+response.message());
                progressDialog.dismiss();
                if(!response.isSuccessful()){
                    return;
                }
                Log.d(TAG, "onResponse: response.body().size(): "+response.body().size());
                if(response.body().size() > 0){
                    calledPemesanan = response.body().get(0);
                    JadwalPemesananPerminggu currJadwalPemesananPerminggu = verifyJadwalPemesananPerminggu(calledPemesanan.getJadwalPemesananPerminggu());
                    if(currJadwalPemesananPerminggu != null){
                        Log.d(TAG, "onResponse: currJadwalPemesananPerminggu is not null");
                        Log.d(TAG, "onResponse: idJadwalPemesananPerminggu: "+currJadwalPemesananPerminggu.getIdJadwalPemesananPerminggu());
                        callStoreAbsen(currPemesanan.getIdPemesanan(), currJadwalPemesananPerminggu.getIdJadwalPemesananPerminggu(), null);
                    }else if(currJadwalPemesananPerminggu == null){
                        Log.d(TAG, "onResponse: currJadwalPemesananPerminggu is null");
                        callTanggalPengganti();
//                        callStoreAbsen(currPemesanan.getIdPemesanan(), null);
                    }
                }else{
//                    callStoreAbsen(currPemesanan.getIdPemesanan(), null);
                    showDialogPemesananNotFound();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Pemesanan>> call, Throwable t) {
                Log.d(TAG, "callPemesananFiltered: onFailure: "+t.getMessage());
                progressDialog.dismiss();
                t.printStackTrace();
            }
        });
    }

    private void showDialogConfirm(){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Hore!");
        alertDialog.setMessage("Murid Anda dengan nama "+calledPemesanan.getMurid().getName()+ " telah berhasil diabsen");
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Sip!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                onBackPressed();
            }
        });
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
                onBackPressed();
            }
        });
        alertDialog.show();
    }

    private void showDialogJadwalTidakBisaDiganti(){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Jadwal pengganti");
        alertDialog.setMessage("Untuk saat ini, Anda tidak bisa melakukan absen jadwal pengganti.");
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Kembali", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                onBackPressed();
            }
        });
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
                onBackPressed();
            }
        });
        alertDialog.show();
    }

    private void showDialogFailed(){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Hmmm... ada absen yang sama");
        alertDialog.setMessage("Murid Anda "+calledPemesanan.getMurid().getName()+ " sudah absen hari ini. \nAbsen hanya bisa dilakukan sehari sekali");
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Kembali", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                onBackPressed();
            }
        });
//        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Coba lagi", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                reset();
//            }
//        });
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
                onBackPressed();
            }
        });
        alertDialog.show();
    }

    private void showDialogPemesananNotFound(){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Hmmm... ada yang salah");
        alertDialog.setMessage("Sepertinya QR Code yang Anda pindai tidak sesuai");
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Kembali", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                onBackPressed();
            }
        });
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Coba lagi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                reset();
            }
        });
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
                onBackPressed();
            }
        });
        alertDialog.show();
    }

    private void showDialogJadwalPengganti(){
        AlertDialog alertDialog = new AlertDialog.Builder(QRScannerActivity.this).create();
        alertDialog.setCancelable(true);
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
                onBackPressed();
            }
        });

        alertDialog.setTitle("Jadwal pengganti");

        View v = LayoutInflater.from(this).inflate(R.layout.dialog_tanggal_pengganti, null);
        Spinner spinTanggalPengganti = v.findViewById(R.id.spinTanggalPengganti);

        ArrayList<String> tanggalArray = new ArrayList<>();

        for(int i = 0; i < tanggalPengganti.size(); i++){
            tanggalArray.add(customUtility.reformatDateTime(tanggalPengganti.get(i).getTanggalPengganti(), "yyyy-MM-dd", "EEEE, dd MMMM yyyy"));
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tanggalArray);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinTanggalPengganti.setAdapter(arrayAdapter);

        alertDialog.setView(v);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Simpan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TanggalPengganti selectedItem = tanggalPengganti.get(spinTanggalPengganti.getSelectedItemPosition());
                String tanggalPenggantiStr = selectedItem.getTanggalPengganti();
                Log.d(TAG, "onClick: tanggalPenggantiStr: "+tanggalPenggantiStr);

                callStoreAbsen(currPemesanan.getIdPemesanan(), selectedItem.getIdJadwalPerminggu(), tanggalPenggantiStr);
            }
        });

        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Batalkan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                onBackPressed();
            }
        });
        alertDialog.show();
    }

    private void reset(){
        Intent i = getIntent();
        finish();
        startActivity(i);
    }

    private void callStoreAbsen(Integer idPemesanan, Integer idJadwalPemesananPerminggu, String tanggalPengganti){
        Log.d(TAG, "callStoreAbsen: id_pemesanan: "+idPemesanan);
        Log.d(TAG, "callStoreAbsen: idJadwalPemesananPerminggu: "+idJadwalPemesananPerminggu);
        Log.d(TAG, "callStoreAbsen: tanggalPengganti: "+tanggalPengganti);
        Call<Integer> call = apiInterface.createAbsen(idPemesanan, idJadwalPemesananPerminggu, tanggalPengganti);
        ProgressDialog progressDialog = rci.getProgressDialog(this);
        progressDialog.show();
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                Log.d(TAG, "callStoreAbsen: onResponse: "+response.message());
                progressDialog.dismiss();
                if(!response.isSuccessful()){
                    return;
                }
                Integer statusResponse = response.body();
                if(statusResponse == 1){
                    showDialogConfirm();
                }else if(statusResponse == 0){
                    showDialogFailed();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.d(TAG, "callStoreAbsen: onFailure: "+t.getMessage());
                progressDialog.dismiss();
                t.printStackTrace();
            }
        });
    }

    private void callTanggalPengganti(){
        ProgressDialog progressDialog = rci.getProgressDialog(this);
        progressDialog.show();
        Call<ArrayList<TanggalPengganti>> call = apiInterface.getTanggalPengganti(currPemesanan.getIdPemesanan());
        call.enqueue(new Callback<ArrayList<TanggalPengganti>>() {
            @Override
            public void onResponse(Call<ArrayList<TanggalPengganti>> call, Response<ArrayList<TanggalPengganti>> response) {
                Log.d(TAG, "onResponse: "+response.message());
                progressDialog.dismiss();
                if(!response.isSuccessful()){
                    return;
                }

                tanggalPengganti = response.body();
                if(tanggalPengganti.size() > 0){
                    showDialogJadwalPengganti();
                }else{
                    showDialogJadwalTidakBisaDiganti();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<TanggalPengganti>> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
                progressDialog.dismiss();
                t.printStackTrace();
            }
        });
    }

    private JadwalPemesananPerminggu verifyJadwalPemesananPerminggu(ArrayList<JadwalPemesananPerminggu> jpp){
        Calendar myCalendar = Calendar.getInstance();
        String currHari = customUtility.hariIntToString(myCalendar.get(Calendar.DAY_OF_WEEK));
        for(int i = 0; i < jpp.size(); i++){
            JadwalPemesananPerminggu jp = jpp.get(i);
            if(currHari.equals(jp.getJadwalAvailable().getHari().toLowerCase())){
                return jp;
            }
        }
        return null;
    }

}
