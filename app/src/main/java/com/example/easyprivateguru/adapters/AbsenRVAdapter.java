package com.example.easyprivateguru.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyprivateguru.CustomUtility;
import com.example.easyprivateguru.UserHelper;
import com.example.easyprivateguru.models.Absen;
import com.example.easyprivateguru.models.Pemesanan;
import com.example.easyprivateguru.models.User;
import com.example.easyprivateguru.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class AbsenRVAdapter extends RecyclerView.Adapter<AbsenRVAdapter.ViewHolder>{
    private Context mContext;
    private ArrayList<Absen> absens = new ArrayList<>();
    private UserHelper userHelper;

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView subtitle1, title, subtitle2;
        CircleImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.civPic);
            title = itemView.findViewById(R.id.tvTitle);
            subtitle1 = itemView.findViewById(R.id.tvSubtitle1);
            subtitle2 = itemView.findViewById(R.id.tvSubtitle2);
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
        Pemesanan p = a.getPemesanan();
        User guru = p.getGuru();
        User murid = p.getMurid();

        CustomUtility customUtility = new CustomUtility(mContext);

        if(murid.getAvatar() != null){
            customUtility.putIntoImage(murid.getAvatar(), holder.image);
        }
        holder.title.setText(murid.getName());

        String dateStr = customUtility.reformatDateTime(a.getWaktuAbsen(), "yyyy-MM-dd HH:mm:ss", "EEEE, dd MMMM yyyy, HH:mm");
        holder.subtitle1.setText(dateStr);
        holder.subtitle2.setText(p.getMataPelajaran().getNamaMapel());
    }

    @Override
    public int getItemCount() {
        return absens.size();
    }
}
