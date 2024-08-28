package com.github.shadeslayer1467.ui.clock;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
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
    TextView stopwatchTextView;
    MaterialButton resetStopwatch, startStopwatch, stopStopwatch;
    StopwatchManager stopwatchManager;

    ListView lvTimes;
    ArrayList<String> timeList = new ArrayList<>();
    ArrayAdapter<String> timeListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clock_stopwatch, container, false);
        stopwatchTextView = view.findViewById(R.id.clockStopwatchTextView);
        resetStopwatch = view.findViewById(R.id.clockStopwatchReset);
        startStopwatch = view.findViewById(R.id.clockStopwatchStart);
        stopStopwatch = view.findViewById(R.id.clockStopwatchStop);
        lvTimes = view.findViewById(R.id.clockStopwatchLVTimes);

        timeListAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, timeList);
        lvTimes.setAdapter(timeListAdapter);

        stopwatchManager = new StopwatchManager(stopwatchTextView, new Handler(Looper.getMainLooper()));

        setupUI();
        return view;
    }

    private void setupUI() {
        stopwatchTextView.setText("00:00:000");

        startStopwatch.setOnClickListener(v -> StartListener());
        stopStopwatch.setOnClickListener(v -> StopListener());
        resetStopwatch.setOnClickListener(v -> ResetListener());
    }

    private void StartListener()
    {
        stopwatchManager.start();
        resetStopwatch.setEnabled(false);
        stopStopwatch.setEnabled(true);
        startStopwatch.setEnabled(false);
    }
    private void StopListener()
    {
        stopwatchManager.stop();
        resetStopwatch.setEnabled(true);
        stopStopwatch.setEnabled(false);
        startStopwatch.setEnabled(true);

        timeList.add(stopwatchTextView.getText().toString());
        timeListAdapter.notifyDataSetChanged();
    }
    private void ResetListener()
    {
        stopwatchManager.reset();
        stopwatchTextView.setText("00:00:000");
    }
}

