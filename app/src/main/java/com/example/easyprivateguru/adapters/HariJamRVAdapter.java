package com.example.easyprivateguru.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyprivateguru.CustomUtility;
import com.example.easyprivateguru.R;
import com.example.easyprivateguru.models.JadwalAvailable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.zip.Inflater;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class HariJamRVAdapter extends RecyclerView.Adapter<HariJamRVAdapter.ViewHolder>{
    private Context mContext;
    private ArrayList<JadwalAvailable> jadwalAvailableArrayList = new ArrayList<>();

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvHari, tvJam;

        public ViewHolder(@NonNull View iv) {
            super(iv);
            tvHari = iv.findViewById(R.id.tvHari);
            tvJam = iv.findViewById(R.id.tvJam);
        }
    }

    public HariJamRVAdapter(Context mContext, ArrayList<JadwalAvailable> jadwalAvailableArrayList) {
        this.mContext = mContext;
        this.jadwalAvailableArrayList = jadwalAvailableArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.schedule_item_card, parent, false);
        return new HariJamRVAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JadwalAvailable ja = jadwalAvailableArrayList.get(position);

        holder.tvHari.setText(ja.getHari());

        CustomUtility cu = new CustomUtility(mContext);

        //Reformat jam
        String jamStartStr = cu.reformatDateTime(ja.getStart(), "HH:mm:ss", "HH:mm");
        String jamEndStr = cu.reformatDateTime(ja.getEnd(), "HH:mm:ss", "HH:mm");

        //Menggabungkan jam start dengan jam end
        String jamStr = jamStartStr + " s/d " + jamEndStr;

        holder.tvJam.setText(jamStr);
    }

    @Override
    public int getItemCount() {
        return jadwalAvailableArrayList.size();
    }
}
