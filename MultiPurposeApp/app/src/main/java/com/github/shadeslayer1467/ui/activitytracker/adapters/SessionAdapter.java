package com.github.shadeslayer1467.ui.activitytracker.adapters;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.github.shadeslayer1467.R;
import com.github.shadeslayer1467.ui.activitytracker.SessionActionListener;
import com.github.shadeslayer1467.ui.activitytracker.databases.EventDatabase;
import com.github.shadeslayer1467.ui.activitytracker.databases.LocalEventDatabase;
import com.github.shadeslayer1467.ui.activitytracker.dialogs.AddEditSessionDialog;
import com.github.shadeslayer1467.ui.activitytracker.models.EventSessionModel;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.ViewHolder> {
    private List<EventSessionModel> sessionList;
    private final Fragment parentFragment;

    public SessionAdapter(List<EventSessionModel> sessionList, Fragment parentFragment) {
        this.sessionList = sessionList;
        this.parentFragment = parentFragment; // Used to show the dialog
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_activitytracker_activity_session_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EventSessionModel session = sessionList.get(position);

        // Set session details on the view
        holder.sessionStartTime.setText(formatTime(session.getStartTime()));
        holder.sessionEndTime.setText(formatTime(session.getEndTime()));
        holder.sessionDurationTime.setText(formatDuration(session.getStartTime(), session.getEndTime()));

        holder.deleteSessionButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(parentFragment.getContext());
            builder.setTitle("Delete Session");
            builder.setMessage("Are you sure you want to delete this session?");
            builder.setPositiveButton("Confirm",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            EventDatabase db = new LocalEventDatabase(parentFragment.getContext());
                            db.openDatabase();
                            db.deleteSession(session.getSessionId());
                            sessionList.remove(session);
                            if ((parentFragment) instanceof SessionActionListener) {
                                ((SessionActionListener)parentFragment).onSessionDeleted(session.getSessionId());
                            }
                        }
                    });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            AlertDialog dialog = builder.create();
            dialog.setOnDismissListener(dialogInterface -> {
            });
            dialog.show();
        });
        holder.editSessionButton.setOnClickListener(v -> {
            AddEditSessionDialog dialog = AddEditSessionDialog.newInstance(session, session.getEventId());
            dialog.setTargetFragment(parentFragment, 1);
            dialog.show(parentFragment.getParentFragmentManager(), "EditSessionDialog");
        });
    }

    private String formatTime(String timeInMillis) {
        long time = Long.parseLong(timeInMillis);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault());
        return sdf.format(time);
    }
    private String formatDuration(String startTimeInMillis, String endTimeInMillis) {
        long startTime = Long.parseLong(startTimeInMillis);
        long endTime = Long.parseLong(endTimeInMillis);

        // Calculate duration in milliseconds
        long durationInMillis = endTime - startTime;

        // Convert to hours and minutes
        long minutes = (durationInMillis / (1000 * 60)) % 60;
        long hours = (durationInMillis / (1000 * 60 * 60));

        return String.format(Locale.getDefault(), "%d hrs %d mins", hours, minutes);
    }
    @Override
    public int getItemCount() {
        return sessionList.size();
    }

    public void setSessions(List<EventSessionModel> sessionList) {
        this.sessionList = sessionList;
        notifyDataSetChanged();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView sessionStartTime, sessionEndTime, sessionDurationTime;
        ImageButton editSessionButton, deleteSessionButton;

        public ViewHolder(View view) {
            super(view);
            sessionStartTime = view.findViewById(R.id.fragment_activitytracker_activity_session_item_sessionStartTime);
            sessionEndTime = view.findViewById(R.id.fragment_activitytracker_activity_session_item_sessionEndTime);
            sessionDurationTime = view.findViewById(R.id.fragment_activitytracker_activity_session_item_sessionDuration);
            sessionDurationTime = view.findViewById(R.id.fragment_activitytracker_activity_session_item_sessionDuration);

            editSessionButton = view.findViewById(R.id.fragment_activitytracker_activity_session_item_editSessionButton);
            deleteSessionButton = view.findViewById(R.id.fragment_activitytracker_activity_session_item_deleteSessionButton);
        }
    }
}
