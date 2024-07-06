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
    private static final String DATABASE_NAME = "hobbyTrackerDatabase";
    private static final String TABLE_ACTIVITY = "activities";
    private static final String TABLE_PRACTICE_SESSION = "practice_sessions";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_ACTIVITY_ID = "activity_id";
    private static final String KEY_START_TIME = "start_time";
    private static final String KEY_END_TIME = "end_time";
    private static final String KEY_DURATION = "duration";
    private static final String KEY_NOTES = "notes";

    private static final String CREATE_TABLE_ACTIVITY = "CREATE TABLE " + TABLE_ACTIVITY + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_NAME + " TEXT)";

    private static final String CREATE_TABLE_PRACTICE_SESSION = "CREATE TABLE " + TABLE_PRACTICE_SESSION + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_ACTIVITY_ID + " INTEGER, "
            + KEY_START_TIME + " TEXT, "
            + KEY_END_TIME + " TEXT, "
            + KEY_DURATION + " INTEGER, "
            + KEY_NOTES + " TEXT, "
            + "FOREIGN KEY(" + KEY_ACTIVITY_ID + ") REFERENCES " + TABLE_ACTIVITY + "(" + KEY_ID + "))";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ACTIVITY);
        db.execSQL(CREATE_TABLE_PRACTICE_SESSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRACTICE_SESSION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTIVITY);
        // Create tables again
        onCreate(db);
    }

    // CRUD operations (Create, Read, Update, Delete) for "activities" table
    public void addActivity(Activity activity) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, activity.getName());

        // Inserting Row
        db.insert(TABLE_ACTIVITY, null, values);
        db.close(); // Closing database connection
    }

    public Activity getActivity(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ACTIVITY, new String[] { KEY_ID, KEY_NAME }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Activity activity = new Activity(Integer.parseInt(cursor.getString(0)), cursor.getString(1));
        cursor.close();

        return activity;
    }

    public List<Activity> getAllActivities() {
        List<Activity> activityList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_ACTIVITY;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Activity activity = new Activity();
                activity.setId(Integer.parseInt(cursor.getString(0)));
                activity.setName(cursor.getString(1));
                activityList.add(activity);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return activityList;
    }

    public int updateActivity(Activity activity) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, activity.getName());

        // Updating row
        return db.update(TABLE_ACTIVITY, values, KEY_ID + " = ?",
                new String[] { String.valueOf(activity.getId()) });
    }

    public void deleteActivity(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ACTIVITY, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }

    // CRUD operations (Create, Read, Update, Delete) for "practice_sessions" table
    public void addPracticeSession(PracticeSession session) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ACTIVITY_ID, session.getActivityId());
        values.put(KEY_START_TIME, session.getStartTime());
        values.put(KEY_END_TIME, session.getEndTime());
        values.put(KEY_DURATION, session.getDuration());
        values.put(KEY_NOTES, session.getNotes());

        // Inserting Row
        db.insert(TABLE_PRACTICE_SESSION, null, values);
        db.close(); // Closing database connection
    }

    public List<PracticeSession> getAllSessionsForActivity(int activityId) {
        List<PracticeSession> sessionList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PRACTICE_SESSION + " WHERE " + KEY_ACTIVITY_ID + "=" + activityId;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PracticeSession session = new PracticeSession();
                session.setId(Integer.parseInt(cursor.getString(0)));
                session.setActivityId(cursor.getInt(cursor.getColumnIndex(KEY_ACTIVITY_ID)));
                session.setStartTime(cursor.getString(cursor.getColumnIndex(KEY_START_TIME)));
                session.setEndTime(cursor.getString(cursor.getColumnIndex(KEY_END_TIME)));
                session.setDuration(cursor.getInt(cursor.getColumnIndex(KEY_DURATION)));
                session.setNotes(cursor.getString(cursor.getColumnIndex(KEY_NOTES)));
                sessionList.add(session);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return sessionList;
    }
}
