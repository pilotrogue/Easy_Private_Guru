package com.easyprivate.easyprivateguru;

import android.content.Context;

import com.easyprivate.easyprivateguru.models.User;
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;

public class MidtransHelper {
    private Context mContext;
    private User currUser;
    private MidtransSDK midtransSDK = MidtransSDK.getInstance();
    public static final String CLIENT_KEY = "";

    public MidtransHelper(Context mContext) {
        this.mContext = mContext;
        this.currUser = new UserHelper(mContext).retrieveUser();
        this.midtransSDK.setClientKey(CLIENT_KEY);
    }

    public void pay(String snapToken){
        midtransSDK.setTransactionFinishedCallback(new TransactionFinishedCallback() {
            @Override
            public void onTransactionFinished(TransactionResult transactionResult) {
                if(transactionResult != null){
                    showDialog(transactionResult);
                }
            }
        });
        midtransSDK.startPaymentUiFlow(mContext, snapToken);
    }

    private void showDialog(TransactionResult tr){
        String title, message;
        switch (tr.getStatus()){
            case (TransactionResult.STATUS_SUCCESS):
                break;
            case (TransactionResult.STATUS_PENDING):
                break;
            case (TransactionResult.STATUS_INVALID):
                break;
            case (TransactionResult.STATUS_FAILED):
                break;
            default:
                break;
        }
    }
}
