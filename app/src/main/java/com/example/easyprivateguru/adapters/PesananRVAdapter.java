package com.example.easyprivateguru.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyprivateguru.UserHelper;
import com.example.easyprivateguru.activities.DetailPemesananActivity;
import com.example.easyprivateguru.models.MataPelajaran;
import com.example.easyprivateguru.models.Pemesanan;
import com.example.easyprivateguru.models.User;
import com.example.easyprivateguru.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PesananRVAdapter extends RecyclerView.Adapter<PesananRVAdapter.ViewHolder>{
    private Context mContext;
    private ArrayList<Pemesanan> pesanans = new ArrayList<>();
    private UserHelper userHelper;
    private static final String TAG = "PesananRVAdapter";

    public class ViewHolder extends RecyclerView.ViewHolder{
        private RelativeLayout rvCardItem;
        private TextView title, subtitle1, subtitle2, subtitle3;
        private CircleImageView image;
        private static final String TAG = "ViewHolder";

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.d(TAG, "ViewHolder: called");
            rvCardItem = itemView.findViewById(R.id.rvItem);

            image = itemView.findViewById(R.id.civPic);
            title = itemView.findViewById(R.id.tvTitle);
            subtitle1 = itemView.findViewById(R.id.tvSubtitle1);
            subtitle2 = itemView.findViewById(R.id.tvSubtitle2);
            subtitle3 = itemView.findViewById(R.id.tvSubtitle3);
        }
    }

    public PesananRVAdapter(Context mContext, ArrayList<Pemesanan> pesanans) {
        Log.d(TAG, "PesananRVAdapter: called");
        this.mContext = mContext;
        this.pesanans = pesanans;
        this.userHelper = new UserHelper(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: called");
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_card, parent, false);
        return new PesananRVAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called");
        Pemesanan p = pesanans.get(position);
        User murid = p.getMurid();
        MataPelajaran mataPelajaran = p.getMataPelajaran();

        userHelper.putIntoImage(murid.getAvatar(), holder.image);

        holder.title.setText(murid.getName());
        holder.subtitle1.setText(murid.getAlamat().getAlamatLengkap());
        holder.subtitle2.setText(mataPelajaran.getNamaMapel());
        holder.subtitle3.setVisibility(View.VISIBLE);

        holder.rvCardItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailPemesananActivity.class);
                intent.putExtra("idPemesanan", p.getIdPemesanan());

                mContext.startActivity(intent);
            }
        });

        switch (p.getStatus()){
            case 0:
                holder.subtitle3.setText("Pemesanan baru");
                holder.subtitle3.setTextColor(mContext.getResources().getColor(R.color.white));
                holder.subtitle3.setBackgroundColor(mContext.getResources().getColor(R.color.yellow));
                break;
            case 1:
                holder.subtitle3.setText("Pemesanan diterima");
                holder.subtitle3.setTextColor(mContext.getResources().getColor(R.color.white));
                holder.subtitle3.setBackgroundColor(mContext.getResources().getColor(R.color.green));
                break;
            case 2:
                holder.subtitle3.setText("Pemesanan ditolak");
                holder.subtitle3.setTextColor(mContext.getResources().getColor(R.color.white));
                holder.subtitle3.setBackgroundColor(mContext.getResources().getColor(R.color.red));
                holder.rvCardItem.setOnClickListener(null);
                break;
            case 3:
                holder.subtitle3.setText("Pemesanan selesai");
                holder.subtitle3.setTextColor(mContext.getResources().getColor(R.color.fontDark));
                holder.subtitle3.setBackgroundColor(mContext.getResources().getColor(R.color.whiteDark));
                holder.rvCardItem.setOnClickListener(null);
                break;
            default:
                holder.subtitle3.setText("Hmm... ada sesuatu yang salah");
                holder.subtitle3.setTextColor(mContext.getResources().getColor(R.color.fontDark));
                holder.subtitle3.setBackgroundColor(mContext.getResources().getColor(R.color.whiteDark));
                holder.rvCardItem.setOnClickListener(null);
                break;
        }
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: "+pesanans.size());
        return pesanans.size();
    }
}
