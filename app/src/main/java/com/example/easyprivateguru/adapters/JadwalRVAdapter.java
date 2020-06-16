package com.example.easyprivateguru.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyprivateguru.CustomUtility;
import com.example.easyprivateguru.R;
import com.example.easyprivateguru.UserHelper;
import com.example.easyprivateguru.activities.DetailJadwalActivity;
import com.example.easyprivateguru.activities.DetailPemesananActivity;
import com.example.easyprivateguru.models.JadwalAvailable;
import com.example.easyprivateguru.models.JadwalPemesananPerminggu;
import com.example.easyprivateguru.models.Pemesanan;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class JadwalRVAdapter extends RecyclerView.Adapter<JadwalRVAdapter.ViewHolder>{
    private Context mContext;
    private ArrayList<JadwalAvailable> jadwalAvailableArrayList = new ArrayList<>();
    private UserHelper userHelper;

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvHari, tvJam, tvNamaMurid, tvMapel;
        LinearLayout llBody;
        RelativeLayout rvItem, rlHari;
        CircleImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            rvItem = itemView.findViewById(R.id.rvItem);
            rlHari = itemView.findViewById(R.id.rlHari);
            llBody = itemView.findViewById(R.id.llBody);

            image = itemView.findViewById(R.id.civPic);

            tvHari = itemView.findViewById(R.id.tvHari);
            tvJam = itemView.findViewById(R.id.tvJam);
            tvNamaMurid = itemView.findViewById(R.id.tvNamaMurid);
            tvMapel = itemView.findViewById(R.id.tvMapel);
        }
    }

    public JadwalRVAdapter(Context mContext, ArrayList<JadwalAvailable> jadwalAvailableArrayList) {
        this.mContext = mContext;
        this.jadwalAvailableArrayList = jadwalAvailableArrayList;
        this.userHelper = new UserHelper(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_card_secondary, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JadwalAvailable ja = jadwalAvailableArrayList.get(position);
        JadwalPemesananPerminggu jpp = ja.getJadwalPemesananPerminggu();
        Log.d(TAG, "onBindViewHolder: id jadwal available: "+ja.getIdJadwalAvailable());

        holder.rlHari.setVisibility(View.GONE);

        //Menampilkan hari
        if(position > 0){
            JadwalAvailable jax = jadwalAvailableArrayList.get(position - 1);
            if(!jax.getHari().equals(ja.getHari())){
                holder.rlHari.setVisibility(View.VISIBLE);
                holder.tvHari.setText(ja.getHari());
            }
        }else if(position == 0){
            holder.rlHari.setVisibility(View.VISIBLE);
            holder.tvHari.setText(ja.getHari());
        }

        CustomUtility cu = new CustomUtility(mContext);
        //Menampilkan jam
        String jamStartStr = cu.reformatDateTime(ja.getStart(), "HH:mm:ss", "HH:mm");
        String jamEndStr = cu.reformatDateTime(ja.getEnd(), "HH:mm:ss", "HH:mm");

        holder.tvJam.setText(jamStartStr);

        if(jpp == null){
            holder.llBody.setVisibility(View.GONE);
            holder.tvJam.setBackgroundResource(R.drawable.background_white);
            holder.tvJam.setTextColor(mContext.getResources().getColor(R.color.fontLight));
        }else{
            holder.llBody.setVisibility(View.VISIBLE);
            holder.tvJam.setBackgroundResource(R.drawable.background_blue);
            holder.tvJam.setTextColor(mContext.getResources().getColor(R.color.white));
            Pemesanan pem = jpp.getPemesanan();

            //Menampilkan avatar murid
            cu.putIntoImage(pem.getMurid().getAvatar(), holder.image);

            //Menampilkan nama murid
            holder.tvNamaMurid.setText(pem.getMurid().getName());

            //Menampilkan mata pelajaran dan jenjang
            holder.tvMapel.setText(pem.getMataPelajaran().getNamaMapel() + " Kelas "+pem.getKelas());

            holder.rvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContext, DetailPemesananActivity.class);
                    i.putExtra("idPemesanan", pem.getIdPemesanan());
                    mContext.startActivity(i);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return jadwalAvailableArrayList.size();
    }
}
