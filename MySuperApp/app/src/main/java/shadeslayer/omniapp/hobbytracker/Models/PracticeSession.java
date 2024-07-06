package shadeslayer.omniapp.hobbytracker.Models;

public class PracticeSession {
    private int id;
    private int activityId;
    private String startTime;
    private String endTime;
    private int duration;  // Duration in minutes
    private String notes;  // Optional notes about the session

    // Constructor
    public PracticeSession() {
    }

    public PracticeSession(int id, int activityId, String startTime, String endTime, int duration, String notes) {
        this.id = id;
        this.activityId = activityId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
        this.notes = notes;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
