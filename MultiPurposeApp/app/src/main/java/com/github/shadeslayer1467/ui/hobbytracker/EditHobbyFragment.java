package com.github.shadeslayer1467.ui.hobbytracker;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.shadeslayer1467.MainActivity;
import com.github.shadeslayer1467.R;
import com.github.shadeslayer1467.ui.hobbytracker.adapters.HobbyAdapter;
import com.github.shadeslayer1467.ui.hobbytracker.adapters.SessionAdapter;
import com.github.shadeslayer1467.ui.hobbytracker.databases.EventDatabase;
import com.github.shadeslayer1467.ui.hobbytracker.databases.LocalEventDatabase;
import com.github.shadeslayer1467.ui.hobbytracker.models.EventModel;
import com.github.shadeslayer1467.ui.hobbytracker.models.EventSessionModel;
import com.github.shadeslayer1467.ui.hobbytracker.dialogs.AddEditSessionDialog;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EditHobbyFragment extends Fragment {

    private RecyclerView sessionsRecyclerView;
    private SessionAdapter sessionAdapter;
    private FloatingActionButton addSessionButton;

    private TextView totalTimeTextView;
    private int eventId;

    private List<EventSessionModel> sessionList;
    private EventDatabase db;

    public EditHobbyFragment(int eventId) {
        // Required empty public constructor
        this.eventId = eventId;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hobbytracker_hobby_edit, container, false);

        db = new LocalEventDatabase(this.getActivity());
        db.openDatabase();

        // Initialize UI elements
        totalTimeTextView = view.findViewById(R.id.fragment_hobbytracker_hobby_edit_totalTimeTextView);
        sessionsRecyclerView = view.findViewById(R.id.fragment_hobbytracker_hobby_edit_sessionsRecyclerView);
        addSessionButton = view.findViewById(R.id.fragment_hobbytracker_hobby_edit_addSessionButton);

        // Setup RecyclerView
        sessionAdapter = new SessionAdapter(sessionList, this::onSessionClick); // Handle session clicks
        sessionsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        sessionsRecyclerView.setAdapter(sessionAdapter);

        sessionList = new ArrayList<>();
        sessionList = db.getSessionsForEvent(eventId, false);
        Collections.reverse(sessionList);
        sessionAdapter.setSessions(sessionList);

        updateTotalTime();

        addSessionButton.setOnClickListener(v -> openAddSessionDialog());

        return view;
    }

    private void onSessionClick(EventSessionModel session) {
        openEditSessionDialog(session);
    }

    private void openAddSessionDialog() {
        AddEditSessionDialog addDialog = AddEditSessionDialog.newInstance(null); // Pass null for adding a new session
        addDialog.setTargetFragment(EditHobbyFragment.this, 1);
        addDialog.show(getParentFragmentManager(), AddEditSessionDialog.TAG);
    }

    private void openEditSessionDialog(EventSessionModel session) {
        AddEditSessionDialog editDialog = AddEditSessionDialog.newInstance(session);
        editDialog.setTargetFragment(EditHobbyFragment.this, 1);
        editDialog.show(getParentFragmentManager(), AddEditSessionDialog.TAG);
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