package com.example.easyprivateguru.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyprivateguru.models.MataPelajaran;
import com.example.easyprivateguru.models.Pesanan;
import com.example.easyprivateguru.models.User;
import com.example.easyprivateguru.R;

import java.util.ArrayList;

public class PesananRVAdapter extends RecyclerView.Adapter<PesananRVAdapter.ViewHolder>{
    private Context mContext;
    private ArrayList<Pesanan> pesanans;

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView subtitle1, title, subtitle2;
        private ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            subtitle1 = itemView.findViewById(R.id.tvSubtitle1);
            title = itemView.findViewById(R.id.tvTitle);
            subtitle2 = itemView.findViewById(R.id.tvSubtitle2);
            image = itemView.findViewById(R.id.ivPic);
        }
    }

    public PesananRVAdapter(Context mContext, ArrayList<Pesanan> pesanans) {
        this.mContext = mContext;
        this.pesanans = pesanans;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_card, parent, false);
        return new PesananRVAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pesanan pesanan = pesanans.get(position);
        User murid = pesanan.getMurid();
        MataPelajaran mataPelajaran = pesanan.getMataPelajaran();

        holder.subtitle1.setText(murid.getAlamat());
        holder.title.setText(murid.getNamaUser());
        holder.subtitle2.setText(mataPelajaran.getNamaMataPelajaran());
    }

    @Override
    public int getItemCount() {
        return pesanans.size();
    }
}
