package com.example.easyprivateguru;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.easyprivateguru.api.ApiInterface;
import com.example.easyprivateguru.api.RetrofitClientInstance;
import com.example.easyprivateguru.models.User;
import com.google.gson.Gson;

public class UserHelper {
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
        User u = gson.fromJson(json, User.class);
        return u;
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
