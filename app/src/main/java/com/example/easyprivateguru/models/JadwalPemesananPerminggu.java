package com.example.easyprivateguru.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JadwalPemesananPerminggu implements Parcelable {

    @Expose
    @SerializedName("pemesanan")
    private Pemesanan pemesanan;
    @Expose
    @SerializedName("jadwal_available")
    private JadwalAvailable jadwalAvailable;
    @Expose
    @SerializedName("id_event")
    private int idEvent;
    @Expose
    @SerializedName("id_jadwal_available")
    private int idJadwalAvailable;
    @Expose
    @SerializedName("id_pemesanan")
    private int idPemesanan;
    @Expose
    @SerializedName("id_jadwal_pemesanan_perminggu")
    private int idJadwalPemesananPerminggu;

    public JadwalPemesananPerminggu() {
    }

    protected JadwalPemesananPerminggu(Parcel in) {
        pemesanan = in.readParcelable(Pemesanan.class.getClassLoader());
        jadwalAvailable = in.readParcelable(JadwalAvailable.class.getClassLoader());
        idEvent = in.readInt();
        idJadwalAvailable = in.readInt();
        idPemesanan = in.readInt();
        idJadwalPemesananPerminggu = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(pemesanan, flags);
        dest.writeParcelable(jadwalAvailable, flags);
        dest.writeInt(idEvent);
        dest.writeInt(idJadwalAvailable);
        dest.writeInt(idPemesanan);
        dest.writeInt(idJadwalPemesananPerminggu);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<JadwalPemesananPerminggu> CREATOR = new Creator<JadwalPemesananPerminggu>() {
        @Override
        public JadwalPemesananPerminggu createFromParcel(Parcel in) {
            return new JadwalPemesananPerminggu(in);
        }

        @Override
        public JadwalPemesananPerminggu[] newArray(int size) {
            return new JadwalPemesananPerminggu[size];
        }
    };

    public Pemesanan getPemesanan() {
        return pemesanan;
    }

    public void setPemesanan(Pemesanan pemesanan) {
        this.pemesanan = pemesanan;
    }

    public JadwalAvailable getJadwalAvailable() {
        return jadwalAvailable;
    }

    public void setJadwalAvailable(JadwalAvailable jadwalAvailable) {
        this.jadwalAvailable = jadwalAvailable;
    }

    public int getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(int idEvent) {
        this.idEvent = idEvent;
    }

    public int getIdJadwalAvailable() {
        return idJadwalAvailable;
    }

    public void setIdJadwalAvailable(int idJadwalAvailable) {
        this.idJadwalAvailable = idJadwalAvailable;
    }

    public int getIdPemesanan() {
        return idPemesanan;
    }

    public void setIdPemesanan(int idPemesanan) {
        this.idPemesanan = idPemesanan;
    }

    public int getIdJadwalPemesananPerminggu() {
        return idJadwalPemesananPerminggu;
    }

    public void setIdJadwalPemesananPerminggu(int idJadwalPemesananPerminggu) {
        this.idJadwalPemesananPerminggu = idJadwalPemesananPerminggu;
    }
}
