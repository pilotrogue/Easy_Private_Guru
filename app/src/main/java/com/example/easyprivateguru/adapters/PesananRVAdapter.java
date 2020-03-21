package com.example.easyprivateguru.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyprivateguru.models.MataPelajaran;
import com.example.easyprivateguru.models.Pemesanan;
import com.example.easyprivateguru.models.User;
import com.example.easyprivateguru.R;

import java.util.ArrayList;

public class PesananRVAdapter extends RecyclerView.Adapter<PesananRVAdapter.ViewHolder>{
    private Context mContext;
    private ArrayList<Pemesanan> pesanans = new ArrayList<>();
    private static final String TAG = "PesananRVAdapter";

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView subtitle1, title, subtitle2;
        private ImageView image;
        private static final String TAG = "ViewHolder";

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.d(TAG, "ViewHolder: called");
            subtitle1 = itemView.findViewById(R.id.tvSubtitle1);
            title = itemView.findViewById(R.id.tvTitle);
            subtitle2 = itemView.findViewById(R.id.tvSubtitle2);
            image = itemView.findViewById(R.id.ivPic);
        }
    }

    public PesananRVAdapter(Context mContext, ArrayList<Pemesanan> pesanans) {
        Log.d(TAG, "PesananRVAdapter: called");
        this.mContext = mContext;
        this.pesanans = pesanans;
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

        holder.subtitle1.setText(murid.getEmail());
        holder.title.setText(murid.getName());
        holder.subtitle2.setText(mataPelajaran.getNamaMapel());
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: "+pesanans.size());
        return pesanans.size();
    }
}
