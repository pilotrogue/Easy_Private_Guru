package com.example.easyprivateguru.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyprivateguru.models.Absen;
import com.example.easyprivateguru.models.Pemesanan;
import com.example.easyprivateguru.models.User;
import com.example.easyprivateguru.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class AbsenRVAdapter extends RecyclerView.Adapter<AbsenRVAdapter.ViewHolder>{
    private Context mContext;
    private ArrayList<Absen> absens = new ArrayList<>();

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
        Log.d(TAG, "AbsenRVAdapter: absens.size: "+absens.size());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Absen a = absens.get(position);
        Pemesanan p = a.getPemesanan();
        User guru = p.getGuru();
        User murid = p.getMurid();

        Picasso.get()
                .load(murid.getAvatar())
                .placeholder(R.drawable.account_default)
                .error(R.drawable.account_default)
                .noFade()
                .into(holder.image);

        holder.title.setText(murid.getName());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            Date date = sdf.parse(a.getWaktuAbsen());

            sdf.applyPattern("dd MMMM yyyy");
            String tanggal = sdf.format(date);

            sdf.applyPattern("HH:mm");
            String waktu = sdf.format(date);

            holder.subtitle1.setText(tanggal+", "+waktu);
        }catch (ParseException e){
            Log.d(TAG, "onBindViewHolder: "+e.getMessage());
            holder.subtitle1.setText("Error");
        }

        holder.subtitle2.setText(p.getMataPelajaran().getNamaMapel());
    }

    @Override
    public int getItemCount() {
        return absens.size();
    }
}
