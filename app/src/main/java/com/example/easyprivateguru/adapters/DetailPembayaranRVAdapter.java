package com.example.easyprivateguru.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyprivateguru.CustomUtility;
import com.example.easyprivateguru.models.Absen;
import com.example.easyprivateguru.models.AbsenPembayaran;
import com.example.easyprivateguru.models.Pembayaran;
import com.example.easyprivateguru.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailPembayaranRVAdapter extends RecyclerView.Adapter<DetailPembayaranRVAdapter.ViewHolder>{
    private Context mContext;
    private ArrayList<AbsenPembayaran> absenPembayaranArrayList = new ArrayList<>();

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvPeriode, tvNamaMurid, tvMapel, tvHargaDanPertemuan, tvTotal, tvStatus;
        private CircleImageView image;
        private LinearLayout llPeriode;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            llPeriode = itemView.findViewById(R.id.llPeriode);
            tvPeriode = itemView.findViewById(R.id.tvPeriode);
            tvNamaMurid = itemView.findViewById(R.id.tvNamaMurid);
            tvMapel = itemView.findViewById(R.id.tvMapel);
            tvHargaDanPertemuan = itemView.findViewById(R.id.tvHargaDanPertemuan);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            image = itemView.findViewById(R.id.civPic);
        }
    }

    public DetailPembayaranRVAdapter(Context mContext, ArrayList<AbsenPembayaran> absenPembayaranArrayList) {
        this.mContext = mContext;
        this.absenPembayaranArrayList = absenPembayaranArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_card_pembayaran, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AbsenPembayaran ap = absenPembayaranArrayList.get(position);
        CustomUtility cu = new CustomUtility(mContext);

        //Periode
        holder.llPeriode.setVisibility(View.GONE);
        String bulanStr = cu.reformatDateTime(ap.getBulan().toString(), "MM", "MMMM");
        String tahunStr = ap.getTahun().toString();
        String periodeStr = bulanStr + " " + tahunStr;
        if(position > 0){
            AbsenPembayaran apx = absenPembayaranArrayList.get(position-1);
            String bulanStrX = cu.reformatDateTime(apx.getBulan().toString(), "MM", "MMMM");
            String tahunStrX = apx.getTahun().toString();
            String periodeStrX = bulanStrX + " " + tahunStrX;
            if(!periodeStr.equals(periodeStrX)){
                holder.llPeriode.setVisibility(View.VISIBLE);
                holder.tvPeriode.setText(periodeStr);
            }
        }else if(position == 0){
            holder.llPeriode.setVisibility(View.VISIBLE);
            holder.tvPeriode.setText(periodeStr);
        }

        //Image
        cu.putIntoImage(ap.getMurid().getAvatar(), holder.image);

        //Nama murid
        holder.tvNamaMurid.setText(ap.getMurid().getName());

        //Mapel
        String mapelStr = ap.getPemesanan().getMataPelajaran().getNamaMapel()+", kelas "+ap.getPemesanan().getKelas();
        holder.tvMapel.setText(mapelStr);

        //Status
        String statusStr = "";
        holder.tvStatus.setTextColor(mContext.getResources().getColor(R.color.white));
        if(ap.getIdPembayaran() != null){
            statusStr = "Pembayaran selesai";
            holder.tvStatus.setBackgroundColor(mContext.getResources().getColor(R.color.blue));
        }else{
            statusStr = "Menunggu pembayaran";
            holder.tvStatus.setBackgroundColor(mContext.getResources().getColor(R.color.yellow));
        }
        holder.tvStatus.setText(statusStr);

        //Harga dan jumlah pertemuan
        Integer harga = ap.getPemesanan().getMataPelajaran().getJenjang().getUpahGuruPerPertemuan();
        Integer jumlahPertemuan = ap.getJumlahAbsen();
        String hargaStr = "Rp "+harga.toString();
        String jumlahPertemuanStr = jumlahPertemuan.toString() + " pertemuan";
        holder.tvHargaDanPertemuan.setText(hargaStr + "\n x " + jumlahPertemuanStr);

        //Total
        Integer totalInt = harga*jumlahPertemuan;
        String totalString = "Rp "+totalInt.toString();
        holder.tvTotal.setText(totalString);
    }

    @Override
    public int getItemCount() {
        return absenPembayaranArrayList.size();
    }
}
