package com.easyprivate.easyprivateguru;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.easyprivate.easyprivateguru.api.ApiInterface;
import com.easyprivate.easyprivateguru.api.RetrofitClientInstance;
import com.easyprivate.easyprivateguru.models.Absen;
import com.easyprivate.easyprivateguru.models.Pemesanan;
import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomUtility {
    private Context mContext;
    private Geocoder mGeocoder;
    private RetrofitClientInstance rci = new RetrofitClientInstance();
    private ApiInterface apiInterface = rci.getApiInterface();
    private static final String TAG = "CustomUtility";

    public CustomUtility(Context mContext) {
        this.mContext = mContext;
        this.mGeocoder = new Geocoder(mContext, Locale.getDefault());
    }

    public Address getAddress(Double lat, Double lon){
        List<Address> addressList = null;
        try {
            addressList = mGeocoder.getFromLocation(lat, lon, 1);
            if (addressList != null && addressList.size() > 0){
                return addressList.get(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String reformatDateTime(String dateTimeStr, String beforePattern, String afterPattern){
        SimpleDateFormat sdf = new SimpleDateFormat(beforePattern);
        try{
            Date date = sdf.parse(dateTimeStr);

            sdf.applyPattern(afterPattern);
            String reformatedStr = sdf.format(date);

            return reformatedStr;
        }catch (ParseException e){
            Log.d(TAG, "reformatDateTime: "+e.getMessage());
            return "";
        }
    }

    public Absen jsonToAbsen(String absenJsonStr){
        Gson gson = new Gson();
        try {
            Absen absen = gson.fromJson(absenJsonStr, Absen.class);
            return absen;
        }catch (Throwable t){
            Log.d(TAG, "jsonToAbsen: "+t.getMessage());
            return null;
        }
    }

    public Pemesanan jsonToPemesanan(String pemesananJsonStr){
        Gson gson = new Gson();
        try {
            Pemesanan pemesanan = gson.fromJson(pemesananJsonStr, Pemesanan.class);
            return pemesanan;
        }catch (Throwable t){
            Log.d(TAG, "jsonToPemesanan: "+t.getMessage());
            return null;
        }
    }

    public void putIntoImage(String avatarStr, CircleImageView civ){
        if(avatarStr == null || avatarStr.equals("")){
            Log.d(TAG, "putIntoImage: avatar is not available");
            civ.setImageResource(R.drawable.account_default);
            return;
        }
        Picasso.get()
                .load(avatarStr)
                .placeholder(R.drawable.account_default)
                .error(R.drawable.account_default)
                .noFade()
                .into(civ, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                        String avatarStrAlt = modifyAvatarStr(avatarStr);
                        putIntoImageAlt(avatarStrAlt, civ);
                    }
                });
    }

    public void putIntoImageAlt(String avatarStr, CircleImageView civ){
        Picasso.get()
                .load(avatarStr)
                .placeholder(R.drawable.account_default)
                .error(R.drawable.account_default)
                .noFade()
                .into(civ);
    }

    public String modifyAvatarStr(String avatarStr){
        avatarStr = RetrofitClientInstance.BASE_URL + "assets/avatars/" + avatarStr;
        return avatarStr;
    }

    public String getCountTimeString(String timeStr, String pattern){
        String timeCountStr = getCountTimeString(reformatDateTime(timeStr, pattern, "yyyy-MM-dd HH:mm:ss"));
        return timeCountStr;
    }

    public String getCountTimeString(String timeStr){
        String yearStart = reformatDateTime(timeStr, "yyyy-MM-dd HH:mm:ss", "yyyy");
        String monthStart = reformatDateTime(timeStr, "yyyy-MM-dd HH:mm:ss", "MM");
        String dayStart = reformatDateTime(timeStr, "yyyy-MM-dd HH:mm:ss", "dd");
        String hourStart = reformatDateTime(timeStr, "yyyy-MM-dd HH:mm:ss", "HH");
        String minuteStart = reformatDateTime(timeStr, "yyyy-MM-dd HH:mm:ss", "mm");
        String secondStart = reformatDateTime(timeStr, "yyyy-MM-dd HH:mm:ss", "ss");

        Log.d(TAG, "getCountTimeString: monthStart: "+monthStart);

        //Get calendar first meet
        Calendar currCalendar = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(yearStart));
        calendar.set(Calendar.MONTH, Integer.parseInt(monthStart) - 1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dayStart));
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hourStart));
        calendar.set(Calendar.MINUTE, Integer.parseInt(minuteStart));
        calendar.set(Calendar.SECOND, Integer.parseInt(secondStart));

        long currDateLong = currCalendar.getTimeInMillis();
        long dateLong = calendar.getTimeInMillis();

        long difference = currDateLong - dateLong;
        String timeCountStr = "";

        long seconds = difference/1000;
        timeCountStr = seconds + " detik yang lalu";
        if(seconds > 60){
            long minutes = seconds/60;
            timeCountStr = minutes + " menit yang lalu";
            if(minutes > 60){
                long hours = minutes/60;
                timeCountStr = hours + " jam yang lalu";
                if(hours > 24){
                    long days = hours/24;
                    timeCountStr = days + " hari yang lalu";
                    if(days > 30){
                        long months = days/30;
                        timeCountStr = months + " bulan yang lalu";
                        if(months > 12){
                            long years = months/12;
                            timeCountStr = days + " tahun yang lalu";
                        }
                    }
                }
            }
        }

        return timeCountStr;
    }

    public String hariIntToString(Integer hariInt){
        String hariStr = "";
        switch (hariInt){
            case 1:
                hariStr = "minggu";
                break;
            case 2:
                hariStr = "senin";
                break;
            case 3:
                hariStr = "selasa";
                break;
            case 4:
                hariStr = "rabu";
                break;
            case 5:
                hariStr = "kamis";
                break;
            case 6:
                hariStr = "jumat";
                break;
            case 7:
                hariStr = "sabtu";
                break;
            default:
                hariStr = "";
                break;
        }
        return hariStr;
    }
}
