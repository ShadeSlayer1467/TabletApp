package com.github.shadeslayer1467.ui.clock;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import com.github.shadeslayer1467.R;

import java.util.ArrayList;

public class StopwatchFragment extends Fragment {
    private TextView stopwatchTextView;
    private MaterialButton resetStopwatch, startStopwatch, stopStopwatch;
    private StopwatchManager stopwatchManager;

    private ListView lvTimes;
    private ArrayList<String> timeList = new ArrayList<>();
    private ArrayAdapter<String> timeListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clock_stopwatch, container, false);

        stopwatchTextView = view.findViewById(R.id.clockStopwatchTextView);
        resetStopwatch = view.findViewById(R.id.clockStopwatchReset);
        startStopwatch = view.findViewById(R.id.clockStopwatchStart);
        stopStopwatch = view.findViewById(R.id.clockStopwatchStop);
        lvTimes = view.findViewById(R.id.clockStopwatchLVTimes);

        timeListAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, timeList);
        lvTimes.setAdapter(timeListAdapter);

        stopwatchManager = new ViewModelProvider(this).get(StopwatchManager.class);

        setupUI();
        observeStopwatch();

        return view;
    }

    private void setupUI() {
        stopwatchTextView.setText("00:00:000");

        startStopwatch.setOnClickListener(v -> startStopwatch());
        stopStopwatch.setOnClickListener(v -> stopStopwatch());
        resetStopwatch.setOnClickListener(v -> resetStopwatch());
    }

    private void startStopwatch() {
        stopwatchManager.start();
        resetStopwatch.setEnabled(false);
        stopStopwatch.setEnabled(true);
        startStopwatch.setEnabled(false);
    }

    private void stopStopwatch() {
        stopwatchManager.stop();
        resetStopwatch.setEnabled(true);
        stopStopwatch.setEnabled(false);
        startStopwatch.setEnabled(true);

        timeList.add(stopwatchTextView.getText().toString());
        timeListAdapter.notifyDataSetChanged();
    }

    private void resetStopwatch() {
        stopwatchManager.reset();
        stopwatchTextView.setText("00:00:000");
    }

    private void observeStopwatch() {
        stopwatchManager.getTimeLiveData().observe(getViewLifecycleOwner(), time -> {
            stopwatchTextView.setText(time);
        });
    }
}
