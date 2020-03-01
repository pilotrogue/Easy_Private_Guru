package com.example.easyprivateguru.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.easyprivateguru.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public class QRCodeActivity extends AppCompatActivity {

    ImageView ivQRCode;
    EditText etQRString;
    Button btnGenerateQR;
    int qrSize = 0;

    private static final String TAG = "QRCodeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        init();

        btnGenerateQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = etQRString.getText().toString();
                Bitmap bm = encodeBitmap(str);
                ivQRCode.setImageBitmap(bm);
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        qrSize = ivQRCode.getWidth();
        Log.d(TAG, "init: qrSize:" + qrSize);
    }

    private void init(){
        ivQRCode = findViewById(R.id.ivQRCode);
        etQRString = findViewById(R.id.etQrString);
        btnGenerateQR = findViewById(R.id.btnGenerate);
    }

    private Bitmap encodeBitmap(String s){
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(s, BarcodeFormat.QR_CODE, qrSize, qrSize, null);
        }catch(WriterException e){
            Log.d(TAG, "encodeBitmap: "+e.getMessage());
            return null;
        }

        int w = bitMatrix.getWidth(), h = bitMatrix.getHeight();
        int[] pixels = new int[w*h];

        for(int y = 0; y<h; y++){
            int offset = y*w;
            for (int x = 0; x<w; x++){
                pixels[offset + x] = bitMatrix.get(x, y)? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, qrSize, 0,0,w,h);

        return bitmap;
    }
}
