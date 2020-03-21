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
import com.example.easyprivateguru.models.Jadwal;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class JadwalRVAdapter extends RecyclerView.Adapter<JadwalRVAdapter.ViewHolder>{
    private Context mContext;
    private ArrayList<Jadwal> jadwals = new ArrayList<>();

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView subtitle1, title, subtitle2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            subtitle1 = itemView.findViewById(R.id.tvSubtitle1);
            title = itemView.findViewById(R.id.tvTitle);
            subtitle2 = itemView.findViewById(R.id.tvSubtitle2);
        }
    }

    public JadwalRVAdapter(Context mContext, ArrayList<Jadwal> jadwals) {
        this.mContext = mContext;
        this.jadwals = jadwals;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Jadwal j = jadwals.get(position);
        Log.d(TAG, "onBindViewHolder: eventId: "+j.getEventId());

        holder.subtitle1.setText(j.getEventLocation());
        holder.title.setText(j.getEventTitle());
        holder.subtitle2.setText(j.getEventDescription());
    }

    @Override
    public int getItemCount() {
        return jadwals.size();
    }
}
