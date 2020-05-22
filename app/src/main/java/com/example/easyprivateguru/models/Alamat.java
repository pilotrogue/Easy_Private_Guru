package com.example.easyprivateguru.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Alamat implements Parcelable {
    @Expose
    @SerializedName("alamat_lengkap")
    private String alamatLengkap;
    @Expose
    @SerializedName("longitude")
    private double longitude;
    @Expose
    @SerializedName("latitude")
    private double latitude;
    @Expose
    @SerializedName("id_user")
    private int idUser;
    @Expose
    @SerializedName("id_alamat")
    private int idAlamat;

    public Alamat() {
    }

    protected Alamat(Parcel in) {
        alamatLengkap = in.readString();
        longitude = in.readDouble();
        latitude = in.readDouble();
        idUser = in.readInt();
        idAlamat = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(alamatLengkap);
        dest.writeDouble(longitude);
        dest.writeDouble(latitude);
        dest.writeInt(idUser);
        dest.writeInt(idAlamat);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Alamat> CREATOR = new Creator<Alamat>() {
        @Override
        public Alamat createFromParcel(Parcel in) {
            return new Alamat(in);
        }

        @Override
        public Alamat[] newArray(int size) {
            return new Alamat[size];
        }
    };

    public String getAlamatLengkap() {
        return alamatLengkap;
    }

    public void setAlamatLengkap(String alamatLengkap) {
        this.alamatLengkap = alamatLengkap;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdAlamat() {
        return idAlamat;
    }

    public void setIdAlamat(int idAlamat) {
        this.idAlamat = idAlamat;
    }
}
