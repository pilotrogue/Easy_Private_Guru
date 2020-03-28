package com.example.easyprivateguru.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyprivateguru.models.Absen;
import com.example.easyprivateguru.models.Pemesanan;
import com.example.easyprivateguru.models.User;
import com.example.easyprivateguru.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class AbsenRVAdapter extends RecyclerView.Adapter<AbsenRVAdapter.ViewHolder>{
    private Context mContext;
    private ArrayList<Absen> absens = new ArrayList<>();

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView subtitle1, title, subtitle2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            subtitle1 = itemView.findViewById(R.id.tvSubtitle1);
            title = itemView.findViewById(R.id.tvTitle);
            subtitle2 = itemView.findViewById(R.id.tvSubtitle2);
        }
    }

    public AbsenRVAdapter(Context mContext, ArrayList<Absen> absens) {
        this.mContext = mContext;
        this.absens = absens;
        Log.d(TAG, "AbsenRVAdapter: absens.size: "+absens.size());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Absen a = absens.get(position);
        Pemesanan p = a.getPemesanan();
        User guru = p.getGuru();
        User murid = p.getMurid();

        holder.subtitle1.setText(a.getWaktuAbsen());
        holder.title.setText(murid.getName());
        holder.subtitle2.setText(p.getMataPelajaran().getNamaMapel());
    }

    @Override
    public int getItemCount() {
        return absens.size();
    }
}
