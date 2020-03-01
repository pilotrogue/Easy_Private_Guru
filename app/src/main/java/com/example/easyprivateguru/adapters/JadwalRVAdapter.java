package com.example.easyprivateguru.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyprivateguru.models.Absen;
import com.example.easyprivateguru.models.Pesanan;
import com.example.easyprivateguru.models.User;
import com.example.easyprivateguru.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class JadwalRVAdapter extends RecyclerView.Adapter<JadwalRVAdapter.ViewHolder>{
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

    public JadwalRVAdapter(Context mContext, ArrayList<Absen> absens) {
        this.mContext = mContext;
        this.absens = absens;
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
        Pesanan p = a.getPesanan();
        User guru = p.getGuru(), murid = p.getMurid();

        SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy");
        String tglPertemuanStr = sdf.format(a.getTanggalPertemuan());

        holder.subtitle1.setText(tglPertemuanStr);
        holder.title.setText(murid.getNamaUser());
        holder.subtitle2.setText(p.getMataPelajaran().getNamaMataPelajaran());
    }

    @Override
    public int getItemCount() {
        return absens.size();
    }
}
