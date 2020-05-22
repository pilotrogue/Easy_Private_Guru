package com.example.easyprivateguru.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MataPelajaran implements Parcelable {

    @Expose
    @SerializedName("jenjang")
    private Jenjang jenjang;

    @Expose
    @SerializedName("nama_mapel")
    private String namaMapel;

    @Expose
    @SerializedName("id_jenjang")
    private int idJenjang;

    @Expose
    @SerializedName("id_mapel")
    private int idMapel;

    public MataPelajaran() {
    }

    protected MataPelajaran(Parcel in) {
        jenjang = in.readParcelable(Jenjang.class.getClassLoader());
        namaMapel = in.readString();
        idJenjang = in.readInt();
        idMapel = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(jenjang, flags);
        dest.writeString(namaMapel);
        dest.writeInt(idJenjang);
        dest.writeInt(idMapel);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MataPelajaran> CREATOR = new Creator<MataPelajaran>() {
        @Override
        public MataPelajaran createFromParcel(Parcel in) {
            return new MataPelajaran(in);
        }

        @Override
        public MataPelajaran[] newArray(int size) {
            return new MataPelajaran[size];
        }
    };

    public Jenjang getJenjang() {
        return jenjang;
    }

    public void setJenjang(Jenjang jenjang) {
        this.jenjang = jenjang;
    }

    public String getNamaMapel() {
        return namaMapel;
    }

    public void setNamaMapel(String namaMapel) {
        this.namaMapel = namaMapel;
    }

    public int getIdJenjang() {
        return idJenjang;
    }

    public void setIdJenjang(int idJenjang) {
        this.idJenjang = idJenjang;
    }

    public int getIdMapel() {
        return idMapel;
    }

    public void setIdMapel(int idMapel) {
        this.idMapel = idMapel;
    }
}
