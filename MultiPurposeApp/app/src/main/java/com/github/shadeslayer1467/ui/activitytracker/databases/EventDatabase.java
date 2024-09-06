package com.github.shadeslayer1467.ui.activitytracker.databases;

import com.github.shadeslayer1467.ui.activitytracker.models.EventModel;
import com.github.shadeslayer1467.ui.activitytracker.models.EventSessionModel;

import java.util.List;

public interface EventDatabase {
    public void openDatabase();

    String getCategory(int categoryID);

    // Event operations
    void createEvent(EventModel event);
    EventModel getEvent(int eventId);
    List<EventModel> getAllEvents();
    void updateEvent(EventModel event);
    void deleteEvent(int eventId);
    public void updateEventTotalTime(int eventId);
    public void updateAllEventTotalTimes();

    // Event session operations
    void createSession(EventSessionModel session);
    EventSessionModel getSession(int sessionId);
    List<EventSessionModel> getSessionsForEvent(int eventId, boolean getDeleted);
    void updateSession(EventSessionModel session);
    void deleteSession(int sessionId);
    public void markSessionAsDeleted(int sessionId);
}
