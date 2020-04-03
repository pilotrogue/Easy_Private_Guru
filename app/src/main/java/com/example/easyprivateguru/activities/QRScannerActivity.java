package com.example.easyprivateguru.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.easyprivateguru.UserHelper;
import com.example.easyprivateguru.api.ApiInterface;
import com.example.easyprivateguru.api.RetrofitClientInstance;
import com.example.easyprivateguru.models.Absen;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRScannerActivity extends AppCompatActivity {
    private ZXingScannerView qrScanner;
    private RetrofitClientInstance rci = new RetrofitClientInstance();
    private ApiInterface apiInterface = rci.getApiInterface();

    private UserHelper userHelper;

    private static final String TAG = "QRScannerActivity";

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
                Toast.makeText(QRScannerActivity.this, resultStr, Toast.LENGTH_LONG).show();
                retrieveAbsen(resultStr);
                onBackPressed();
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
    }

    private void retrieveAbsen(String absenJsonStr){
        Absen currAbsen = userHelper.jsonToAbsen(absenJsonStr);
        //Jika qr code valid
        if(currAbsen!=null){
            Log.d(TAG, "retrieveAbsen: id_absen: "+currAbsen.getIdAbsen());
        }
        //Jika qr code tidak valid
        else{
            Log.d(TAG, "retrieveAbsen: QR code invalid");
        }
    }
}
