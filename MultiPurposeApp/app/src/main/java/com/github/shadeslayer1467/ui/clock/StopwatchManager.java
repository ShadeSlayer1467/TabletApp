package com.github.shadeslayer1467.ui.clock;

import android.os.Handler;
import android.os.SystemClock;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class StopwatchManager extends ViewModel {
    private long startTime, timeBuff, updateTime = 0L;
    private boolean isRunning = false;
    private final MutableLiveData<String> timeLiveData = new MutableLiveData<>();
    private final Handler handler = new Handler();

    private final Runnable updater = new Runnable() {
        @Override
        public void run() {
            if (isRunning) {
                long millisecondTime = SystemClock.uptimeMillis() - startTime;
                updateTime = timeBuff + millisecondTime;
                updateTimer();
                handler.postDelayed(this, 50);
            }
        }
    };

    public LiveData<String> getTimeLiveData() {
        return timeLiveData;
    }

    public void start() {
        if (!isRunning) {
            startTime = SystemClock.uptimeMillis();
            handler.post(updater);
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
        isRunning = false;
        updateTimer();
    }

    private void updateTimer() {
        int seconds = (int) (updateTime / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;
        int milliSeconds = (int) (updateTime % 1000);
        timeLiveData.postValue(String.format("%02d:%02d:%03d", minutes, seconds, milliSeconds));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        handler.removeCallbacks(updater);
    }
}
