package com.easyprivate.easyprivateguru;

import android.app.ProgressDialog;
import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;
import android.widget.Toast;

import com.easyprivate.easyprivateguru.api.ApiInterface;
import com.easyprivate.easyprivateguru.api.RetrofitClientInstance;
import com.easyprivate.easyprivateguru.models.User;

import java.util.Calendar;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Response;

public class EventQueryHandler extends AsyncQueryHandler{
    private Context mContext;
    private UserHelper uh;
    private EventQueryHandler eventsQueryHandler;
    private static final String TAG = "EventsQueryHandler";

    private RetrofitClientInstance rci = new RetrofitClientInstance();
    private ApiInterface apiInterface = rci.getApiInterface();
    private ProgressDialog progressDialog;

    private User muridUser;
    private Integer idJadwalPemesananPerminggu;

    public static final String DEFAULT_TITLE = "Mengajar";
    public static final String DEFAULT_DESCRIPTION = "Les Easy Private";

    // Projection arrays
    private static final String[] CALENDAR_PROJECTION = new String[]
            {
                    CalendarContract.Calendars._ID,
                    CalendarContract.Calendars.ACCOUNT_NAME,
                    CalendarContract.Calendars.ACCOUNT_TYPE,
                    CalendarContract.Calendars.OWNER_ACCOUNT
            };

    // The indices for the projection array above.
    private static final int CALENDAR_ID_INDEX = 0;
    private static final int CALENDAR_ACCOUNT_NAME_INDEX = 1;
    private static final int CALENDAR_ACCOUNT_TYPE_INDEX = 2;
    private static final int CALENDAR_OWNER_ACCOUNT_INDEX = 3;

    private static final int CALENDAR = 0;
    private static final int EVENT    = 1;
    private static final int REMINDER = 2;
    private static final int ATTENDEE = 3;
    private static final int GET_EVENT = 4;
    private static final int UPDATE_EVENT = 5;

    private static final String FREQ_RULE = "FREQ=";
    private static final String COUNT_RULE = "COUNT=";
    private static final String WKST_RULE = "WKST=";
    private static final String BYDAY_RULE = "BYDAY=";
    private static final String INTERVAL_RULE = "INTERVAL=";
    private static final String UNTIL_RULE = "UNTIL=";

    public EventQueryHandler(Context mContext) {
        super(mContext.getContentResolver());
        this.mContext = mContext;
        this.uh = new UserHelper(mContext);
    }

    public EventQueryHandler(Context mContext, User muridUser, Integer idJadwalPemesananPerminggu) {
        super(mContext.getContentResolver());
        this.mContext = mContext;
        this.uh = new UserHelper(mContext);
        this.muridUser = muridUser;
        this.idJadwalPemesananPerminggu = idJadwalPemesananPerminggu;
    }

    public void insertEvent(Context context, long dtStart, long dtEnd, String title, String description){
        Log.d(TAG, "insertEvent: called");

        if (eventsQueryHandler == null){
            eventsQueryHandler = new EventQueryHandler(context);
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(CalendarContract.Events.DTSTART, dtStart);
        contentValues.put(CalendarContract.Events.DTEND, dtEnd);
        contentValues.put(CalendarContract.Events.TITLE, title);
        contentValues.put(CalendarContract.Events.DESCRIPTION, description);

        Log.d(TAG, "insertEvent: dtStart: "+dtStart);
        Log.d(TAG, "insertEvent: dtEnd: "+dtEnd);
        Log.d(TAG, "insertEvent: title: "+title);
        Log.d(TAG, "insertEvent: description: "+description);

        eventsQueryHandler.startQuery(CALENDAR, contentValues, CalendarContract.Calendars.CONTENT_URI, CALENDAR_PROJECTION, null, null, null);
    }

    public void insertEvent(Context context, long dtStart, String title, String description, String eventLocation){
        Log.d(TAG, "insertEvent: called");

        if (eventsQueryHandler == null){
            eventsQueryHandler = new EventQueryHandler(context, getMuridUser(), getIdJadwalPemesananPerminggu());
        }

//        String rRule = FREQ_RULE+"WEEKLY;"+WKST_RULE+codeHari+";"+BYDAY_RULE+codeHari;
        String rRule = FREQ_RULE+"WEEKLY;"+INTERVAL_RULE+"1";
        long dtEnd = dtStart + (90*60*1000);

        Log.d(TAG, "insertEvent: rRule: "+rRule);
        Log.d(TAG, "insertEvent: dtStart: "+dtStart);
        Log.d(TAG, "insertEvent: dtEnd: "+dtEnd);
        Log.d(TAG, "insertEvent: title: "+title);
        Log.d(TAG, "insertEvent: description: "+description);
        Log.d(TAG, "insertEvent: eventLocation: "+eventLocation);

        ContentValues contentValues = new ContentValues();
        contentValues.put(CalendarContract.Events.DTSTART, dtStart);
        contentValues.put(CalendarContract.Events.DTEND, dtEnd);
        contentValues.put(CalendarContract.Events.TITLE, title);
        contentValues.put(CalendarContract.Events.DESCRIPTION, description);
        contentValues.put(CalendarContract.Events.EVENT_LOCATION, eventLocation);
        contentValues.put(CalendarContract.Events.RRULE, rRule);

        User currUser = uh.retrieveUser();
        String userEmail = currUser.getEmail();
        String selectionStr = CalendarContract.Calendars.ACCOUNT_NAME+"= ? AND "+CalendarContract.Calendars.OWNER_ACCOUNT+"= ? ";
        String[] selectionArgs = {userEmail, userEmail};

        eventsQueryHandler.startQuery(CALENDAR, contentValues, CalendarContract.Calendars.CONTENT_URI, CALENDAR_PROJECTION, selectionStr, selectionArgs, null);
    }

    public void updateStopEvent(Context context, int eventId){
        Log.d(TAG, "updateStopEvent: called");
        CustomUtility cu = new CustomUtility(mContext);

        if (eventsQueryHandler == null){
            eventsQueryHandler = new EventQueryHandler(context, getMuridUser(), getIdJadwalPemesananPerminggu());
        }

        Calendar myCalendar = Calendar.getInstance();
        String calendarStr = myCalendar.getTime().toString();
        Log.d(TAG, "updateStopEvent: calendarStr: "+calendarStr);

        calendarStr = cu.reformatDateTime(calendarStr, "EEE MMM dd HH:mm:ss z yyyy", "yyyyMMdd");

        String rRule = FREQ_RULE+"WEEKLY;"+INTERVAL_RULE+"1;"+UNTIL_RULE+calendarStr;

        Log.d(TAG, "updateStopEvent: rRule: "+rRule);

        ContentValues contentValues = new ContentValues();
        contentValues.put(CalendarContract.Events._ID, eventId);
        contentValues.put(CalendarContract.Events.RRULE, rRule);

        User currUser = uh.retrieveUser();
        String userEmail = currUser.getEmail();
        String selectionStr = CalendarContract.Calendars.ACCOUNT_NAME+"= ? AND "+CalendarContract.Calendars.OWNER_ACCOUNT+"= ? ";
        String[] selectionArgs = {userEmail, userEmail};

        eventsQueryHandler.startQuery(UPDATE_EVENT, contentValues, CalendarContract.Calendars.CONTENT_URI, CALENDAR_PROJECTION, selectionStr, selectionArgs, null);
    }

    private String getHariCode(String hari){
        String codeHari = "";
        switch (hari.toLowerCase()){
            case "senin":
                codeHari = "MO";
                break;
            case "selasa":
                codeHari = "TU";
                break;
            case "rabu":
                codeHari = "WE";
                break;
            case "kamis":
                codeHari = "TH";
                break;
            case "jumat":
                codeHari = "FR";
                break;
            case "sabtu":
                codeHari = "SA";
                break;
            case "minggu":
                codeHari = "SU";
                break;
            default:
                codeHari = "";
                break;
        }
        return codeHari;
    }

    @Override
    protected void onQueryComplete(int token, Object object, Cursor cursor) {
//        super.onQueryComplete(token, object, cursor);
        Log.d(TAG, "onQueryComplete: called");
        Log.d(TAG, "onQueryComplete: token: "+token);
        User currUser = uh.retrieveUser();
        String userEmail = currUser.getEmail();
        cursor.moveToFirst();

        long calendarID = 0;
        String accountName = "";
        String accountType = "";
        String ownerName = "";

//        while(cursor.moveToNext()){
            calendarID = cursor.getLong(CALENDAR_ID_INDEX);
            accountName = cursor.getString(CALENDAR_ACCOUNT_NAME_INDEX);
            accountType = cursor.getString(CALENDAR_ACCOUNT_TYPE_INDEX);
            ownerName = cursor.getString(CALENDAR_OWNER_ACCOUNT_INDEX);

            Log.d(TAG, "onQueryComplete: position: " + cursor.getPosition());
            Log.d(TAG, "onQueryComplete: calendarID: "+calendarID);
            Log.d(TAG, "onQueryComplete: accountName: "+accountName);
            Log.d(TAG, "onQueryComplete: accountType: "+accountType);
            Log.d(TAG, "onQueryComplete: ownerName: "+ownerName);

//            if(accountName.equals(userEmail) && ownerName.equals(userEmail)){
//                break;
//            }
//        }

        if(calendarID != 0 && !accountName.equals("") && !accountType.equals("") && !ownerName.equals("")){
            ContentValues contentValues = (ContentValues) object;

            progressDialog = rci.getProgressDialog(mContext);
//            progressDialog.show();
            switch(token){
                case CALENDAR:
                    contentValues.put(CalendarContract.Events.CALENDAR_ID, calendarID);
                    contentValues.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getDisplayName());
                    startInsert(EVENT, null, CalendarContract.Events.CONTENT_URI, contentValues);
                    break;
                case UPDATE_EVENT:
                    String eventId = contentValues.get(CalendarContract.Events._ID).toString();

                    String selectionStr = CalendarContract.Events._ID + " = ? AND "
                            + CalendarContract.Events.CALENDAR_ID + " = ?";
                    String[] selectionArgs = {eventId, String.valueOf(calendarID)};

                    startUpdate(UPDATE_EVENT, null, CalendarContract.Events.CONTENT_URI, contentValues, selectionStr,selectionArgs);
                    break;
            }
        }else{
//            Toast.makeText(mContext, "Account not found", Toast.LENGTH_LONG).show();
            Log.d(TAG, "onQueryComplete: account not found");
        }
    }

    @Override
    protected void onInsertComplete(int token, Object object, Uri uri) {
//        super.onInsertComplete(token, object, uri);
        User currUser = uh.retrieveUser();
        Toast.makeText(mContext, "Jadwal berhasil dimasukkan", Toast.LENGTH_LONG).show();
        if (uri != null)
        {
            Log.d(TAG, "onInsertComplete: Insert complete " + uri.getLastPathSegment());
            long eventID = Long.parseLong(uri.getLastPathSegment());
            Integer eventIdInteger = (int) eventID;

            switch (token)
            {
                case EVENT:
                    ContentValues values = new ContentValues();
                    values.put(CalendarContract.Reminders.MINUTES, 30);
                    values.put(CalendarContract.Reminders.EVENT_ID, eventID);
                    values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
                    startInsert(REMINDER, null, CalendarContract.Reminders.CONTENT_URI, values);

                    if(getMuridUser() != null){
                        Log.d(TAG, "onInsertComplete: murid is not null");
                        ContentValues values1 = new ContentValues();
                        values1.put(CalendarContract.Attendees.ATTENDEE_EMAIL, muridUser.getEmail());
                        values1.put(CalendarContract.Attendees.ATTENDEE_NAME, muridUser.getName());
                        values1.put(CalendarContract.Attendees.EVENT_ID, eventID);
                        values1.put(CalendarContract.Attendees.ATTENDEE_STATUS, CalendarContract.Attendees.ATTENDEE_STATUS_ACCEPTED);
                        startInsert(ATTENDEE, null, CalendarContract.Attendees.CONTENT_URI, values1);
                    }else{
                        Log.d(TAG, "onInsertComplete: murid is null");
                    }
                    callUpdateIdEventJadwalPemesananPerminggu(getIdJadwalPemesananPerminggu(), eventIdInteger);
                    break;
                case UPDATE_EVENT:
//                    progressDialog.dismiss();
                    break;
            }
        }
    }

    private void callUpdateIdEventJadwalPemesananPerminggu(Integer idJadwalPemesananPerminggu, Integer idEvent){
        Log.d(TAG, "callUpdateIdEventJadwalPemesananPerminggu: idJadwalPemesananPerminggu: "+idJadwalPemesananPerminggu);
        Log.d(TAG, "callUpdateIdEventJadwalPemesananPerminggu: idEvent: "+idEvent);
        Call<Void> call = apiInterface.updateIdEventJadwalPemesananPerminggu(idJadwalPemesananPerminggu, idEvent);

        call.enqueue(new retrofit2.Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
//                progressDialog.dismiss();
                Log.d(TAG, "onResponse: "+response.message());
                if(!response.isSuccessful()){
                    return;
                }

                Toast.makeText(mContext, "Jadwal telah ditambahkan ke dalam Google Calendar", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
//                progressDialog.dismiss();
                Toast.makeText(mContext, "Oops! Jadwal gagal ditambahkan ke dalam Google Calendar", Toast.LENGTH_LONG).show();
                Log.d(TAG, "onFailure: "+t.getMessage());
                t.printStackTrace();
            }
        });
    }

    public User getMuridUser() {
        return muridUser;
    }

    public void setMuridUser(User muridUser) {
        this.muridUser = muridUser;
    }

    public Integer getIdJadwalPemesananPerminggu() {
        return idJadwalPemesananPerminggu;
    }

    public void setIdJadwalPemesananPerminggu(Integer idJadwalPemesananPerminggu) {
        this.idJadwalPemesananPerminggu = idJadwalPemesananPerminggu;
    }
}
