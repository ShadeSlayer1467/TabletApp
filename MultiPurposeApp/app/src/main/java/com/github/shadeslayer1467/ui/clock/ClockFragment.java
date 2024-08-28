package com.github.shadeslayer1467.ui.clock;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.github.shadeslayer1467.R;

public class ClockFragment extends Fragment {
    public static final String TAG = "ClockFragment";
    private static final String SELECTED_ITEM_ID = "selected_item_id";
    private int selectedItemId = R.id.navigation_stopwatch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_clock, container, false);

        if (savedInstanceState != null) {
            selectedItemId = savedInstanceState.getInt(SELECTED_ITEM_ID, R.id.navigation_stopwatch);
        }

        // Set up BottomNavigationView
        BottomNavigationView bottomNavigationView = view.findViewById(R.id.clockBottomNavigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            selectedItemId = item.getItemId();
            if (selectedItemId == R.id.navigation_stopwatch) {
                loadFragment(new StopwatchFragment());
                return true;
            } else if (selectedItemId == R.id.navigation_timer) {
                loadFragment(new TimerFragment());
                return true;
            } else return false;
        });

        // Load the StopwatchFragment by default
        bottomNavigationView.setSelectedItemId(selectedItemId);
        loadFragmentById(selectedItemId);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SELECTED_ITEM_ID, selectedItemId);
    }

    private void loadFragmentById(int itemId) {
        Fragment fragment;
        if (itemId == R.id.navigation_timer) {
            fragment = new TimerFragment();
        } else {
            fragment = new StopwatchFragment(); // Default to stopwatch
        }
        loadFragment(fragment);
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.clockFragmentContainer, fragment);
        transaction.commit();
    }
}

