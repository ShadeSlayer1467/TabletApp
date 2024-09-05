package com.github.shadeslayer1467.ui.hobbytracker.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.github.shadeslayer1467.ui.hobbytracker.models.EventModel;
import com.github.shadeslayer1467.ui.hobbytracker.models.EventSessionModel;

import java.util.ArrayList;
import java.util.List;

public class LocalEventDatabase extends SQLiteOpenHelper implements EventDatabase {

    private static final int VERSION = 1;
    private static final String NAME = "HobbyTrackerDatabase";

    // Table Names
    private static final String EVENTS_TABLE = "events";
    private static final String SESSIONS_TABLE = "event_sessions";
    private static final String CATEGORIES_TABLE = "event_categories";

    // Common Column Names
    private static final String ID = "id";

    // Events Table - column names
    private static final String EVENT_NAME = "event_name";
    private static final String CATEGORY_ID = "category_id";
    private static final String CREATED_AT = "created_at";
    private static final String UPDATED_AT = "updated_at";
    private static final String TOTAL_MS = "total_ms";

    // EventSessions Table - column names
    private static final String EVENT_ID = "event_id";
    private static final String START_TIME = "start_time";
    private static final String END_TIME = "end_time";
    private static final String DURATION = "duration";
    private static final String DELETED = "deleted";

    // EventCategories Table - column names
    private static final String CATEGORY_NAME = "category_name";

    // Table Create Statements
    private static final String CREATE_EVENTS_TABLE = "CREATE TABLE " + EVENTS_TABLE + "(" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            EVENT_NAME + " TEXT, " +
            CATEGORY_ID + " INTEGER, " +
            CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
            UPDATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
            TOTAL_MS + " INTEGER DEFAULT 0, " +
            "FOREIGN KEY(" + CATEGORY_ID + ") REFERENCES " + CATEGORIES_TABLE + "(" + ID + ")" +
            ")";

    private static final String CREATE_SESSIONS_TABLE = "CREATE TABLE " + SESSIONS_TABLE + "(" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            EVENT_ID + " INTEGER, " +
            START_TIME + " DATETIME, " +
            END_TIME + " DATETIME, " +
            DURATION + " INTEGER, " +
            DELETED + " INTEGER, " +
            "FOREIGN KEY(" + EVENT_ID + ") REFERENCES " + EVENTS_TABLE + "(" + ID + ")" +
            ")";

    private static final String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + CATEGORIES_TABLE + "(" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CATEGORY_NAME + " TEXT" +
            ")";

    private SQLiteDatabase db;

    public LocalEventDatabase(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_EVENTS_TABLE);
        db.execSQL(CREATE_SESSIONS_TABLE);
        db.execSQL(CREATE_CATEGORIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + EVENTS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SESSIONS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CATEGORIES_TABLE);
        onCreate(db);
    }

    @Override
    public void openDatabase() {
        db = this.getWritableDatabase();
    }

    @Override
    public String getCategory(int categoryID) {
        String categoryName = null;
        Cursor cursor = null;
        try {
            cursor = db.query(CATEGORIES_TABLE, new String[]{CATEGORY_NAME}, ID + "=?", new String[]{String.valueOf(categoryID)}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                categoryName = cursor.getString(cursor.getColumnIndexOrThrow(CATEGORY_NAME));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return categoryName;
    }

    @Override
    public void createEvent(EventModel event) {
        ContentValues cv = new ContentValues();
        cv.put(EVENT_NAME, event.getEventName());
        cv.put(CATEGORY_ID, event.getCategoryId());
        cv.put(CREATED_AT, event.getCreatedAt());
        cv.put(UPDATED_AT, event.getUpdatedAt());
        cv.put(TOTAL_MS, event.getTotalMS());
        db.insert(EVENTS_TABLE, null, cv);
    }

    @Override
    public EventModel getEvent(int eventId) {
        EventModel event = null;
        Cursor cur = null;

        try {
            cur = db.query(EVENTS_TABLE, null, ID + "=?", new String[]{String.valueOf(eventId)}, null, null, null);

            if (cur != null && cur.moveToFirst()) {
                // Use getColumnIndexOrThrow for better error handling
                int idIndex = cur.getColumnIndexOrThrow(ID);
                int eventNameIndex = cur.getColumnIndexOrThrow(EVENT_NAME);
                int categoryIdIndex = cur.getColumnIndexOrThrow(CATEGORY_ID);
                int createdAtIndex = cur.getColumnIndexOrThrow(CREATED_AT);
                int updatedAtIndex = cur.getColumnIndexOrThrow(UPDATED_AT);
                int totalMSIndex = cur.getColumnIndexOrThrow(TOTAL_MS);

                event = new EventModel(
                        cur.getInt(idIndex),
                        cur.getString(eventNameIndex),
                        cur.getInt(categoryIdIndex),
                        null,
                        cur.getString(createdAtIndex),
                        cur.getString(updatedAtIndex),
                        cur.getLong(totalMSIndex)
                );
            }
        } catch (Exception e) {
            e.printStackTrace();  // Log the exception to help with debugging
        } finally {
            if (cur != null) {
                cur.close();
            }
        }

        return event;
    }

    @Override
    public List<EventModel> getAllEvents() {
        List<EventModel> eventList = new ArrayList<>();
        Cursor cur = null;

        try {
            cur = db.query(EVENTS_TABLE, null, null, null, null, null, null);

            if (cur != null && cur.moveToFirst()) {
                do {
                    int idIndex = cur.getColumnIndexOrThrow(ID);
                    int eventNameIndex = cur.getColumnIndexOrThrow(EVENT_NAME);
                    int categoryIdIndex = cur.getColumnIndexOrThrow(CATEGORY_ID);
                    int createdAtIndex = cur.getColumnIndexOrThrow(CREATED_AT);
                    int updatedAtIndex = cur.getColumnIndexOrThrow(UPDATED_AT);
                    int totalMSIndex = cur.getColumnIndexOrThrow(TOTAL_MS);

                    EventModel event = new EventModel(
                            cur.getInt(idIndex),
                            cur.getString(eventNameIndex),
                            cur.getInt(categoryIdIndex),
                            null,
                            cur.getString(createdAtIndex),
                            cur.getString(updatedAtIndex),
                            cur.getLong(totalMSIndex)
                    );
                    eventList.add(event);
                } while (cur.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();  // Log the exception to help with debugging
        } finally {
            if (cur != null) {
                cur.close();
            }
        }

        return eventList;
    }
    @Override
    public EventSessionModel getSession(int sessionId) {
        EventSessionModel session = null;
        Cursor cur = null;

        try {
            cur = db.query(SESSIONS_TABLE, null, ID + "=?", new String[]{String.valueOf(sessionId)}, null, null, null);

            if (cur != null && cur.moveToFirst()) {
                int idIndex = cur.getColumnIndexOrThrow(ID);
                int eventIdIndex = cur.getColumnIndexOrThrow(EVENT_ID);
                int startTimeIndex = cur.getColumnIndexOrThrow(START_TIME);
                int endTimeIndex = cur.getColumnIndexOrThrow(END_TIME);
                int durationIndex = cur.getColumnIndexOrThrow(DURATION);
                int deletedIndex = cur.getColumnIndexOrThrow(DELETED);

                session = new EventSessionModel(
                        cur.getInt(idIndex),
                        cur.getInt(eventIdIndex),
                        cur.getString(startTimeIndex),
                        cur.getString(endTimeIndex),
                        cur.getInt(durationIndex),
                        (cur.getInt(deletedIndex) != 0) // if 0, deleted = false
                );
            }
        } catch (Exception e) {
            e.printStackTrace();  // Log the exception to help with debugging
        } finally {
            if (cur != null) {
                cur.close();
            }
        }

        return session;
    }
    @Override
    public void updateEvent(EventModel event) {
        ContentValues cv = new ContentValues();
        cv.put(EVENT_NAME, event.getEventName());
        cv.put(CATEGORY_ID, event.getCategoryId());
        cv.put(UPDATED_AT, event.getUpdatedAt());
        cv.put(TOTAL_MS, event.getTotalMS());
        db.update(EVENTS_TABLE, cv, ID + "=?", new String[]{String.valueOf(event.getEventId())});
    }
    @Override
    public void deleteEvent(int eventId) {
        db.beginTransaction();
        try {
            ContentValues sessionValues = new ContentValues();
            sessionValues.put(DELETED, 1);
            db.update(SESSIONS_TABLE, sessionValues, "event_id=?", new String[]{String.valueOf(eventId)});

            db.delete(EVENTS_TABLE, ID + "=?", new String[]{String.valueOf(eventId)});

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void createSession(EventSessionModel session) {
        ContentValues cv = new ContentValues();
        cv.put(EVENT_ID, session.getEventId());
        cv.put(START_TIME, session.getStartTime());
        cv.put(END_TIME, session.getEndTime());
        cv.put(DURATION, session.getDuration());
        db.insert(SESSIONS_TABLE, null, cv);
    }
    @Override
    public List<EventSessionModel> getSessionsForEvent(int eventId, boolean getDeleted) {
        List<EventSessionModel> sessionList = new ArrayList<>();
        Cursor cur = null;

        try {
            if (getDeleted)cur = db.query(SESSIONS_TABLE, null, EVENT_ID + "=?", new String[]{String.valueOf(eventId)}, null, null, null);
            else cur = db.query(SESSIONS_TABLE, null, EVENT_ID + "=? AND deleted=0", new String[]{String.valueOf(eventId)}, null, null, null);

            if (cur != null && cur.moveToFirst()) {
                do {
                    int idIndex = cur.getColumnIndexOrThrow(ID);
                    int eventIdIndex = cur.getColumnIndexOrThrow(EVENT_ID);
                    int startTimeIndex = cur.getColumnIndexOrThrow(START_TIME);
                    int endTimeIndex = cur.getColumnIndexOrThrow(END_TIME);
                    int durationIndex = cur.getColumnIndexOrThrow(DURATION);
                    int deletedIndex = cur.getColumnIndexOrThrow(DELETED);

                    EventSessionModel session = new EventSessionModel(
                            cur.getInt(idIndex),
                            cur.getInt(eventIdIndex),
                            cur.getString(startTimeIndex),
                            cur.getString(endTimeIndex),
                            cur.getInt(durationIndex),
                            (cur.getInt(deletedIndex) != 0)
                    );
                    sessionList.add(session);
                } while (cur.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();  // Log the exception to help with debugging
        } finally {
            if (cur != null) {
                cur.close();
            }
        }

        return sessionList;
    }
    @Override
    public void updateSession(EventSessionModel session) {
        ContentValues cv = new ContentValues();
        cv.put(START_TIME, session.getStartTime());
        cv.put(END_TIME, session.getEndTime());
        cv.put(DURATION, session.getDuration());
        cv.put(DELETED, session.isDeleted());
        db.update(SESSIONS_TABLE, cv, ID + "=?", new String[]{String.valueOf(session.getSessionId())});
    }
    @Override
    public void deleteSession(int sessionId) {
        db.delete(SESSIONS_TABLE, ID + "=?", new String[]{String.valueOf(sessionId)});
    }
    public void markSessionAsDeleted(int sessionId) {
        ContentValues values = new ContentValues();
        values.put("deleted", 1); // Mark session as deleted
        db.update("event_sessions", values, "session_id=?", new String[]{String.valueOf(sessionId)});
    }
}