package com.easyprivate.easyprivateguru.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.easyprivate.easyprivateguru.CustomUtility;
import com.easyprivate.easyprivateguru.R;
import com.easyprivate.easyprivateguru.activities.DetailPembayaranActivity;
import com.easyprivate.easyprivateguru.models.AbsenPembayaran;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AbsenPembayaranRVAdapter extends RecyclerView.Adapter<AbsenPembayaranRVAdapter.ViewHolder>{
    private Context mContext;
    private ArrayList<AbsenPembayaran> absenPembayaranArrayList = new ArrayList<>();
    private CustomUtility cu;

    public AbsenPembayaranRVAdapter(Context mContext, ArrayList<AbsenPembayaran> absenPembayaranArrayList) {
        this.mContext = mContext;
        this.absenPembayaranArrayList = absenPembayaranArrayList;
        this.cu = new CustomUtility(mContext);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvBulan, tvSidePrompt, tvHeader, tvStatus;
        LinearLayout llBody;
        RelativeLayout rvItem, rlHeader;
        CircleImageView image;
        ImageView ivGoogleCalendar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            rvItem = itemView.findViewById(R.id.rvItem);
            rlHeader = itemView.findViewById(R.id.rlHari);
            llBody = itemView.findViewById(R.id.llBody);

            ivGoogleCalendar = itemView.findViewById(R.id.ivGoogleCalendar);
            image = itemView.findViewById(R.id.civPic);

            tvBulan = itemView.findViewById(R.id.tvNamaMurid);
            tvSidePrompt = itemView.findViewById(R.id.tvJam);
            tvHeader = itemView.findViewById(R.id.tvHari);
            tvStatus = itemView.findViewById(R.id.tvMapel);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_card_secondary, parent, false);
        return new AbsenPembayaranRVAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AbsenPembayaran ap = absenPembayaranArrayList.get(position);

        //Bulan
        String bulanStr = cu.reformatDateTime(String.valueOf(ap.getBulan()), "MM", "MMMM");
        holder.tvBulan.setText(bulanStr);

        //Tahun
        holder.rlHeader.setVisibility(View.GONE);
        String tahunStr = String.valueOf(ap.getTahun());

        if(position > 0){
            AbsenPembayaran apx = absenPembayaranArrayList.get(position-1);
            String tahunStrX = String.valueOf(apx.getTahun());
            if(!tahunStr.equals(tahunStrX)){
                holder.rlHeader.setVisibility(View.VISIBLE);
                holder.tvHeader.setText(tahunStr);
            }
        }else if(position == 0){
            holder.rlHeader.setVisibility(View.VISIBLE);
            holder.tvHeader.setText(tahunStr);
        }

        //SidePrompt
        holder.tvSidePrompt.setVisibility(View.GONE);

        //Status
        holder.tvStatus.setVisibility(View.GONE);

        //Image
        holder.image.setVisibility(View.GONE);

        //Logo Google Calendar
        holder.ivGoogleCalendar.setVisibility(View.GONE);

        holder.rvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, DetailPembayaranActivity.class);
                i.putExtra("bulan", ap.getBulan());
                i.putExtra("tahun", ap.getTahun());
                mContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return absenPembayaranArrayList.size();
    }
}
