package com.github.shadeslayer1467.ui.activitytracker;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavHostController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.shadeslayer1467.MainActivity;
import com.github.shadeslayer1467.R;
import com.github.shadeslayer1467.ui.activitytracker.adapters.ActivityAdapter;
import com.github.shadeslayer1467.ui.activitytracker.databases.EventDatabase;
import com.github.shadeslayer1467.ui.activitytracker.databases.LocalEventDatabase;
import com.github.shadeslayer1467.ui.activitytracker.models.EventModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ActivityTracker extends Fragment implements DialogCloseListener {

    private RecyclerView activitiesRecyclerView;
    private ActivityAdapter activitiesAdapter;
    private FloatingActionButton fab;

    private List<EventModel> activitiesList;
    private EventDatabase db;

    public ActivityTracker() {
        // Required empty public constructor
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavController navController = NavHostFragment.findNavController(this);
        navController.getCurrentBackStackEntry().getSavedStateHandle().getLiveData("refresh")
                .observe(getViewLifecycleOwner(), refresh -> {
                    refreshActivityList();
                });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_activitytracker, container, false);

        db = new LocalEventDatabase(this.getActivity());
        db.openDatabase();

        // Initialize your task list here or in a separate method called during onCreateView
        activitiesList = new ArrayList<>();

        activitiesRecyclerView = view.findViewById(R.id.activitiesRecyclerView);
        activitiesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        NavController navController = NavHostFragment.findNavController(this);

        // Set up the adapter and RecyclerView
        activitiesAdapter = new ActivityAdapter(getContext(),db, navController, this.getParentFragmentManager());
        activitiesRecyclerView.setAdapter(activitiesAdapter);

        fab = view.findViewById(R.id.activitiesFAB);

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new HobbiesRecyclerItemTouchHelper(activitiesAdapter));
        itemTouchHelper.attachToRecyclerView(activitiesRecyclerView);


        // Populate the task list with existing tasks if needed
        db.updateAllEventTotalTimes();
        activitiesList = db.getAllEvents();
        Collections.reverse(activitiesList);
        activitiesAdapter.setActivities(activitiesList);

        fab.setOnClickListener(view1 -> AddNewActivityFragment.newInstance().show(getParentFragmentManager(), AddNewActivityFragment.TAG));

        return view;
    }
    public void refreshActivityList() {
        activitiesList = db.getAllEvents();
        Collections.reverse(activitiesList);
        activitiesAdapter.setActivities(activitiesList);
        activitiesAdapter.notifyDataSetChanged();
    }
    @Override
    public void handleDialogClose(DialogInterface dialog){
        activitiesList = db.getAllEvents();
        Collections.reverse(activitiesList);
        activitiesAdapter.setActivities(activitiesList);
        activitiesAdapter.notifyDataSetChanged();
    }

}