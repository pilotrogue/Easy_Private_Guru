package com.easyprivate.easyprivateguru.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.easyprivate.easyprivateguru.CustomUtility;
import com.easyprivate.easyprivateguru.UserHelper;
import com.easyprivate.easyprivateguru.activities.DetailPemesananActivity;
import com.easyprivate.easyprivateguru.models.Absen;
import com.easyprivate.easyprivateguru.models.JadwalPengganti;
import com.easyprivate.easyprivateguru.models.Pemesanan;
import com.easyprivate.easyprivateguru.models.User;
import com.easyprivate.easyprivateguru.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class AbsenRVAdapter extends RecyclerView.Adapter<AbsenRVAdapter.ViewHolder>{
    private Context mContext;
    private ArrayList<Absen> absens = new ArrayList<>();
    private UserHelper userHelper;

    public class ViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout rvItem;
        TextView subtitle1, title, subtitle2, subtitle3;
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

    public AbsenRVAdapter(Context mContext, ArrayList<Absen> absens) {
        this.mContext = mContext;
        this.absens = absens;
        this.userHelper = new UserHelper(mContext);
        Log.d(TAG, "AbsenRVAdapter: absens.size: "+absens.size());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_card_primary, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Absen a = absens.get(position);
        JadwalPengganti jp = a.getJadwalPengganti();
        Pemesanan p = a.getPemesanan();
        User guru = p.getGuru();
        User murid = p.getMurid();

        CustomUtility customUtility = new CustomUtility(mContext);
        customUtility.putIntoImage(murid.getAvatar(), holder.image);

        holder.title.setText(murid.getName());

        String dateStr = customUtility.reformatDateTime(a.getWaktuAbsen(), "yyyy-MM-dd HH:mm:ss", "EEEE, dd MMMM yyyy, HH:mm");
        holder.subtitle2.setText(dateStr);

        String countDateStr = customUtility.getCountTimeString(a.getWaktuAbsen());
        holder.subtitle1.setText(p.getMataPelajaran().getNamaMapel());
        holder.subtitle3.setVisibility(View.GONE);

        if(jp != null){
            holder.subtitle3.setVisibility(View.VISIBLE);

            String waktuAbsenStr = customUtility.reformatDateTime(a.getWaktuAbsen(), "yyyy-MM-dd HH:mm:ss", "EEEE, dd MMMM yyyy");

            holder.subtitle3.setText("Jadwal pengganti \n "+waktuAbsenStr);
            holder.subtitle3.setTextColor(mContext.getResources().getColor(R.color.fontDark));
            holder.subtitle3.setTypeface(null);

            dateStr = customUtility.reformatDateTime(jp.getWaktuPengganti(), "yyyy-MM-dd HH:mm:ss", "EEEE, dd MMMM yyyy, HH:mm");
            holder.subtitle2.setText(dateStr);
        }

        holder.rvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, DetailPemesananActivity.class);
                i.putExtra("idPemesanan", a.getIdPemesanan());
                mContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return absens.size();
    }
}
