package com.example.easyprivateguru.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Absen implements Parcelable {
    @Expose
    @SerializedName("pemesanan")
    private Pemesanan pemesanan;
    @Expose
    @SerializedName("id_absen")
    private int idAbsen;
    @Expose
    @SerializedName("jadwal_ajar")
    private JadwalAjar jadwalAjar;
    @Expose
    @SerializedName("waktu_absen")
    private String waktuAbsen;
    @Expose
    @SerializedName("id_pemesanan")
    private int idPemesanan;
    @Expose
    @SerializedName("id_jadwal_ajar")
    private int idJadwalAjar;

    public Absen() {
    }

    protected Absen(Parcel in) {
        pemesanan = in.readParcelable(Pemesanan.class.getClassLoader());
        idAbsen = in.readInt();
        waktuAbsen = in.readString();
        idPemesanan = in.readInt();
        idJadwalAjar = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(pemesanan, flags);
        dest.writeInt(idAbsen);
        dest.writeString(waktuAbsen);
        dest.writeInt(idPemesanan);
        dest.writeInt(idJadwalAjar);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Absen> CREATOR = new Creator<Absen>() {
        @Override
        public Absen createFromParcel(Parcel in) {
            return new Absen(in);
        }

        @Override
        public Absen[] newArray(int size) {
            return new Absen[size];
        }
    };

    public Pemesanan getPemesanan() {
        return pemesanan;
    }

    public void setPemesanan(Pemesanan pemesanan) {
        this.pemesanan = pemesanan;
    }

    public int getIdAbsen() {
        return idAbsen;
    }

    public void setIdAbsen(int idAbsen) {
        this.idAbsen = idAbsen;
    }

    public JadwalAjar getJadwalAjar() {
        return jadwalAjar;
    }

    public void setJadwalAjar(JadwalAjar jadwalAjar) {
        this.jadwalAjar = jadwalAjar;
    }

    public String getWaktuAbsen() {
        return waktuAbsen;
    }

    public void setWaktuAbsen(String waktuAbsen) {
        this.waktuAbsen = waktuAbsen;
    }

    public int getIdPemesanan() {
        return idPemesanan;
    }

    public void setIdPemesanan(int idPemesanan) {
        this.idPemesanan = idPemesanan;
    }

    public int getIdJadwalAjar() {
        return idJadwalAjar;
    }

    public void setIdJadwalAjar(int idJadwalAjar) {
        this.idJadwalAjar = idJadwalAjar;
    }
}
