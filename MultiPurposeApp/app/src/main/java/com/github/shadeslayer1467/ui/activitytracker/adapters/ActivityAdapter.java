package com.github.shadeslayer1467.ui.activitytracker.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.shadeslayer1467.R;
import com.github.shadeslayer1467.ui.activitytracker.databases.EventDatabase;
import com.github.shadeslayer1467.ui.activitytracker.AddNewActivityFragment;
import com.github.shadeslayer1467.ui.activitytracker.models.EventModel;
import com.github.shadeslayer1467.ui.activitytracker.ViewActivityFragment;
import com.github.shadeslayer1467.MainActivity;

import java.util.List;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder> {

    private EventDatabase db;
    private List<EventModel> activities;
    private final MainActivity mainActivity;

    public ActivityAdapter(EventDatabase db, MainActivity mainActivity) {
        this.db = db;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_activitytracker_activity_cardview, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EventModel activity = activities.get(position);
        holder.activityTitle.setText(activity.getEventName());

        // Set total time text
        long totalMS = activity.getTotalMS();
        int hours = (int) (totalMS / (1000 * 60 * 60));
        int minutes = (int) ((totalMS / (1000 * 60)) % 60);
        holder.activityTotalTime.setText(String.format("%d hrs %d mins", hours, minutes));

        // Navigate to edit screen when an Activity is clicked
        holder.itemView.setOnClickListener(v -> {
            ViewActivityFragment editActivityFragment = new ViewActivityFragment(activity);
            mainActivity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment_content_main, editActivityFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Override
    public int getItemCount() {
        return activities.size();
    }

    public void setActivities(List<EventModel> activities) {
        this.activities = activities;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        EventModel item = activities.get(position);
        db.deleteEvent(item.getEventId());
        activities.remove(position);
        notifyItemRemoved(position);
    }

    public Context getContext() {
        return getContext();
    }

    public void editItem(int position) {
        EventModel item = activities.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getEventId());
        bundle.putString("event_name", item.getEventName());
        bundle.putLong("total_ms", item.getTotalMS());
        AddNewActivityFragment fragment = AddNewActivityFragment.newInstance();
        fragment.setArguments(bundle);
        fragment.show(mainActivity.getSupportFragmentManager(), AddNewActivityFragment.TAG);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView activityTitle, activityTotalTime;

        public ViewHolder(View view) {
            super(view);
            activityTitle = view.findViewById(R.id.activity_cardview_activity_name);
            activityTotalTime = view.findViewById(R.id.activity_cardview_activity_total_time);
        }
    }
}
