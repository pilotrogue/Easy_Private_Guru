package com.example.easyprivateguru.adapters;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyprivateguru.CustomUtility;
import com.example.easyprivateguru.UserHelper;
import com.example.easyprivateguru.activities.DetailMuridActivity;
import com.example.easyprivateguru.models.Alamat;
import com.example.easyprivateguru.models.MataPelajaran;
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

public class MuridSayaRVAdapter extends RecyclerView.Adapter<MuridSayaRVAdapter.ViewHolder>{
    private Context mContext;
    private ArrayList<Pemesanan> pesanans= new ArrayList<>();
    private UserHelper userHelper;

    public class ViewHolder extends RecyclerView.ViewHolder{
        private RelativeLayout rvItem;
        private TextView title, subtitle1, subtitle2, subtitle3;
        private CircleImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rvItem = itemView.findViewById(R.id.rvItem);
            title = itemView.findViewById(R.id.tvTitle);
            subtitle1 = itemView.findViewById(R.id.tvSubtitle1);
            subtitle2 = itemView.findViewById(R.id.tvSubtitle2);
            subtitle3 = itemView.findViewById(R.id.tvSubtitle3);
            image = itemView.findViewById(R.id.civPic);
        }
    }

    public MuridSayaRVAdapter(Context mContext, ArrayList<Pemesanan> pesanans) {
        this.mContext = mContext;
        this.pesanans = pesanans;
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
        Pemesanan pesanan = pesanans.get(position);
        User murid = pesanan.getMurid();
        MataPelajaran mataPelajaran = pesanan.getMataPelajaran();

        CustomUtility customUtility = new CustomUtility(mContext);

        customUtility.putIntoImage(murid.getAvatar(), holder.image);
        holder.title.setText(murid.getName());

        Alamat currAlamat = murid.getAlamat();
        Address address = customUtility.getAddress(currAlamat.getLatitude(), currAlamat.getLongitude());

        String alamatStr = "";
        if(address == null){
            alamatStr = currAlamat.getAlamatLengkap();
        }else{
            alamatStr = address.getLocality() + ", " + address.getSubLocality() + ", "+address.getSubAdminArea();
        }

        holder.subtitle1.setText(alamatStr);
        holder.subtitle2.setText(mataPelajaran.getNamaMapel()+ " " + "Kelas "+pesanan.getKelas());
        holder.subtitle3.setVisibility(View.VISIBLE);
        holder.subtitle3.setText("Tekan untuk info lengkap");
        holder.subtitle3.setTextColor(mContext.getResources().getColor(R.color.fontDark));
        holder.subtitle3.setBackgroundColor(mContext.getResources().getColor(R.color.whiteDark));

        holder.rvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, DetailMuridActivity.class);
                i.putExtra("id_pemesanan", pesanan.getIdPemesanan());
                mContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pesanans.size();
    }
}
