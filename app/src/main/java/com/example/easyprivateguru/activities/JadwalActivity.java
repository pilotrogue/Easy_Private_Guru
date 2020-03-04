package com.example.easyprivateguru.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.widget.Toast;

import com.example.easyprivateguru.adapters.AbsenRVAdapter;
import com.example.easyprivateguru.DummyGenerator;
import com.example.easyprivateguru.R;
import com.example.easyprivateguru.models.Jadwal;

import java.util.ArrayList;

public class JadwalActivity extends AppCompatActivity {
    private RecyclerView rvJadwal;
    private Cursor mCursor;
    private CalendarContract.Calendars calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jadwal);

        init();

        DummyGenerator dg = new DummyGenerator();
        AbsenRVAdapter adapter = new AbsenRVAdapter(this, dg.getAbsenDummy());
        rvJadwal.setAdapter(adapter);
        rvJadwal.setLayoutManager(new LinearLayoutManager(this));
    }

    private void showEvents(){
        int idIndex = mCursor.getColumnIndex(CalendarContract.Events._ID),
                titleIndex = mCursor.getColumnIndex(CalendarContract.Events.TITLE),
                descriptionIndex = mCursor.getColumnIndex(CalendarContract.Events.DESCRIPTION),
                eventLocationIndex = mCursor.getColumnIndex(CalendarContract.Events.EVENT_LOCATION);

        String idStr, titleStr, descriptionStr, eventLocationStr;
        ArrayList<Jadwal> jadwals = new ArrayList<>();
        while (mCursor.moveToNext()){
            if (mCursor != null){
                idStr = mCursor.getString(idIndex);
                titleStr = mCursor.getString(titleIndex);
                descriptionStr = mCursor.getString(descriptionIndex);
                eventLocationStr = mCursor.getString(eventLocationIndex);

                Jadwal jadwal = new Jadwal(idStr, titleStr, descriptionStr, eventLocationStr);
                jadwals.add(jadwal);
            }
        }
    }

    private Cursor getCursor(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
            Cursor cursor = getContentResolver().query(CalendarContract.Events.CONTENT_URI, null, null, null, null);
            return cursor;
        }else{
            Toast.makeText(this, "No permission!", Toast.LENGTH_LONG).show();
            return null;
        }
    }

    private void init() {
        rvJadwal = findViewById(R.id.rvJadwal);
        mCursor = getCursor();
    }
}
