package com.example.easyprivateguru.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyprivateguru.R;
import com.example.easyprivateguru.models.JadwalAjar;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class JadwalRVAdapter extends RecyclerView.Adapter<JadwalRVAdapter.ViewHolder>{
    private Context mContext;
    private ArrayList<JadwalAjar> jadwalAjars = new ArrayList<>();

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView subtitle1, title, subtitle2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            subtitle1 = itemView.findViewById(R.id.tvSubtitle1);
            title = itemView.findViewById(R.id.tvTitle);
            subtitle2 = itemView.findViewById(R.id.tvSubtitle2);
        }
    }

    public JadwalRVAdapter(Context mContext, ArrayList<JadwalAjar> jadwalAjars) {
        this.mContext = mContext;
        this.jadwalAjars = jadwalAjars;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JadwalAjar j = jadwalAjars.get(position);
        Log.d(TAG, "onBindViewHolder: eventId: "+j.getIdJadwalAjar());

        holder.subtitle1.setText(j.getWaktuAjar());
        holder.title.setText(j.getPemesanan().getMurid().getName());
        holder.subtitle2.setText(j.getPemesanan().getMataPelajaran().getNamaMapel());
    }

    @Override
    public int getItemCount() {
        return jadwalAjars.size();
    }
}
