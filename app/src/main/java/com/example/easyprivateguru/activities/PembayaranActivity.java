package com.example.easyprivateguru.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.easyprivateguru.adapters.PembayaranRVAdapter;
import com.example.easyprivateguru.R;
import com.example.easyprivateguru.api.ApiInterface;
import com.example.easyprivateguru.api.RetrofitClientInstance;
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme;
import com.midtrans.sdk.corekit.models.CustomerDetails;
import com.midtrans.sdk.corekit.models.ItemDetails;
import com.midtrans.sdk.corekit.models.snap.Authentication;
import com.midtrans.sdk.corekit.models.snap.CreditCard;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.uikit.SdkUIFlowBuilder;

import java.util.ArrayList;

public class PembayaranActivity extends AppCompatActivity {
    private RecyclerView rvPembayaran;
    private Button btnBayar;
    private RetrofitClientInstance rci = new RetrofitClientInstance();
    private ApiInterface apiInterface = rci.getApiInterface();

    private final static String BASE_URL = RetrofitClientInstance.BASE_URL + "api/midtrans/";
    private final static String CLIENT_KEY = "SB-Mid-client-JFggDG5pgc2T9Z7c";

    private static final String TAG = "PembayaranActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembayaran);

        init();
        btnBayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickPay();
            }
        });

        makePayment();
    }

    private void init(){
        rvPembayaran = findViewById(R.id.rvPembayaran);
        btnBayar = findViewById(R.id.btnBayar);
    }

    private void makePayment(){
        SdkUIFlowBuilder.init()
                .setContext(this)
                .setMerchantBaseUrl(BASE_URL)
                .setClientKey(CLIENT_KEY)
                .setTransactionFinishedCallback(new TransactionFinishedCallback() {
                    @Override
                    public void onTransactionFinished(TransactionResult transactionResult) {
                        String message = "Hmmm...";
                        switch (transactionResult.getStatus()){
                            case TransactionResult.STATUS_SUCCESS:
                                message = "Transaksi sukses";
                                break;
                            case TransactionResult.STATUS_FAILED:
                                message = "Transaksi gagal";
                                break;
                            case TransactionResult.STATUS_INVALID:
                                message = "Transaksi invalid";
                                break;
                            case TransactionResult.STATUS_PENDING:
                                message = "Transaksi pending";
                                break;
                        }
                        Log.d(TAG, "onTransactionFinished: ");
                        Toast.makeText(PembayaranActivity.this, transactionResult.getStatusMessage(), Toast.LENGTH_LONG).show();
                    }
                })
                .enableLog(true)
                .setColorTheme(new CustomColorTheme("#777777","#f77474" , "#3f0d0d"))
                .buildSDK();
    }

    private void clickPay(){
        MidtransSDK midtransSDK = MidtransSDK.getInstance();
        midtransSDK.setTransactionRequest(transactionRequest("101",2000, 1, "John"));
        midtransSDK.startPaymentUiFlow(PembayaranActivity.this );
    }

    public static TransactionRequest transactionRequest(String id, int price, int qty, String name){
        TransactionRequest request =  new TransactionRequest(System.currentTimeMillis() + " " , 2000 );
        request.setCustomerDetails(customerDetails());
        ItemDetails details = new ItemDetails(id, price, qty, name);

        ArrayList<ItemDetails> itemDetails = new ArrayList<>();
        itemDetails.add(details);
        request.setItemDetails(itemDetails);
        CreditCard creditCard = new CreditCard();
        creditCard.setSaveCard(false);
        creditCard.setAuthentication(Authentication.AUTH_RBA);

        request.setCreditCard(creditCard);
        return request;
    }

    public static CustomerDetails customerDetails(){
        CustomerDetails cd = new CustomerDetails();
        cd.setFirstName("Nama depan");
        cd.setLastName("belakang");
        cd.setEmail("email@gmail.com");
        cd.setPhone("0123456789");
        return cd;
    }

}
