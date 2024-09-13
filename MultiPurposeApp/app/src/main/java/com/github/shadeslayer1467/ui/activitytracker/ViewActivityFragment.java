package com.github.shadeslayer1467.ui.activitytracker;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.shadeslayer1467.R;
import com.github.shadeslayer1467.ui.activitytracker.adapters.SessionAdapter;
import com.github.shadeslayer1467.ui.activitytracker.databases.EventDatabase;
import com.github.shadeslayer1467.ui.activitytracker.databases.LocalEventDatabase;
import com.github.shadeslayer1467.ui.activitytracker.dialogs.AddEditSessionDialog;
import com.github.shadeslayer1467.ui.activitytracker.models.EventModel;
import com.github.shadeslayer1467.ui.activitytracker.models.EventSessionModel;

import com.github.shadeslayer1467.ui.todoapp.DialogCloseListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ViewActivityFragment extends Fragment implements DialogCloseListener, SessionActionListener {

    private static final String ARG_ACTIVITY = "activity";
    private RecyclerView sessionsRecyclerView;
    private SessionAdapter sessionAdapter;
    private FloatingActionButton addSessionButton;

    private TextView activityNameTextView;
    private TextView totalTimeTextView;
    private EventModel activityModel;

    private List<EventSessionModel> sessionList;
    private EventDatabase db;

    private SwipeRefreshLayout swipeRefreshLayout;

    public ViewActivityFragment(){}

    public static ViewActivityFragment newInstance(EventModel activity) {
        ViewActivityFragment fragment = new ViewActivityFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ACTIVITY, activity);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            activityModel = (EventModel) getArguments().getSerializable(ARG_ACTIVITY);
        } else {
            // Log or handle the case where arguments are missing
            Log.e("ViewActivityFragment", "Arguments are missing or null!");
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activitytracker_session_views, container, false);
        if (activityModel == null) {
            // Handle the null case (activityModel was not passed correctly)
            Log.e("ViewActivityFragment", "activityModel is null!");
            return view;
        }
        db = new LocalEventDatabase(this.getActivity());
        db.openDatabase();

        // Initialize UI elements
        activityNameTextView = view.findViewById(R.id.fragment_activitytracker_activity_activityTitle);
        totalTimeTextView = view.findViewById(R.id.fragment_activitytracker_activity_totalTimeTextView);
        sessionsRecyclerView = view.findViewById(R.id.fragment_activitytracker_activity_sessionsRecyclerView);
        addSessionButton = view.findViewById(R.id.fragment_activitytracker_activity_addSessionButton);


        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            refreshSessions();
            swipeRefreshLayout.setRefreshing(false);
        });

        // Setup RecyclerView
        sessionAdapter = new SessionAdapter(sessionList, this);
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
        AddEditSessionDialog addDialog = AddEditSessionDialog.newInstance(null, activityModel.getEventId());
        addDialog.setTargetFragment(ViewActivityFragment.this, 1);
        addDialog.show(getParentFragmentManager(), AddEditSessionDialog.TAG);
        updateTotalTime();
    }
    @Override
    public void onSessionDeleted(int sessionId) {
        db.markSessionAsDeleted(sessionId);
        refreshSessions();
    }
    private void updateTotalTime() {
        long totalTime = 0;
        for (EventSessionModel session : sessionList) {
            if(!session.isDeleted()) totalTime += session.getDuration();
        }
        int hours = (int) (totalTime / (1000 * 60 * 60));
        int minutes = (int) ((totalTime / (1000 * 60)) % 60);
        totalTimeTextView.setText(String.format("Total Time: %d hrs %d mins", hours, minutes));
    }
    public void refreshSessions() {
        sessionList = db.getSessionsForEvent(activityModel.getEventId(), false);
        Collections.reverse(sessionList);
        sessionAdapter.setSessions(sessionList);
        sessionAdapter.notifyDataSetChanged();
        updateTotalTime();
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        sessionList = db.getSessionsForEvent(activityModel.getEventId(),false);
        Collections.reverse(sessionList);
        sessionAdapter.setSessions(sessionList);
        updateTotalTime();
        sessionAdapter.notifyDataSetChanged();
    }
}