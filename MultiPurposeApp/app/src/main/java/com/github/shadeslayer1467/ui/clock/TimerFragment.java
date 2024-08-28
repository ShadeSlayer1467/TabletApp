package com.github.shadeslayer1467.ui.clock;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import com.github.shadeslayer1467.R;

import java.util.Locale;

public class TimerFragment extends Fragment {
    TextView timerTextView;
    MaterialButton startTimer, pauseTimer, resetTimer;
    NumberPicker hourPicker, minutePicker, secondPicker;
    TimerManager timerManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clock_timer, container, false);
        timerTextView = view.findViewById(R.id.clockTimerTextView);
        startTimer = view.findViewById(R.id.clockTimerStart);
        pauseTimer = view.findViewById(R.id.clockTimerPause);
        resetTimer = view.findViewById(R.id.clockTimerReset);

        hourPicker = view.findViewById(R.id.clockTimerHourPicker);
        minutePicker = view.findViewById(R.id.clockTimerMinutePicker);
        secondPicker = view.findViewById(R.id.clockTimerSecondPicker);

        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(59);
        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(59);
        secondPicker.setMinValue(0);
        secondPicker.setMaxValue(59);

        setupUI();
        return view;
    }

    private void setupUI() {
        hourPicker.setOnValueChangedListener((picker, oldVal, newVal) -> updateTimerDisplay());
        minutePicker.setOnValueChangedListener((picker, oldVal, newVal) -> updateTimerDisplay());
        secondPicker.setOnValueChangedListener((picker, oldVal, newVal) -> updateTimerDisplay());

        startTimer.setOnClickListener(v -> StartListener());
        pauseTimer.setOnClickListener(v -> PauseListener());
        resetTimer.setOnClickListener(v -> ResetListener());
    }

    private void StartListener()
    {
        // continue
        if (timerManager != null && !timerManager.hasFinished)
        {
            timerManager.Resume();
            resetTimer.setEnabled(false);
            pauseTimer.setEnabled(true);
            startTimer.setEnabled(false);
            return;
        }
        // new timer
        long selectedTime = getSelectedTimeInMillis();
        if (selectedTime > SECOND) {
            DisablePickers();
            timerManager = new TimerManager(timerTextView, selectedTime, () -> Finished());
            timerManager.start();
            resetTimer.setEnabled(false);
            pauseTimer.setEnabled(true);
            startTimer.setEnabled(false);
        }
    }
    private void PauseListener()
    {
        if (timerManager != null) {
            if (timerManager.hasFinished) EnablePickers();
            timerManager.pause();
            resetTimer.setEnabled(true);
            pauseTimer.setEnabled(false);
            startTimer.setEnabled(true);
        }
    }
    private void ResetListener()
    {
        EnablePickers();
        if (timerManager != null) {
            resetTimer.setEnabled(true);
            pauseTimer.setEnabled(false);
            startTimer.setEnabled(true);
            long selectedTime = getSelectedTimeInMillis();
            if (selectedTime > SECOND) timerManager = new TimerManager(timerTextView, selectedTime, () -> Finished());
            updateTimerDisplay();
        }

    }
    private void Finished()
    {
        resetTimer.setEnabled(true);
        pauseTimer.setEnabled(false);
        startTimer.setEnabled(false);
        EnablePickers();
    }
    private long getSelectedTimeInMillis() {
        int hours = hourPicker.getValue();
        int minutes = minutePicker.getValue();
        int seconds = secondPicker.getValue();
        return (hours * HOUR + minutes * MINUTE + seconds * SECOND);
    }

    private void updateTimerDisplay() {
        int hours = hourPicker.getValue();
        int minutes = minutePicker.getValue();
        int seconds = secondPicker.getValue();

        if (hours == 0) timerTextView.setText(String.format(Locale.getDefault(), "%02d:%02d:%03d", minutes, seconds, 0));
        else timerTextView.setText(String.format(Locale.getDefault(), "%02d:%02d:%02d:%03d", hours, minutes, seconds, 0));
    }
    private void DisablePickers()
    {
        hourPicker.setEnabled(false);
        minutePicker.setEnabled(false);
        secondPicker.setEnabled(false);
    }
    private void EnablePickers()
    {
        hourPicker.setEnabled(true);
        minutePicker.setEnabled(true);
        secondPicker.setEnabled(true);
    }

    private final long HOUR = 3_600_000;
    private final long MINUTE = 60_000;
    private final long SECOND = 1_000;
}

