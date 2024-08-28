package com.github.shadeslayer1467.ui.clock;

import android.os.CountDownTimer;
import android.widget.TextView;

import java.util.Locale;

public class TimerManager {
    private final TextView textView;
    private final long initialTime;
    private CountDownTimer countDownTimer;

    private long timeLeft;
    Runnable finishedReset;
    public Boolean hasFinished = true;

    public TimerManager(TextView textView, long initialTime, Runnable finishedReset) {
        this.textView = textView;
        this.initialTime = initialTime;
        timeLeft = initialTime;
        this.finishedReset = finishedReset;
        NewCountdown();
    }

    public void start() {
        hasFinished = false;
        NewCountdown();
        countDownTimer.start();
    }

    public void pause() {
        countDownTimer.cancel();
    }

    public void Resume() {
        NewCountdown(timeLeft);
        countDownTimer.start();
    }

    private void NewCountdown()
    {
        NewCountdown(initialTime);
    }
    private void NewCountdown(Long time)
    {
        countDownTimer = new CountDownTimer(time, 10) {
            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished;
                try {
                    int hours = (int) (millisUntilFinished / HOUR);
                    int minutes = (int) ((millisUntilFinished % HOUR) / MINUTE);
                    int seconds = (int) ((millisUntilFinished % MINUTE) / SECOND);
                    int milliseconds = (int) (millisUntilFinished % SECOND);

                    if (hours == 0) textView.setText(String.format(Locale.getDefault(), "%02d:%02d:%03d", minutes, seconds, milliseconds));
                    else textView.setText(String.format(Locale.getDefault(), "%02d:%02d:%02d:%03d", hours, minutes, seconds, milliseconds));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void onFinish() {
                textView.setText("00:00:000");
                hasFinished = true;
                if (finishedReset != null) finishedReset.run();
            }
        };
    }

    private final long HOUR = 3_600_000;
    private final long MINUTE = 60_000;
    private final long SECOND = 1_000;
}

