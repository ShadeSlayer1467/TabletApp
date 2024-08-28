package com.github.shadeslayer1467.ui.clock;

import android.os.Handler;
import android.os.SystemClock;
import android.widget.TextView;

public class StopwatchManager {
    private final TextView textView;
    private final Handler handler;
    private long startTime, timeBuff, updateTime = 0L;
    private int seconds, minutes, milliSeconds;
    private boolean isRunning = false;
    private final Runnable updater = new Runnable() {
        public void run() {
            long millisecondTime = SystemClock.uptimeMillis() - startTime;
            updateTime = timeBuff + millisecondTime;
            updateTimer();
            handler.postDelayed(this, 0);
        }
    };

    public StopwatchManager(TextView textView, Handler handler) {
        this.textView = textView;
        this.handler = handler;
    }

    public void start() {
        if (!isRunning) {
            startTime = SystemClock.uptimeMillis();
            handler.postDelayed(updater, 0);
            isRunning = true;
        }
    }

    public void stop() {
        if (isRunning) {
            timeBuff += SystemClock.uptimeMillis() - startTime;
            handler.removeCallbacks(updater);
            isRunning = false;
        }
    }

    public void reset() {
        startTime = 0L;
        timeBuff = 0L;
        updateTime = 0L;
        minutes = 0;
        seconds = 0;
        milliSeconds = 0;

        updateTimer();
    }

    private void updateTimer() {
        seconds = (int) (updateTime / 1000);
        minutes = seconds / 60;
        seconds = seconds % 60;
        milliSeconds = (int) (updateTime % 1000);
        textView.setText(String.format("%02d:%02d:%02d", minutes, seconds, milliSeconds));
    }
}

