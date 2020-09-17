package com.easyprivate.easyprivateguru.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easyprivate.easyprivateguru.CustomUtility;
import com.easyprivate.easyprivateguru.R;
import com.easyprivate.easyprivateguru.UserHelper;
import com.easyprivate.easyprivateguru.api.ApiInterface;
import com.easyprivate.easyprivateguru.api.RetrofitClientInstance;
import com.easyprivate.easyprivateguru.models.JadwalAvailable;
import com.easyprivate.easyprivateguru.models.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditJadwalActivity extends AppCompatActivity {
    private ArrayList<JadwalAvailable> jadwalAvailableArrayList = new ArrayList<>();
    private ArrayList<JadwalAvailable> editedJadwalAvailableArrayList = new ArrayList<>();
    private RetrofitClientInstance rci = new RetrofitClientInstance();
    private ApiInterface apiInterface = rci.getApiInterface();

    private User currUser;
    private UserHelper userHelper;
    private CustomUtility customUtility;

    private LinearLayout llMain;
//    private GridLayout glJadwalAvailable;
    private Button btnSubmit;

    private static final String TAG = "EditJadwalActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_jadwal);
        init();

        callJadwalAvailable();
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callUpdateJadwalAvailable(getIdJadwalByAvailability(1), getIdJadwalByAvailability(0));
            }
        });
    }

    private void init(){
        userHelper = new UserHelper(this);
        currUser = userHelper.retrieveUser();
        customUtility = new CustomUtility(this);

        llMain = findViewById(R.id.llMain);
        btnSubmit = findViewById(R.id.btnSimpan);
//        glJadwalAvailable = findViewById(R.id.glJadwalAvailable);
//        glJadwalAvailable.setColumnCount(3);
    }

    private void callJadwalAvailable(){
        Call<ArrayList<JadwalAvailable>> call = apiInterface.getJadwalAvailable(currUser.getId(), null, null, null, null);
        ProgressDialog progressDialog = rci.getProgressDialog(this, "Menampilkan jadwal Anda");
        progressDialog.show();
        call.enqueue(new Callback<ArrayList<JadwalAvailable>>() {
            @Override
            public void onResponse(Call<ArrayList<JadwalAvailable>> call, Response<ArrayList<JadwalAvailable>> response) {
                Log.d(TAG, "onResponse: "+response.message());
                progressDialog.dismiss();
                if(!response.isSuccessful()){
                    return;
                }

                jadwalAvailableArrayList = response.body();
                retrieveJadwalAvailable();
            }

            @Override
            public void onFailure(Call<ArrayList<JadwalAvailable>> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
                t.printStackTrace();
                progressDialog.show();
            }
        });
    }

    private void retrieveJadwalAvailable(){
        ArrayList<JadwalAvailable> jadwalAvailablePerHari = new ArrayList<>();
        for(int i = 0; i < jadwalAvailableArrayList.size(); i++){
            JadwalAvailable ja = jadwalAvailableArrayList.get(i);
            if(i > 0){
                JadwalAvailable jx = jadwalAvailableArrayList.get(i-1);
                if(!jx.getHari().equals(ja.getHari())){
                    attachJadwalAvailable(jadwalAvailablePerHari);
                    jadwalAvailablePerHari.clear();
                }
            }
            jadwalAvailablePerHari.add(ja);
            //Khusus hari minggu
            if(i == jadwalAvailableArrayList.size() - 1){
                attachJadwalAvailable(jadwalAvailablePerHari);
            }
        }
    }

    private void attachJadwalAvailable(ArrayList<JadwalAvailable> jaArrayList){
        //Deklarasi textview untuk diisi hari
        TextView tvHari = new TextView(this);
        tvHari.setText(jaArrayList.get(0).getHari());
        float textSize = (float) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics());
        tvHari.setTextSize(20);
//        tvHari.setTextSize(getResources().getDimension(R.dimen.paragraph));
        tvHari.setTypeface(Typeface.DEFAULT_BOLD);
        llMain.addView(tvHari);

        //Deklarasi Horizontal ScrollView
        HorizontalScrollView.LayoutParams hsvParams = new HorizontalScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        HorizontalScrollView horizontalScrollView = new HorizontalScrollView(this);
        horizontalScrollView.setLayoutParams(hsvParams);

        //Deklarasi Linear Layout
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(llParams);

        //Deklarasi GridLayout
        GridLayout gridLayout = new GridLayout(this);
        gridLayout.setColumnCount(3);
//        gridLayout.setUseDefaultMargins(true);

        //Memasukkan masing-masing jadwalAvailable ke dalam linearLayout
        for(int i = 0; i < jaArrayList.size(); i++){
            JadwalAvailable ja = jaArrayList.get(i);
            //Memasukkan item_card_time.xml ke dalam view
            View v = LayoutInflater.from(this).inflate(R.layout.item_card_time, null);

            //Mendeklarasikan llItemCard yang ada di dalam item_card_time.xml
            LinearLayout llItemCard = v.findViewById(R.id.llItemCard);

            //Mendeklarasikan tvJam yang ada di dalam item_card_time.xml
            TextView tvJam = v.findViewById(R.id.tvJam);
            String startStr = customUtility.reformatDateTime(ja.getStart(), "HH:mm:ss", "HH:mm");
            String endStr = customUtility.reformatDateTime(ja.getEnd(), "HH:mm:ss", "HH:mm");;
            tvJam.setText(startStr + " - " + endStr);

            switch (ja.getAvailable()){
                case 0:
                    tvJam.setTextColor(getResources().getColor(R.color.blue));
                    llItemCard.setBackgroundResource(R.drawable.background_time_card_white);
                    break;
                case 1:
                case 2:
                    tvJam.setTextColor(getResources().getColor(R.color.fontLight));
                    llItemCard.setBackgroundResource(R.drawable.background_time_card_blue);
                    break;
            }

            llItemCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editedJadwalAvailableArrayList.add(ja);
                    Log.d(TAG, "onClick: ja.id: "+ja.getIdJadwalAvailable());
                    Log.d(TAG, "onClick: available: "+ja.getAvailable());
                    switch (ja.getChangedAvailability()){
                        case 0:
                            ja.setChangedAvailability(1);
                            tvJam.setTextColor(getResources().getColor(R.color.fontLight));
                            llItemCard.setBackgroundResource(R.drawable.background_time_card_blue);
                            break;
                        case 1:
                        case 2:
                            ja.setChangedAvailability(0);
                            tvJam.setTextColor(getResources().getColor(R.color.blue));
                            llItemCard.setBackgroundResource(R.drawable.background_time_card_white);
                            break;
                        case 99:
                            if(ja.getAvailable() == 0){
                                ja.setChangedAvailability(1);
                                tvJam.setTextColor(getResources().getColor(R.color.fontLight));
                                llItemCard.setBackgroundResource(R.drawable.background_time_card_blue);
                            }else{
                                ja.setChangedAvailability(0);
                                tvJam.setTextColor(getResources().getColor(R.color.blue));
                                llItemCard.setBackgroundResource(R.drawable.background_time_card_white);
                            }
                            break;
                    }
                }
            });

            //Mengatur margin
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            int marginDimen = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics());
            params.setMargins(marginDimen, marginDimen, marginDimen, marginDimen);

            llItemCard.setLayoutParams(params);
            llItemCard.setTag("llHariJam"+ja.getIdJadwalAvailable());
            Log.d(TAG, "attachJadwalAvailable: llItemCard.getTag: "+llItemCard.getTag());

//            linearLayout.addView(v);
            gridLayout.addView(v);
        }

        //Memasukkan linearLayout ke dalam horizontalScrolllView
//        horizontalScrollView.addView(linearLayout);

        //Memasukkan horizontal Scrollview ke dalam llMain
//        llMain.addView(horizontalScrollView);

        llMain.addView(gridLayout);
    }

    private ArrayList<Integer> getIdJadwalByAvailability(int available){
        ArrayList<Integer> idJadwal = new ArrayList<>();
        for(int i = 0; i < editedJadwalAvailableArrayList.size(); i++){
            JadwalAvailable ja = editedJadwalAvailableArrayList.get(i);
            if(ja.getAvailable() != ja.getChangedAvailability() && ja.getChangedAvailability() != 99){
                if(ja.getChangedAvailability() == available){
                    idJadwal.add(ja.getIdJadwalAvailable());
                }
            }
        }
        return idJadwal;
    }

    private void callUpdateJadwalAvailable(ArrayList<Integer> idAvailableArrayList, ArrayList<Integer> idNonAvailableArrayList){
        Call<Void> call = apiInterface.updateJadwalAvailable(idAvailableArrayList, idNonAvailableArrayList, null);
        ProgressDialog progressDialog = rci.getProgressDialog(this, "Menyimpan perubahan jadwal Anda");
        progressDialog.show();
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d(TAG, "onResponse: "+response.message());
                if(!response.isSuccessful()){
                    return;
                }

                Toast.makeText(EditJadwalActivity.this, "Jadwal Anda berhasil diubah", Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
                t.printStackTrace();
                Toast.makeText(EditJadwalActivity.this, "Hmm... sepertinya ada yang salah", Toast.LENGTH_LONG).show();
            }
        });
    }
}
