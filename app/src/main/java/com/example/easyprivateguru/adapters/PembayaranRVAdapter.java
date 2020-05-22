package com.example.easyprivateguru.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyprivateguru.models.Pembayaran;
import com.example.easyprivateguru.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PembayaranRVAdapter extends RecyclerView.Adapter<PembayaranRVAdapter.ViewHolder>{
    private Context mContext;
    private ArrayList<Pembayaran> pembayarans = new ArrayList<>();

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView subtitle1, title, subtitle2;
        private CircleImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            subtitle1 = itemView.findViewById(R.id.tvSubtitle1);
            title = itemView.findViewById(R.id.tvTitle);
            subtitle2 = itemView.findViewById(R.id.tvSubtitle2);
            image = itemView.findViewById(R.id.civPic);
        }
    }

    public PembayaranRVAdapter(Context mContext, ArrayList<Pembayaran> pembayarans) {
        this.mContext = mContext;
        this.pembayarans = pembayarans;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_card_primary, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pembayaran p = pembayarans.get(position);

        SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy");
        String tanggalUpahStr = sdf.format(p.getTanggalUpah());

        holder.subtitle1.setText(tanggalUpahStr);

        if(p.isStatusDone()){
            holder.title.setText("Lunas");
        }else{
            holder.title.setText("Belum lunas");
        }

        holder.subtitle2.setText("Rp " + String.valueOf(p.getJumlahUpah()));
        holder.image.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return pembayarans.size();
    }
}
