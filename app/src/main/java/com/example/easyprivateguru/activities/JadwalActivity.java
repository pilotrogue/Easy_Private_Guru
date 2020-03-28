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
import android.util.Log;
import android.widget.Toast;

import com.example.easyprivateguru.R;
import com.example.easyprivateguru.adapters.JadwalRVAdapter;
import com.example.easyprivateguru.models.JadwalAjar;

import java.util.ArrayList;

public class JadwalActivity extends AppCompatActivity {
    private RecyclerView rvJadwal;
    private Cursor mCursor;
    private static final String TAG = "JadwalActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jadwal);

        init();

        ArrayList<JadwalAjar> jadwalAjars = new ArrayList<>();
        jadwalAjars = getJadwals();

        JadwalRVAdapter adapter = new JadwalRVAdapter(this, jadwalAjars);
        rvJadwal.setAdapter(adapter);
        rvJadwal.setLayoutManager(new LinearLayoutManager(this));
    }

    private ArrayList<JadwalAjar> getJadwals(){
        Log.d(TAG, "getJadwals: called");
        int idIndex = mCursor.getColumnIndex(CalendarContract.Events._ID),
                titleIndex = mCursor.getColumnIndex(CalendarContract.Events.TITLE),
                descriptionIndex = mCursor.getColumnIndex(CalendarContract.Events.DESCRIPTION),
                eventLocationIndex = mCursor.getColumnIndex(CalendarContract.Events.EVENT_LOCATION);

        String idStr, titleStr, descriptionStr, eventLocationStr;
        ArrayList<JadwalAjar> jadwalAjars = new ArrayList<>();
        mCursor.moveToFirst();
        while (mCursor.moveToNext()){
            if (mCursor != null){
                idStr = mCursor.getString(idIndex);
                titleStr = mCursor.getString(titleIndex);
                descriptionStr = mCursor.getString(descriptionIndex);
                eventLocationStr = mCursor.getString(eventLocationIndex);
                Log.d(TAG, "getJadwals: idStr: "+idStr);

                JadwalAjar jadwalAjar = new JadwalAjar(Integer.parseInt(idStr));
                jadwalAjars.add(jadwalAjar);
            }else {
                Log.d(TAG, "getJadwals: No events");
                JadwalAjar jadwalAjar = new JadwalAjar(0);
                jadwalAjars.add(jadwalAjar);
            }
        }

        return jadwalAjars;
    }

    private Cursor getCursor(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "getCursor: called");
            Cursor cursor = getContentResolver().query(CalendarContract.Events.CONTENT_URI, null, null, null, null);
            return cursor;
        }else{
            Log.d(TAG, "getCursor: no permission!");
            Toast.makeText(this, "No permission!", Toast.LENGTH_LONG).show();
            return null;
        }
    }

    private void init() {
        rvJadwal = findViewById(R.id.rvJadwal);
        mCursor = getCursor();
    }
}
