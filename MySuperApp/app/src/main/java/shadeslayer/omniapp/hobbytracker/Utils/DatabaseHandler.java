package shadeslayer.omniapp.hobbytracker.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import shadeslayer.omniapp.hobbytracker.Models.Activity;
import shadeslayer.omniapp.hobbytracker.Models.PracticeSession;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String NAME = "hobbyPracticeDatabase";
    private static final String ACTIVITY_TABLE = "activities";
    private static final String PRACTICE_TABLE = "practice_sessions";
    private static final String ID = "id";
    private static final String NAME_COLUMN = "name";
    private static final String DESCRIPTION = "description";
    private static final String ACTIVITY_ID = "activity_id";
    private static final String START_TIME = "start_time";
    private static final String END_TIME = "end_time";
    private static final String DURATION = "duration";
    private static final String NOTES = "notes";

    private static final String CREATE_ACTIVITY_TABLE = "CREATE TABLE " + ACTIVITY_TABLE + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + NAME_COLUMN + " TEXT, "
            + DESCRIPTION + " TEXT)";

    private static final String CREATE_PRACTICE_TABLE = "CREATE TABLE " + PRACTICE_TABLE + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ACTIVITY_ID + " INTEGER, "
            + START_TIME + " TEXT, "
            + END_TIME + " TEXT, "
            + DURATION + " INTEGER, "
            + NOTES + " TEXT, "
            + "FOREIGN KEY(" + ACTIVITY_ID + ") REFERENCES " + ACTIVITY_TABLE + "(" + ID + "))";

    public DatabaseHandler(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ACTIVITY_TABLE);
        db.execSQL(CREATE_PRACTICE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PRACTICE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ACTIVITY_TABLE);
        onCreate(db);
    }

    public void insertActivity(Activity activity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME_COLUMN, activity.getName());
        values.put(DESCRIPTION, activity.getDescription());
        db.insert(ACTIVITY_TABLE, null, values);
        db.close();
    }

    public List<Activity> getAllActivities() {
        List<Activity> activities = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + ACTIVITY_TABLE, null);
        if (cursor.moveToFirst()) {
            do {
                Activity activity = new Activity();
                activity.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                activity.setName(cursor.getString(cursor.getColumnIndex(NAME_COLUMN)));
                activity.setDescription(cursor.getString(cursor.getColumnIndex(DESCRIPTION)));
                activities.add(activity);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return activities;
    }

    public void insertSession(PracticeSession session) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ACTIVITY_ID, session.getActivityId());
        values.put(START_TIME, session.getStartTime());
        values.put(END_TIME, session.getEndTime());
        values.put(DURATION, session.getDuration());
        values.put(NOTES, session.getNotes());
        db.insert(PRACTICE_TABLE, null, values);
        db.close();
    }

    public List<PracticeSession> getSessionsForActivity(int activityId) {
        List<PracticeSession> sessions = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + PRACTICE_TABLE + " WHERE " + ACTIVITY_ID + " = " + activityId, null);
        if (cursor.moveToFirst()) {
            do {
                PracticeSession session = new PracticeSession();
                session.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                session.setActivityId(cursor.getInt(cursor.getColumnIndex(ACTIVITY_ID)));
                session.setStartTime(cursor.getString(cursor.getColumnIndex(START_TIME)));
                session.setEndTime(cursor.getString(cursor.getColumnIndex(END_TIME)));
                session.setDuration(cursor.getInt(cursor.getColumnIndex(DURATION)));
                session.setNotes(cursor.getString(cursor.getColumnIndex(NOTES)));
                sessions.add(session);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return sessions;
    }
}
