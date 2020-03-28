package com.example.easyprivateguru.api;

import com.example.easyprivateguru.models.Absen;
import com.example.easyprivateguru.models.Pemesanan;
import com.example.easyprivateguru.models.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
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

    //Get absen berdasarkan id guru
    @GET("absen/guru/{id}")
    Call<ArrayList<Absen>> getAbsenByIdGuru(
            @Path("id") int idGuru
    );
}
