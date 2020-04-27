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

import com.example.easyprivateguru.R;
import com.example.easyprivateguru.UserHelper;
import com.example.easyprivateguru.activities.DetailJadwalActivity;
import com.example.easyprivateguru.models.JadwalAjar;
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
    private ArrayList<JadwalAjar> jadwalAjars = new ArrayList<>();
    private UserHelper userHelper;

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title, subtitle1, subtitle2, subtitle3;
        RelativeLayout rvItem;
        CircleImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            rvItem = itemView.findViewById(R.id.rvItem);
            image = itemView.findViewById(R.id.civPic);

            title = itemView.findViewById(R.id.tvTitle);
            subtitle1 = itemView.findViewById(R.id.tvSubtitle1);
            subtitle2 = itemView.findViewById(R.id.tvSubtitle2);
            subtitle3 = itemView.findViewById(R.id.tvSubtitle3);
        }
    }

    public JadwalRVAdapter(Context mContext, ArrayList<JadwalAjar> jadwalAjars) {
        this.mContext = mContext;
        this.jadwalAjars = jadwalAjars;
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
        JadwalAjar j = jadwalAjars.get(position);
        Log.d(TAG, "onBindViewHolder: eventId: "+j.getIdJadwalAjar());

        userHelper.putIntoImage(j.getPemesanan().getMurid().getAvatar(), holder.image);

        holder.title.setText(j.getPemesanan().getMurid().getName());
        holder.subtitle1.setText(reformatDate(j.getWaktuAjar()));
        holder.subtitle2.setText(j.getPemesanan().getMataPelajaran().getNamaMapel());

        holder.rvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, DetailJadwalActivity.class);
                i.putExtra("idJadwalAjar", j.getIdJadwalAjar());
                mContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return jadwalAjars.size();
    }

    private String reformatDate(String dateStr){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            Date date = sdf.parse(dateStr);

            sdf.applyPattern("dd MMMM yyyy");
            String tanggal = sdf.format(date);

            sdf.applyPattern("HH:mm");
            String waktu = sdf.format(date);

            return tanggal+", "+waktu;
        }catch (ParseException e){
            Log.d(TAG, "reformatDate: "+ e.getMessage());
            return "Error";
        }
    }
}
