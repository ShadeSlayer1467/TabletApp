package com.github.shadeslayer1467.ui.clock;

import android.os.CountDownTimer;
import android.widget.TextView;

import java.util.Locale;

public class TimerManager {
    private final TextView textView;
    private final long initialTime;
    private CountDownTimer countDownTimer;
    private long timeLeft;

    public TimerManager(TextView textView, long initialTime) {
        this.textView = textView;
        this.initialTime = initialTime;
        this.timeLeft = initialTime;
    }

    public void start(Runnable reset) {
        countDownTimer = new CountDownTimer(timeLeft, 10) {
            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished;
                updateTimer();
            }

            public void onFinish() {
                textView.setText("00:00:00");
                if (reset != null) reset.run();
            }
        }.start();
    }

    public void pause() {
        countDownTimer.cancel();
    }

    public void reset() {
        timeLeft = initialTime;
        updateTimer();
    }

    private void updateTimer() {
        try {
            int minutes = (int) (timeLeft / 1000 / 60);
            int seconds = (int) (timeLeft / 1000 % 60);
            int milliseconds;

            milliseconds = (timeLeft > 1000) ? (int) (timeLeft % (seconds * 1000) / 10) :
                    (int) (timeLeft % 1000 / 10); // Handle case when timeLeft < 1000 ms


            textView.setText(String.format(Locale.getDefault(), "%02d:%02d:%02d", minutes, seconds, milliseconds));
        } catch (Exception e) {
        }
    }
}

