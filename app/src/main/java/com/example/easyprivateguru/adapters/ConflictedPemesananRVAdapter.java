package com.example.easyprivateguru.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.MessagePattern;
import android.location.Address;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyprivateguru.CustomUtility;
import com.example.easyprivateguru.R;
import com.example.easyprivateguru.activities.DetailPemesananActivity;
import com.example.easyprivateguru.api.ApiInterface;
import com.example.easyprivateguru.api.RetrofitClientInstance;
import com.example.easyprivateguru.models.Alamat;
import com.example.easyprivateguru.models.JadwalAvailable;
import com.example.easyprivateguru.models.JadwalPemesananPerminggu;
import com.example.easyprivateguru.models.MataPelajaran;
import com.example.easyprivateguru.models.Pemesanan;
import com.example.easyprivateguru.models.User;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ConflictedPemesananRVAdapter extends RecyclerView.Adapter<ConflictedPemesananRVAdapter.ViewHolder>{
    private Context mContext;
    private ArrayList<Pemesanan> pemesananArrayList;
    private ArrayList<Activity> activities = new ArrayList<>();

    public class ViewHolder extends RecyclerView.ViewHolder{
        private RelativeLayout rvCardItem;
        private LinearLayout llDetail, llCommandRow;
        private Button btnTerima, btnTolak;

        private TextView title, subtitle1, subtitle2, subtitle3;
        private CircleImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rvCardItem = itemView.findViewById(R.id.rvItem);
            image = itemView.findViewById(R.id.civPic);
            title = itemView.findViewById(R.id.tvTitle);
            subtitle1 = itemView.findViewById(R.id.tvSubtitle1);
            subtitle2 = itemView.findViewById(R.id.tvSubtitle2);
            subtitle3 = itemView.findViewById(R.id.tvSubtitle3);

            btnTerima = itemView.findViewById(R.id.btnTerima);
            llCommandRow = itemView.findViewById(R.id.llCommandRow);
            btnTolak = itemView.findViewById(R.id.btnTolak);
            llDetail = itemView.findViewById(R.id.llDetail);
        }
    }

    public ArrayList<Activity> getActivities() {
        return activities;
    }

    public void addActivities(Activity activity) {
        this.activities.add(activity);
    }

    public ConflictedPemesananRVAdapter(Context mContext, ArrayList<Pemesanan> pemesananArrayList) {
        this.mContext = mContext;
        this.pemesananArrayList = pemesananArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_card_primary, parent, false);
        return new ConflictedPemesananRVAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pemesanan p = pemesananArrayList.get(position);
        User murid = p.getMurid();
        MataPelajaran mataPelajaran = p.getMataPelajaran();

        CustomUtility customUtility = new CustomUtility(mContext);

        if(murid.getAvatar() != null){
            customUtility.putIntoImage(murid.getAvatar(), holder.image);
        }

        holder.title.setText(murid.getName());

        Alamat currAlamat = murid.getAlamat();
        Address address = customUtility.getAddress(currAlamat.getLatitude(), currAlamat.getLongitude());

        String alamatStr = "";
        if(address == null){
            alamatStr = currAlamat.getAlamatLengkap();
        }else{
            alamatStr = address.getLocality()+", "+address.getSubAdminArea();
        }

        holder.subtitle1.setText(mataPelajaran.getNamaMapel() + ", " +"Kelas "+p.getKelas());
        holder.subtitle2.setText(alamatStr);
        holder.llCommandRow.setVisibility(View.VISIBLE);

        holder.btnTolak.setVisibility(View.GONE);
        holder.btnTolak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showDialogTolak(p, holder.rvCardItem, position);
            }
        });

        holder.btnTerima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogTerima(p, holder.rvCardItem, position);
            }
        });

//        holder.rvCardItem.setVisibility(View.GONE);
        holder.rvCardItem.setOnClickListener(null);

        List<JadwalPemesananPerminggu> jpp = p.getJadwalPemesananPerminggu();

        for(int i = 0; i < jpp.size(); i++){
            JadwalAvailable ja = jpp.get(i).getJadwalAvailable();
            View v = LayoutInflater.from(mContext).inflate(R.layout.item_card_schedule, holder.llDetail, false);

            //Deklarasi hari dan jam pada R.layout.item_card_schedule
            TextView hariTv, jamTv;
            hariTv = v.findViewById(R.id.tvHari);
            jamTv = v.findViewById(R.id.tvJam);

            hariTv.setText(ja.getHari());
//            hariTv.setBackgroundColor(mContext.getResources().getColor(R.color.yellow));

            String start, end;
            start = customUtility.reformatDateTime(ja.getStart(), "HH:mm:ss", "HH:mm");
            end = customUtility.reformatDateTime(ja.getEnd(), "HH:mm:ss", "HH:mm");

            jamTv.setText(start + " - " + end);

            holder.llDetail.addView(v);
        }
    }

    @Override
    public int getItemCount() {
        return pemesananArrayList.size();
    }

    private void updatePemesanan(Pemesanan pem, View view, int position, int status){
        RetrofitClientInstance rci = new RetrofitClientInstance();
        ApiInterface apiInterface = rci.getApiInterface();
        Call<Pemesanan> call = apiInterface.updatePemesanan(pem.getIdPemesanan(), status);

        String message = "";

        if(status == 1){
            message = "Menerima pemesanan...";
        }else if (status == 2){
            message = "Menolak pemesanan...";
        }

        ProgressDialog progressDialog = rci.getProgressDialog(mContext, message);
        progressDialog.show();

        call.enqueue(new Callback<Pemesanan>() {
            @Override
            public void onResponse(Call<Pemesanan> call, Response<Pemesanan> response) {
                Log.d(TAG, "onResponse: "+response.message());
                progressDialog.dismiss();

                if (!response.isSuccessful()){
                    return;
                }

                view.setVisibility(View.GONE);
                pemesananArrayList.remove(position);

                String successMessage = "";

                if(status == 1){
                    successMessage = "Pemesanan berhasil diterima";
                }else if (status == 2){
                    successMessage = "Pemesanan berhasil ditolak";
                }

                Toast.makeText(mContext, successMessage, Toast.LENGTH_LONG).show();

                for(int i = 0; i < activities.size(); i++){
                    Activity currActivity = activities.get(i);
                    currActivity.finish();
                }
            }

            @Override
            public void onFailure(Call<Pemesanan> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(mContext, "Hmmm... sepertinya ada yang salah", Toast.LENGTH_LONG).show();
                Log.d(TAG, "onFailure: "+t.getMessage());
                t.printStackTrace();

            }
        });
    }

    private void showDialogTolak(Pemesanan pem, View view, int position){
        String dialogMessage = "Apakah Anda yakin ingin menolak pemesanan?";
        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
        alertDialog.setTitle("Terima pemesanan");
        alertDialog.setMessage(dialogMessage);
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Tolak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updatePemesanan(pem, view, position, 2);
            }
        });
        alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Kembali", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void showDialogTerima(Pemesanan pem, View view, int position){
        String dialogMessage = "Menerima pemesanan ini akan membatalkan pemesanan dengan jadwal serupa.";
        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
        alertDialog.setTitle("Terima pemesanan?");
        alertDialog.setMessage(dialogMessage);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Terima", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updatePemesanan(pem, view, position, 1);
            }
        });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Kembali", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }
}