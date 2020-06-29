package com.example.easyprivateguru.api;

import android.provider.CalendarContract;

import com.example.easyprivateguru.models.Absen;
import com.example.easyprivateguru.models.AbsenPembayaran;
import com.example.easyprivateguru.models.JadwalAvailable;
import com.example.easyprivateguru.models.JadwalPemesananPerminggu;
import com.example.easyprivateguru.models.Pemesanan;
import com.example.easyprivateguru.models.TanggalPengganti;
import com.example.easyprivateguru.models.User;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiInterface {
    //Login guru
    @FormUrlEncoded
    @POST("login/guru")
    Call<User> loginGuru(
            @Field("email") String email
    );

    //Get guru
    @FormUrlEncoded
    @POST("user/guru")
    Call<User> getGuru(
            @Field("email") String email
    );

    //Edit guru
    @Multipart
    @POST("user/guru/update")
    Call<User> editGuru(
            @Part("id") RequestBody id,
            @Part("name") RequestBody name,
            @Part("no_handphone") RequestBody noTelp,
            @Part MultipartBody.Part image
    );

    @FormUrlEncoded
    @POST("user/guru/valid")
    Call<Integer> isGuruValid(
            @Field("email") String email
    );

    //Get semua pemesanan
    @GET("pemesanan")
    Call<ArrayList<Pemesanan>> getAllPemesanan();

    //Get pemesanan berdasarkan id guru
    @GET("pemesanan/guru/{id}")
    Call<ArrayList<Pemesanan>> getPemesananByIdGuru(
            @Path("id") int idGuru
    );

    //Get pemesanan berdasarkan id
    @GET("pemesanan/{id}")
    Call<Pemesanan> getPemesananById(
            @Path("id") int idPemesanan
    );

    //Update pemesanan, terima/tolak
    @FormUrlEncoded
    @POST("pemesanan/update")
    Call<Pemesanan> updatePemesanan(
            @Field("id_pemesanan") int idPemesanan,
            @Field("status") int status
    );

    //Get pemesanan dengan filter
    @FormUrlEncoded
    @POST("pemesanan/filter")
    Call<ArrayList<Pemesanan>> getPemesananFiltered(
            @Field("id_pemesanan") Integer idPemesanan,
            @Field("id_guru") Integer idGuru,
            @Field("id_murid") Integer idMurid,
            @Field("status") Integer status
    );

    //Get absen filtered
    @FormUrlEncoded
    @POST("absen/filter")
    Call<ArrayList<Absen>> getAbsen(
            @Field("id_absen") Integer idAbsen,
            @Field("id_pemesanan") Integer idPemesanan,
            @Field("status") Integer status,
            @Field("id_jadwal_pemesanan_perminggu") Integer idJadwalPemesananPerminggu,
            @Field("id_guru") Integer idGuru,
            @Field("id_murid") Integer idMurid
    );

    //Get tanggal pengganti
    @FormUrlEncoded
    @POST("absen/tanggalPengganti")
    Call<ArrayList<TanggalPengganti>> getTanggalPengganti(
            @Field("id_pemesanan") Integer idPemesanan
    );

    @FormUrlEncoded
    @POST("absen/store")
    Call<Integer> createAbsen(
            @Field("id_pemesanan") Integer idPemesanan,
            @Field("id_jadwal_pemesanan_perminggu") Integer idJadwalPemesananPerminggu,
            @Field("waktu_pengganti") String waktu_pengganti
    );

    @FormUrlEncoded
    @POST("absen_pembayaran")
    Call<ArrayList<AbsenPembayaran>> getAbsenPembayaran(
            @Field("id_pemesanan") Integer idPemesanan,
            @Field("id_guru") Integer idGuru,
            @Field("id_murid") Integer idMurid,
            @Field("bulan") Integer bulan,
            @Field("tahun") Integer tahun,
            @Field("unpaid") String unpaid,
            @Field("paid") String paid,
            @Field("distinct") String distinct
    );

    //Get jadwal pemesanan perminggu
    @FormUrlEncoded
    @POST("pemesanan/jadwal/filter")
    Call<ArrayList<JadwalPemesananPerminggu>> getJadwalPemesananPerminggu(
            @Field("id_pemesanan") Integer idPemesanan,
            @Field("id_guru") Integer idGuru,
            @Field("id_murid") Integer idMurid,
            @Field("status_pemesanan") Integer statusPemesanan
    );

    @FormUrlEncoded
    @POST("pemesanan/jadwal/updateIdEvent")
    Call<Void> updateIdEventJadwalPemesananPerminggu(
            @Field("id_jadwal_pemesanan_perminggu") Integer idJadwalPemesananPerminggu,
            @Field("id_event") Integer idEvent
    );

    //Get jadwal pemesanan perminggu berdasarkan id
    @GET("pemesanan/jadwal/{id}")
    Call<JadwalPemesananPerminggu> getJadwalPemesananPermingguById(
            @Path("id") int id
    );

    //Get pemesanan yang memiliki konflik
    @GET("pemesanan/conflict/{id}")
    Call<ArrayList<Pemesanan>> getConflictedPemesanan(
            @Path("id") int id
    );

    //Get jumlah pemesanan yang memiliki konflik
    @GET("pemesanan/conflict/count/{id}")
    Call<Integer> getCountConflictedPemesanan(
            @Path("id") int id
    );

    @FormUrlEncoded
    @POST("jadwalAvailable/filter")
    Call<ArrayList<JadwalAvailable>> getJadwalAvailable(
            @Field("id_user") Integer id_user,
            @Field("available") Integer available,
            @Field("hari[]") ArrayList<String> hari,
            @Field("start") String start,
            @Field("end") String end
    );

    @FormUrlEncoded
    @POST("jadwalAvailable/update")
    Call<Void> updateJadwalAvailable(
            @Field("id_available[]") ArrayList<Integer> id_available,
            @Field("id_non_available[]") ArrayList<Integer> id_non_available,
            @Field("id_terisi[]") ArrayList<Integer> id_terisi
    );
}
