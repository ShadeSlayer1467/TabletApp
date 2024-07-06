package shadeslayer.omniapp.hobbytracker;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import shadeslayer.omniapp.R;
import shadeslayer.omniapp.hobbytracker.Adapters.ActivitiesAdapter;
import shadeslayer.omniapp.hobbytracker.Models.Activity;
import shadeslayer.omniapp.hobbytracker.Utils.DatabaseHandler;

public class HobbyTracker extends Fragment {
    public static final String TAG = "hobbyTrackerFragment";

    private RecyclerView activitiesRecyclerView;
    private DatabaseHandler dbHandler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hobbytracker, container, false);
        activitiesRecyclerView = view.findViewById(R.id.activitiesRecyclerView);
        activitiesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dbHandler = new DatabaseHandler(getContext());
        List<Activity> activities = dbHandler.getAllActivities();
        ActivitiesAdapter adapter = new ActivitiesAdapter(activities, getContext());
        activitiesRecyclerView.setAdapter(adapter);

        return view;
    }
}