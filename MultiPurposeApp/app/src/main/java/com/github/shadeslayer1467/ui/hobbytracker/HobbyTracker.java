package com.github.shadeslayer1467.ui.hobbytracker;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.shadeslayer1467.MainActivity;
import com.github.shadeslayer1467.R;
import com.github.shadeslayer1467.ui.hobbytracker.adapters.ActivityAdapter;
import com.github.shadeslayer1467.ui.hobbytracker.databases.EventDatabase;
import com.github.shadeslayer1467.ui.hobbytracker.databases.LocalEventDatabase;
import com.github.shadeslayer1467.ui.hobbytracker.models.EventModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HobbyTracker extends Fragment implements DialogCloseListener {

    private RecyclerView hobbiesRecyclerView;
    private ActivityAdapter hobbiesAdapter;
    private FloatingActionButton fab;

    private List<EventModel> hobbiesList;
    private EventDatabase db;

    public HobbyTracker() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_activitytracker, container, false);

        db = new LocalEventDatabase(this.getActivity());
        db.openDatabase();

        // Initialize your task list here or in a separate method called during onCreateView
        hobbiesList = new ArrayList<>();

        hobbiesRecyclerView = view.findViewById(R.id.activitiesRecyclerView);
        hobbiesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Set up the adapter and RecyclerView
        hobbiesAdapter = new ActivityAdapter(db, (MainActivity) this.getActivity());
        hobbiesRecyclerView.setAdapter(hobbiesAdapter);

        fab = view.findViewById(R.id.activitiesFAB);

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new HobbiesRecyclerItemTouchHelper(hobbiesAdapter));
        itemTouchHelper.attachToRecyclerView(hobbiesRecyclerView);

        // Populate the task list with existing tasks if needed
        hobbiesList = db.getAllEvents();
        Collections.reverse(hobbiesList);
        hobbiesAdapter.setHobbies(hobbiesList);

        fab.setOnClickListener(view1 -> AddNewHobbyFragment.newInstance().show(getParentFragmentManager(), AddNewHobbyFragment.TAG));

        return view;
    }
    public void refreshHobbyList() {
        hobbiesList = db.getAllEvents();
        Collections.reverse(hobbiesList);
        hobbiesAdapter.setHobbies(hobbiesList);
        hobbiesAdapter.notifyDataSetChanged();
    }
    @Override
    public void handleDialogClose(DialogInterface dialog){
        hobbiesList = db.getAllEvents();
        Collections.reverse(hobbiesList);
        hobbiesAdapter.setHobbies(hobbiesList);
        hobbiesAdapter.notifyDataSetChanged();
    }

}