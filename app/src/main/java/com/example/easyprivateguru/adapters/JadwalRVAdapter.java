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
import com.example.easyprivateguru.models.JadwalAjar;
import com.example.easyprivateguru.models.JadwalPemesananPerminggu;
import com.example.easyprivateguru.models.User;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class JadwalRVAdapter extends RecyclerView.Adapter<JadwalRVAdapter.ViewHolder>{
    private Context mContext;
    private ArrayList<JadwalPemesananPerminggu> jadwalPemesananPermingguArrayList = new ArrayList<>();
    private UserHelper userHelper;

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView bigTitle, title, subtitle1, subtitle2, subtitle3;
        RelativeLayout rvItem, rlBigTitle;
        CircleImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            rvItem = itemView.findViewById(R.id.rvItem);
            rlBigTitle = itemView.findViewById(R.id.rlBigTitle);

            bigTitle = itemView.findViewById(R.id.tvBigTitle);

            image = itemView.findViewById(R.id.civPic);

            title = itemView.findViewById(R.id.tvTitle);
            subtitle1 = itemView.findViewById(R.id.tvSubtitle1);
            subtitle2 = itemView.findViewById(R.id.tvSubtitle2);
            subtitle3 = itemView.findViewById(R.id.tvSubtitle3);
        }
    }

    public JadwalRVAdapter(Context mContext, ArrayList<JadwalPemesananPerminggu> jadwalPemesananPermingguArrayList) {
        this.mContext = mContext;
        this.jadwalPemesananPermingguArrayList = jadwalPemesananPermingguArrayList;
        this.userHelper = new UserHelper(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JadwalPemesananPerminggu j = jadwalPemesananPermingguArrayList.get(position);
        Log.d(TAG, "onBindViewHolder: id jadwal pemesanan perminggu: "+j.getIdJadwalPemesananPerminggu());

        //Menampilkan hari
        if(position > 0){
            JadwalPemesananPerminggu jx = jadwalPemesananPermingguArrayList.get(position - 1);
            if(!jx.getJadwalAvailable().getHari().equals(j.getJadwalAvailable().getHari())){
                holder.rlBigTitle.setVisibility(View.VISIBLE);
                holder.bigTitle.setText(j.getJadwalAvailable().getHari());
            }
        }else if(position == 0){
            holder.rlBigTitle.setVisibility(View.VISIBLE);
            holder.bigTitle.setText(j.getJadwalAvailable().getHari());
        }
        CustomUtility cu = new CustomUtility(mContext);
        cu.putIntoImage(j.getPemesanan().getMurid().getAvatar(), holder.image);

        holder.title.setText(j.getPemesanan().getMurid().getName());

        String jamStartStr = cu.reformatDateTime(j.getJadwalAvailable().getStart(), "HH:mm:ss", "HH:mm");
        String jamEndStr = cu.reformatDateTime(j.getJadwalAvailable().getEnd(), "HH:mm:ss", "HH:mm");

        holder.subtitle1.setText(j.getJadwalAvailable().getHari() + ", " + jamStartStr + " s/d " + jamEndStr);
        holder.subtitle2.setText(j.getPemesanan().getMataPelajaran().getNamaMapel() + " Kelas "+j.getPemesanan().getKelas());
        holder.subtitle3.setVisibility(View.VISIBLE);
        holder.subtitle3.setText("Tekan untuk info lengkap");
        holder.subtitle3.setTextColor(mContext.getResources().getColor(R.color.fontDark));
        holder.subtitle3.setBackgroundColor(mContext.getResources().getColor(R.color.whiteDark));

        holder.rvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, DetailJadwalActivity.class);
                i.putExtra("idJadwalPemesananPerminggu", j.getIdJadwalPemesananPerminggu());
                mContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return jadwalPemesananPermingguArrayList.size();
    }
}
