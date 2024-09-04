package com.github.shadeslayer1467.ui.hobbytracker.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.github.shadeslayer1467.MainActivity;
import com.github.shadeslayer1467.R;
import com.github.shadeslayer1467.ui.hobbytracker.AddNewHobbyFragment;
import com.github.shadeslayer1467.ui.hobbytracker.databases.EventDatabase;
import com.github.shadeslayer1467.ui.hobbytracker.models.EventModel;

public class HobbyAdapter extends RecyclerView.Adapter<HobbyAdapter.ViewHolder> {

    private List<EventModel> hobbyList;
    private MainActivity activity;
    private EventDatabase db;

    public HobbyAdapter(EventDatabase db, MainActivity activity) {
        this.db = db;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_hobbytracker_hobby_cardview, parent, false); // Assuming you have a layout file named fragment_hobby_cardview.xml
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        db.openDatabase();
        EventModel item = hobbyList.get(position);
        holder.hobbyName.setText(item.getEventName());

        // Assuming total time is in milliseconds and converting it to hours:minutes:seconds
        long totalMilliseconds = item.getTotalMS();
        long seconds = (totalMilliseconds / 1000) % 60;
        long minutes = (totalMilliseconds / (1000 * 60)) % 60;
        long hours = totalMilliseconds / (1000 * 60 * 60);
        String totalTime = String.format("%02d:%02d:%02d", hours, minutes, seconds);

        holder.totalTime.setText(totalTime);
    }

    @Override
    public int getItemCount() {
        return hobbyList.size();
    }
    public void setHobbies(List<EventModel> hobbyList) {
        this.hobbyList = hobbyList;
        notifyDataSetChanged();
    }
    public Context getContext() {
        return activity;
    }
    public void deleteItem(int position) {
        EventModel item = hobbyList.get(position);
        db.deleteEvent(item.getEventId());
        hobbyList.remove(position);
        notifyItemRemoved(position);
    }
    public void editItem(int position) {
        EventModel item = hobbyList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getEventId());
        bundle.putString("event_name", item.getEventName());
        bundle.putLong("total_ms", item.getTotalMS());
        AddNewHobbyFragment fragment = AddNewHobbyFragment.newInstance();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewHobbyFragment.TAG);
    }




    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView hobbyName;
        TextView totalTime;

        ViewHolder(View view) {
            super(view);
            hobbyName = view.findViewById(R.id.tvHobbyName); // Assuming you have a TextView with id hobbyNameTextView in your layout
            totalTime = view.findViewById(R.id.tvTotalTime); // Assuming you have a TextView with id totalTimeTextView in your layout
        }
    }
}
