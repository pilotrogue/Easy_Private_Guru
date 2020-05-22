package com.example.easyprivateguru.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyprivateguru.R;
import com.example.easyprivateguru.models.JadwalAvailable;

import java.util.ArrayList;

public class JadwalAvailableRVAdapter extends RecyclerView.Adapter<JadwalAvailableRVAdapter.ViewHolder>{
    private Context mContext;
    private ArrayList<JadwalAvailable> jadwalAvailableArrayList = new ArrayList<>();

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvJam;
        private LinearLayout llItemCard;

        public ViewHolder(@NonNull View iv) {
            super(iv);
            llItemCard = iv.findViewById(R.id.llItemCard);
            tvJam = iv.findViewById(R.id.tvJam);
        }
    }

    public JadwalAvailableRVAdapter(Context mContext, ArrayList<JadwalAvailable> jadwalAvailableArrayList) {
        this.mContext = mContext;
        this.jadwalAvailableArrayList = jadwalAvailableArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_card_time, parent, false);
        return new JadwalAvailableRVAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JadwalAvailable ja = jadwalAvailableArrayList.get(position);
        holder.tvJam.setText(ja.getStart());

        switch (ja.getAvailable()){
            case 0:
                holder.tvJam.setTextColor(mContext.getResources().getColor(R.color.blue));
                holder.llItemCard.setBackgroundResource(R.drawable.background_time_card_white);
                break;
            case 1:
            case 2:
                holder.tvJam.setTextColor(mContext.getResources().getColor(R.color.fontLight));
                holder.llItemCard.setBackgroundResource(R.drawable.background_time_card_blue);
                break;
        }

        holder.llItemCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (ja.getAvailable()){
                    case 0:
                        ja.setAvailable(1);
                        holder.tvJam.setTextColor(mContext.getResources().getColor(R.color.fontLight));
                        holder.llItemCard.setBackgroundResource(R.drawable.background_time_card_blue);
                        break;
                    case 1:
                    case 2:
                        ja.setAvailable(0);
                        holder.tvJam.setTextColor(mContext.getResources().getColor(R.color.blue));
                        holder.llItemCard.setBackgroundResource(R.drawable.background_time_card_white);
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return jadwalAvailableArrayList.size();
    }
}
