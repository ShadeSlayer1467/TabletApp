package shadeslayer.omniapp.hobbytracker.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import shadeslayer.omniapp.R;
import shadeslayer.omniapp.hobbytracker.Models.Activity;
import shadeslayer.omniapp.hobbytracker.Models.PracticeSession;
import shadeslayer.omniapp.hobbytracker.Utils.DatabaseHandler;

public class ActivitiesAdapter extends RecyclerView.Adapter<ActivitiesAdapter.ActivityViewHolder> {

    private List<Activity> activities;
    private Context context;

    public ActivitiesAdapter(List<Activity> activities, Context context) {
        this.activities = activities;
        this.context = context;
    }

    @NonNull
    @Override
    public ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activity, parent, false);
        return new ActivityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityViewHolder holder, int position) {
        Activity activity = activities.get(position);
        holder.activityNameTextView.setText(activity.getName());

        holder.activityNameTextView.setOnClickListener(v -> {
            boolean isVisible = holder.sessionsRecyclerView.getVisibility() == View.VISIBLE;
            holder.sessionsRecyclerView.setVisibility(isVisible ? View.GONE : View.VISIBLE);

            if (!isVisible) {
                DatabaseHandler dbHandler = new DatabaseHandler(context);
                List<PracticeSession> sessions = dbHandler.getAllSessionsForActivity(activity.getId());
                SessionsAdapter adapter = new SessionsAdapter(sessions);
                holder.sessionsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                holder.sessionsRecyclerView.setAdapter(adapter);
            }
        });
    }

    @Override
    public int getItemCount() {
        return activities.size();
    }

    static class ActivityViewHolder extends RecyclerView.ViewHolder {
        TextView activityNameTextView;
        RecyclerView sessionsRecyclerView;

        ActivityViewHolder(View itemView) {
            super(itemView);
            activityNameTextView = itemView.findViewById(R.id.activityNameTextView);
            sessionsRecyclerView = itemView.findViewById(R.id.sessionsRecyclerView);
        }
    }
}