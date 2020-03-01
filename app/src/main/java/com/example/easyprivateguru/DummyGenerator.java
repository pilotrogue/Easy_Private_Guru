package com.example.easyprivateguru;

import com.example.easyprivateguru.models.Absen;
import com.example.easyprivateguru.models.Jenjang;
import com.example.easyprivateguru.models.MataPelajaran;
import com.example.easyprivateguru.models.Pembayaran;
import com.example.easyprivateguru.models.Pesanan;
import com.example.easyprivateguru.models.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DummyGenerator {
    public ArrayList<Pembayaran> getPembayaranDummy(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date date1 = null, date2 = null, date3 = null;

        try {
            date1 = sdf.parse("2020-01-01");
            date2 = sdf.parse("2020-02-01");
            date3 = sdf.parse("2020-03-01");
        }catch (ParseException e){
            e.getErrorOffset();
        }

        Pembayaran p1 = new Pembayaran(date1, 1000000);
        Pembayaran p2 = new Pembayaran(date2, 1000000);
        Pembayaran p3 = new Pembayaran(date3, 1000000);

        ArrayList<Pembayaran> pembayarans = new ArrayList<>();
        pembayarans.add(p1);
        pembayarans.add(p2);
        pembayarans.add(p3);

        return pembayarans;
    }

    public ArrayList<Absen> getAbsenDummy(){
        ArrayList<Pesanan> pesanans = getPesananDummy();
        ArrayList<Absen> absens = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();

        for (int i = 0; i < pesanans.size(); i++){
            absens.add(new Absen(date, pesanans.get(i)));
        }

        return absens;
    }

    public ArrayList<Pesanan> getPesananDummy(){
        ArrayList<User> gurus = getUserGuruDummy();
        ArrayList<User> murids = getUserMuridDummy();
        ArrayList<MataPelajaran> mataPelajarans = getMataPelajaranDummy();

        ArrayList<Pesanan> pesanans = new ArrayList<>();

        for (int i = 0; i < gurus.size(); i++){
            Pesanan p = new Pesanan(murids.get(i), gurus.get(i), mataPelajarans.get(i));
            pesanans.add(p);
        }

        return pesanans;
    }

    public ArrayList<User> getUserGuruDummy(){
        User user1 = new User("Momo", "momo@email.com", "Jakarta Barat", 2);
        User user2 = new User("Mumu", "mumu@email.com", "Jakarta Utara", 2);
        User user3 = new User("Mimi", "mimi@email.com", "Jakarta Timur", 2);

        ArrayList<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        users.add(user3);

        return users;
    }
    
    public ArrayList<User> getUserMuridDummy(){
        User user1 = new User("Lala", "lala@email.com", "Jakarta Barat", 1);
        User user2 = new User("Lulu", "lulu@email.com", "Jakarta Utara", 1);
        User user3 = new User("Lili", "lili@email.com", "Jakarta Timur", 1);
        
        ArrayList<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        users.add(user3);
        
        return users;
    }
    
    public ArrayList<MataPelajaran> getMataPelajaranDummy(){
        ArrayList<Jenjang> jenjangs = getJenjangDummy();
        MataPelajaran m1 = new MataPelajaran("Matematika", jenjangs.get(0));
        MataPelajaran m2 = new MataPelajaran("IPA", jenjangs.get(1));
        MataPelajaran m3 = new MataPelajaran("Bahasa Inggris", jenjangs.get(2));
        
        ArrayList<MataPelajaran> mataPelajarans = new ArrayList<>();
        mataPelajarans.add(m1);
        mataPelajarans.add(m2);
        mataPelajarans.add(m3);
        
        return mataPelajarans; 
    }
    
    public ArrayList<Jenjang> getJenjangDummy(){
        Jenjang j1 = new Jenjang("SD", 200000, 75000);
        Jenjang j2 = new Jenjang("SMP", 300000, 75000);
        Jenjang j3 = new Jenjang("SD", 350000, 75000);
        
        ArrayList<Jenjang> jenjangs = new ArrayList<>();
        jenjangs.add(j1);
        jenjangs.add(j2);
        jenjangs.add(j3);
        
        return jenjangs;
    }
}
