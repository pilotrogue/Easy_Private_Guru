package com.example.easyprivateguru.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.easyprivateguru.EventQueryHandler;
import com.example.easyprivateguru.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.common.api.Scope;

import java.security.Permission;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddEventActivity extends AppCompatActivity {
    private EditText etTitle, etStart, etEnd, etTimeStart, etTimeEnd;
    private Button btnSubmit;
    private Calendar mCalendarStart = Calendar.getInstance();
    private Calendar mCalendarEnd = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        init();

        etStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerStart();
            }
        });

        etTimeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerStart();
            }
        });

        etEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerEnd();
            }
        });

        etTimeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerEnd();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEvent();
            }
        });
    }

    private void init(){
        etTitle = findViewById(R.id.etTitle);
        etStart = findViewById(R.id.etDtStart);
        etEnd = findViewById(R.id.etDtEnd);

        etTimeStart = findViewById(R.id.etTimeStart);
        etTimeEnd = findViewById(R.id.etTimeEnd);

        btnSubmit = findViewById(R.id.btnSubmit);
    }

    private void addEvent(){
        EventQueryHandler eqh = new EventQueryHandler(this);
        eqh.insertEvent(this, mCalendarStart.getTimeInMillis(), mCalendarEnd.getTimeInMillis(), etTitle.getText().toString(), "No description");
//        eqh.insertEvent(this, mCalendarStart.getTimeInMillis(), mCalendarStart.getTimeInMillis() + (90 * 60000), etTitle.getText().toString(), "No description");
    }

    private void showDatePickerStart(){
        DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener(){
            private static final String TAG = "AddEventActivity";
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mCalendarStart.set(Calendar.YEAR, year);
                mCalendarStart.set(Calendar.MONTH, month);
                mCalendarStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "dd MMMM yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                etStart.setText(sdf.format(mCalendarStart.getTime()));
            }
        }, mCalendarStart.get(Calendar.YEAR), mCalendarStart.get(Calendar.MONTH), mCalendarStart.get(Calendar.DAY_OF_MONTH));

        dpd.setTitle("Pilih tanggal mulai");
        dpd.show();
    }

    private void showDatePickerEnd(){
        DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mCalendarEnd.set(Calendar.YEAR, year);
                mCalendarEnd.set(Calendar.MONTH, month);
                mCalendarEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "dd MMMM yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                etEnd.setText(sdf.format(mCalendarEnd.getTime()));
            }
        }, mCalendarEnd.get(Calendar.YEAR), mCalendarEnd.get(Calendar.MONTH), mCalendarEnd.get(Calendar.DAY_OF_MONTH));

        dpd.setTitle("Pilih tanggal akhir");
        dpd.show();
    }

    private void showTimePickerStart(){
        TimePickerDialog tpd = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            private static final String TAG = "AddEventActivity";
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mCalendarStart.set(Calendar.HOUR_OF_DAY, hourOfDay);
                mCalendarStart.set(Calendar.MINUTE, minute);

                String myFormat = "HH:mm"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

                etTimeStart.setText(sdf.format(mCalendarStart.getTime()));
                long startDateLong = mCalendarStart.getTimeInMillis();
                long endDateLong = mCalendarStart.getTimeInMillis() + (90 * 60000);
                Log.d(TAG, "onTimeSet: startDateLong: "+startDateLong);
                Log.d(TAG, "onTimeSet: endDateLong: "+endDateLong);
                Log.d(TAG, "onTimeSet: day of week: "+mCalendarStart.get(Calendar.DAY_OF_WEEK));
                Log.d(TAG, "onTimeSet: day of month: "+mCalendarStart.get(Calendar.DAY_OF_MONTH));
            }
        }, mCalendarStart.get(Calendar.HOUR_OF_DAY), mCalendarStart.get(Calendar.MINUTE), true);

        tpd.setTitle("Pilih waktu");
        tpd.show();
    }

    private void showTimePickerEnd(){
        TimePickerDialog tpd = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mCalendarEnd.set(Calendar.HOUR_OF_DAY, hourOfDay);
                mCalendarEnd.set(Calendar.MINUTE, minute);

                String myFormat = "HH:mm"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

                etTimeEnd.setText(sdf.format(mCalendarEnd.getTime()));
            }
        }, mCalendarEnd.get(Calendar.HOUR_OF_DAY), mCalendarEnd.get(Calendar.MINUTE), true);

        tpd.setTitle("Pilih waktu");
        tpd.show();
    }
}
