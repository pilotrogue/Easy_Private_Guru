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

import com.example.easyprivateguru.DummyGenerator;
import com.example.easyprivateguru.R;
import com.example.easyprivateguru.adapters.JadwalRVAdapter;
import com.example.easyprivateguru.models.Jadwal;

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

        ArrayList<Jadwal> jadwals = new ArrayList<>();
        jadwals = getJadwals();

        JadwalRVAdapter adapter = new JadwalRVAdapter(this, jadwals);
        rvJadwal.setAdapter(adapter);
        rvJadwal.setLayoutManager(new LinearLayoutManager(this));
    }

    private ArrayList<Jadwal> getJadwals(){
        Log.d(TAG, "getJadwals: called");
        int idIndex = mCursor.getColumnIndex(CalendarContract.Events._ID),
                titleIndex = mCursor.getColumnIndex(CalendarContract.Events.TITLE),
                descriptionIndex = mCursor.getColumnIndex(CalendarContract.Events.DESCRIPTION),
                eventLocationIndex = mCursor.getColumnIndex(CalendarContract.Events.EVENT_LOCATION);

        String idStr, titleStr, descriptionStr, eventLocationStr;
        ArrayList<Jadwal> jadwals = new ArrayList<>();
        mCursor.moveToFirst();
        while (mCursor.moveToNext()){
            if (mCursor != null){
                idStr = mCursor.getString(idIndex);
                titleStr = mCursor.getString(titleIndex);
                descriptionStr = mCursor.getString(descriptionIndex);
                eventLocationStr = mCursor.getString(eventLocationIndex);
                Log.d(TAG, "getJadwals: idStr: "+idStr);

                Jadwal jadwal = new Jadwal(idStr, titleStr, descriptionStr, eventLocationStr);
                jadwals.add(jadwal);
            }else {
                Log.d(TAG, "getJadwals: No events");
                Jadwal jadwal = new Jadwal("0", "No event", "No event", "No event");
                jadwals.add(jadwal);
            }
        }

        return jadwals;
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
