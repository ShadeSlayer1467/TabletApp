package com.github.shadeslayer1467.ui.hobbytracker.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.shadeslayer1467.R;
import com.github.shadeslayer1467.ui.hobbytracker.databases.EventDatabase;
import com.github.shadeslayer1467.ui.hobbytracker.models.EventModel;
import com.github.shadeslayer1467.ui.hobbytracker.EditHobbyFragment;
import com.github.shadeslayer1467.MainActivity;

import java.util.List;

public class HobbyAdapter extends RecyclerView.Adapter<HobbyAdapter.ViewHolder> {

    private EventDatabase db;
    private List<EventModel> hobbyList;
    private final MainActivity activity;

    public HobbyAdapter(EventDatabase db, MainActivity activity) {
        this.db = db;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_hobbytracker_hobby_cardview, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EventModel hobby = hobbyList.get(position);
        holder.hobbyTitle.setText(hobby.getEventName());

        // Set total time text
        long totalMS = hobby.getTotalMS();
        int hours = (int) (totalMS / (1000 * 60 * 60));
        int minutes = (int) ((totalMS / (1000 * 60)) % 60);
        holder.hobbyTotalTime.setText(String.format("%d hrs %d mins", hours, minutes));

        // Navigate to edit screen when a hobby is clicked
        holder.itemView.setOnClickListener(v -> {
            EditHobbyFragment editHobbyFragment = new EditHobbyFragment(hobby);
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment_content_main, editHobbyFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Override
    public int getItemCount() {
        return hobbyList.size();
    }

    public void setHobbies(List<EventModel> hobbyList) {
        this.hobbyList = hobbyList;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        EventModel item = hobbyList.get(position);
        db.deleteEvent(item.getEventId());
        hobbyList.remove(position);
        notifyItemRemoved(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView hobbyTitle, hobbyTotalTime;

        public ViewHolder(View view) {
            super(view);
            hobbyTitle = view.findViewById(R.id.tvHobbyName);
            hobbyTotalTime = view.findViewById(R.id.tvTotalTime);
        }
    }
}
