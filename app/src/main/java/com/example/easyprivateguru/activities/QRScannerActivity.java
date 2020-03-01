package com.example.easyprivateguru.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRScannerActivity extends AppCompatActivity {
    ZXingScannerView qrScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
}
