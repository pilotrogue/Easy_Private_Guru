package com.example.easyprivateguru.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Jenjang implements Parcelable {

    @Expose
    @SerializedName("upah_guru_per_pertemuan")
    private int upahGuruPerPertemuan;

    @Expose
    @SerializedName("harga_per_pertemuan")
    private int hargaPerPertemuan;

    @Expose
    @SerializedName("nama_jenjang")
    private String namaJenjang;

    @Expose
    @SerializedName("id_jenjang")
    private int idJenjang;

    public Jenjang(String namaJenjang, int upahGuruPerPertemuan, int hargaPerPertemuan) {
        this.upahGuruPerPertemuan = upahGuruPerPertemuan;
        this.hargaPerPertemuan = hargaPerPertemuan;
        this.namaJenjang = namaJenjang;
    }

    public Jenjang() {
    }

    protected Jenjang(Parcel in) {
        upahGuruPerPertemuan = in.readInt();
        hargaPerPertemuan = in.readInt();
        namaJenjang = in.readString();
        idJenjang = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(upahGuruPerPertemuan);
        dest.writeInt(hargaPerPertemuan);
        dest.writeString(namaJenjang);
        dest.writeInt(idJenjang);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Jenjang> CREATOR = new Creator<Jenjang>() {
        @Override
        public Jenjang createFromParcel(Parcel in) {
            return new Jenjang(in);
        }

        @Override
        public Jenjang[] newArray(int size) {
            return new Jenjang[size];
        }
    };

    public int getUpahGuruPerPertemuan() {
        return upahGuruPerPertemuan;
    }

    public void setUpahGuruPerPertemuan(int upahGuruPerPertemuan) {
        this.upahGuruPerPertemuan = upahGuruPerPertemuan;
    }

    public int getHargaPerPertemuan() {
        return hargaPerPertemuan;
    }

    public void setHargaPerPertemuan(int hargaPerPertemuan) {
        this.hargaPerPertemuan = hargaPerPertemuan;
    }

    public String getNamaJenjang() {
        return namaJenjang;
    }

    public void setNamaJenjang(String namaJenjang) {
        this.namaJenjang = namaJenjang;
    }

    public int getIdJenjang() {
        return idJenjang;
    }

    public void setIdJenjang(int idJenjang) {
        this.idJenjang = idJenjang;
    }
}
