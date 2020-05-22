package com.example.easyprivateguru.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JadwalAvailable implements Parcelable{
    private int changedAvailability = 99;

    @Expose
    @SerializedName("available")
    private int available;
    @Expose
    @SerializedName("end")
    private String end;
    @Expose
    @SerializedName("start")
    private String start;
    @Expose
    @SerializedName("hari")
    private String hari;
    @Expose
    @SerializedName("id_user")
    private int idUser;
    @Expose
    @SerializedName("id_jadwal_available")
    private int idJadwalAvailable;

    public JadwalAvailable() {
    }

    protected JadwalAvailable(Parcel in) {
        changedAvailability = in.readInt();
        available = in.readInt();
        end = in.readString();
        start = in.readString();
        hari = in.readString();
        idUser = in.readInt();
        idJadwalAvailable = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(changedAvailability);
        dest.writeInt(available);
        dest.writeString(end);
        dest.writeString(start);
        dest.writeString(hari);
        dest.writeInt(idUser);
        dest.writeInt(idJadwalAvailable);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<JadwalAvailable> CREATOR = new Creator<JadwalAvailable>() {
        @Override
        public JadwalAvailable createFromParcel(Parcel in) {
            return new JadwalAvailable(in);
        }

        @Override
        public JadwalAvailable[] newArray(int size) {
            return new JadwalAvailable[size];
        }
    };

    public int getChangedAvailability() {
        return changedAvailability;
    }

    public void setChangedAvailability(int changedAvailability) {
        this.changedAvailability = changedAvailability;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getHari() {
        return hari;
    }

    public void setHari(String hari) {
        this.hari = hari;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdJadwalAvailable() {
        return idJadwalAvailable;
    }

    public void setIdJadwalAvailable(int idJadwalAvailable) {
        this.idJadwalAvailable = idJadwalAvailable;
    }
}
