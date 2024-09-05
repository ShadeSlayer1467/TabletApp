package com.github.shadeslayer1467.ui.activitytracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.shadeslayer1467.R;
import com.github.shadeslayer1467.ui.activitytracker.adapters.SessionAdapter;
import com.github.shadeslayer1467.ui.activitytracker.databases.EventDatabase;
import com.github.shadeslayer1467.ui.activitytracker.databases.LocalEventDatabase;
import com.github.shadeslayer1467.ui.activitytracker.dialogs.AddEditSessionDialog;
import com.github.shadeslayer1467.ui.activitytracker.models.EventModel;
import com.github.shadeslayer1467.ui.activitytracker.models.EventSessionModel;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ViewActivityFragment extends Fragment {

    private RecyclerView sessionsRecyclerView;
    private SessionAdapter sessionAdapter;
    private FloatingActionButton addSessionButton;

    private TextView activityNameTextView;
    private TextView totalTimeTextView;
    private EventModel activityModel;

    private List<EventSessionModel> sessionList;
    private EventDatabase db;

    public ViewActivityFragment(EventModel activityModel) {
        // Required empty public constructor
        this.activityModel = activityModel;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activitytracker_activity, container, false);

        db = new LocalEventDatabase(this.getActivity());
        db.openDatabase();

        // Initialize UI elements
        activityNameTextView = view.findViewById(R.id.fragment_activitytracker_activity_activityTitle);
        totalTimeTextView = view.findViewById(R.id.fragment_activitytracker_activity_totalTimeTextView);
        sessionsRecyclerView = view.findViewById(R.id.fragment_activitytracker_activity_sessionsRecyclerView);
        addSessionButton = view.findViewById(R.id.fragment_activitytracker_activity_addSessionButton);

        // Setup RecyclerView
        sessionAdapter = new SessionAdapter(sessionList);
        sessionsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        sessionsRecyclerView.setAdapter(sessionAdapter);

        sessionList = new ArrayList<>();
        sessionList = db.getSessionsForEvent(activityModel.getEventId(), false);
        Collections.reverse(sessionList);
        sessionAdapter.setSessions(sessionList);

        // Setup Add Session Button
        addSessionButton.setOnClickListener(v -> openAddSessionDialog());

        // Init Activity Details
        updateTotalTime();
        activityNameTextView.setText(activityModel.getEventName());

        return view;
    }

    private void openAddSessionDialog() {
        AddEditSessionDialog addDialog = AddEditSessionDialog.newInstance(null);
        addDialog.setTargetFragment(ViewActivityFragment.this, 1);
        addDialog.show(getParentFragmentManager(), AddEditSessionDialog.TAG);
        updateTotalTime();
    }

    private void updateTotalTime() {
        long totalTime = 0;
        for (EventSessionModel session : sessionList) {
            totalTime += session.getDuration();
        }
        int hours = (int) (totalTime / (1000 * 60 * 60));
        int minutes = (int) ((totalTime / (1000 * 60)) % 60);
        totalTimeTextView.setText(String.format("Total Time: %d hrs %d mins", hours, minutes));
    }
}