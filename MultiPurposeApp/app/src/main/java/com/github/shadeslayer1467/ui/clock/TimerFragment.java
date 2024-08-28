package com.github.shadeslayer1467.ui.clock;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import com.github.shadeslayer1467.R;

public class TimerFragment extends Fragment {
    TextView timerTextView;
    MaterialButton startTimer, pauseTimer, resetTimer;
    TimerManager timerManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clock_timer, container, false);
        timerTextView = view.findViewById(R.id.clockTimerTextView);
        startTimer = view.findViewById(R.id.clockTimerStart);
        pauseTimer = view.findViewById(R.id.clockTimerPause);
        resetTimer = view.findViewById(R.id.clockTimerReset);

        timerManager = new TimerManager(timerTextView, 1000); // 1 minute timer

        setupUI();
        return view;
    }

    private void setupUI() {
        startTimer.setOnClickListener(v -> StartListener());
        pauseTimer.setOnClickListener(v -> PauseListener());
        resetTimer.setOnClickListener(v -> ResetListener());
    }

    private void StartListener()
    {
        timerManager.start(()->Finished());
        resetTimer.setEnabled(false);
        pauseTimer.setEnabled(true);
        startTimer.setEnabled(false);
    }
    private void PauseListener()
    {
        timerManager.pause();
        resetTimer.setEnabled(true);
        pauseTimer.setEnabled(false);
        startTimer.setEnabled(true);
    }
    private void ResetListener()
    {
        timerManager.reset();
        resetTimer.setEnabled(false);
        pauseTimer.setEnabled(false);
        startTimer.setEnabled(true);
    }
    private void Finished()
    {
        resetTimer.setEnabled(true);
        pauseTimer.setEnabled(false);
        startTimer.setEnabled(false);
    }
}

