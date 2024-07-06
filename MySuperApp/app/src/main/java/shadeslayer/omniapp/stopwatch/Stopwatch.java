package shadeslayer.omniapp.stopwatch;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Locale;

import shadeslayer.omniapp.R;

public class Stopwatch extends Fragment {
    public static final String TAG = "StopwatchFragment";
    TextView textview;
    MaterialButton reset, start, stop;
    int seconds, minutes, milliSeconds;
    long millisecond, startTime,timeBuff, updateTime = 0L;
    Handler handler;


    ListView lvTimes;
    ArrayList<String> timeList = new ArrayList<>();
    ArrayAdapter<String> timeListAdapter;

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            millisecond = SystemClock.uptimeMillis() - startTime;
            updateTime = timeBuff + millisecond;
            seconds = (int) (updateTime / 1000);
            minutes = seconds / 60;
            seconds = seconds % 60;
            milliSeconds = (int) (updateTime % 1000);

            textview.setText(MessageFormat.format("{0}:{1}:{2}",minutes, String.format(Locale.getDefault(), "%02d", seconds), String.format(Locale.getDefault(), "%02d", milliSeconds)));
            handler.postDelayed(this, 0);
        }
    };

    public Stopwatch() {
        // Required empty public constructor
    }
    public static Stopwatch newInstance() {
        Stopwatch fragment = new Stopwatch();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stopwatch, container, false);

        textview = view.findViewById(R.id.textView);
        reset = view.findViewById(R.id.reset);
        start = view.findViewById(R.id.start);
        stop = view.findViewById(R.id.stop);
        lvTimes = view.findViewById(R.id.lvTimes);

        // ArrayAdapter setup
        timeListAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, timeList);
        lvTimes.setAdapter(timeListAdapter);

        handler = new Handler(Looper.getMainLooper());

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTime = SystemClock.uptimeMillis();
                handler.postDelayed(runnable, 0);
                reset.setEnabled(false);
                stop.setEnabled(true);
                start.setEnabled(false);
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeBuff += millisecond;
                handler.removeCallbacks(runnable);
                reset.setEnabled(true);
                stop.setEnabled(false);
                start.setEnabled(true);
                // Capture and display the time
                timeList.add(textview.getText().toString());
                timeListAdapter.notifyDataSetChanged();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                millisecond = 0L;
                startTime = 0L;
                timeBuff = 0L;
                updateTime = 0L;
                seconds = 0;
                minutes = 0;
                milliSeconds = 0;
                textview.setText("00:00:00");
            }
        });

        textview.setText("00:00:00");

        return view;
    }
}