package com.example.easyprivateguru;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.easyprivateguru.api.ApiInterface;
import com.example.easyprivateguru.api.RetrofitClientInstance;
import com.example.easyprivateguru.models.Absen;
import com.example.easyprivateguru.models.Pemesanan;
import com.example.easyprivateguru.models.User;
import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class UserHelper {
    private static final String TAG = "UserHelper";
    private Context mContext;
    private SharedPreferences preferences;

    private RetrofitClientInstance rci = new RetrofitClientInstance();
    private ApiInterface apiInterface = rci.getApiInterface();

    private static final String TAG_USER = "currUser";
    private static final String TAG_PREF = "currPref";

    public UserHelper(Context mContext) {
        this.mContext = mContext;
        this.preferences = mContext.getSharedPreferences(TAG_PREF, Context.MODE_PRIVATE);
    }

    public void contoh(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("currPref", Context.MODE_PRIVATE);

        //Store
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("testStr", "ini adalah value");
        editor.putInt("testInt", 99);

        //Retrieve
        String hasilRetrieve = sharedPreferences.getString("testStr", "cadangan");
        Integer hasilRetrieveInt = sharedPreferences.getInt("testInt", 0);

        //Convert object ke bentuk json(string)
        Pemesanan pemesananBaru = new Pemesanan();
        String jsonStr;

        //Menyimpan hasil convert
        jsonStr = new Gson().toJson(pemesananBaru);
        editor.putString("pemesananBaru", jsonStr);

        //Convert json(string) ke bentuk object
        Gson gson = new Gson();
        String pemesananJsonStr = sharedPreferences.getString("pemesananBaru", "");

        try{
            Pemesanan pemesananHasilConvert = gson.fromJson(pemesananJsonStr, Pemesanan.class);
        }catch (Throwable t){
            t.printStackTrace();
        }
    }

    public void storeUser(User u){
        String jsonStr = new Gson().toJson(u);

        SharedPreferences.Editor editor = preferences.edit();

        removeUser();
        editor.putString(TAG_USER, jsonStr);
        editor.commit();
    }

    public User retrieveUser(){
        Gson gson = new Gson();
        String json = preferences.getString(TAG_USER, "");
        try {
            User u = gson.fromJson(json, User.class);
            return u;
        }catch (Throwable t){
            Log.d(TAG, "retrieveUser: "+t.getMessage());
            return null;
        }
    }

    public void removeUser(){
        if (retrieveUser() != null){
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove(TAG_USER);
            editor.commit();
        }
    }

    public static String getUserTag() {
        return TAG_USER;
    }

    public static String getPrefTag() {
        return TAG_PREF;
    }
}
