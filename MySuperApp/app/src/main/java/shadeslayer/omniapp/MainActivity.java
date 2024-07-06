package shadeslayer.omniapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import shadeslayer.omniapp.databinding.ActivityMainBinding;
import shadeslayer.omniapp.hobbytracker.HobbyTracker;
import shadeslayer.omniapp.stopwatch.Stopwatch;
import shadeslayer.omniapp.todolistapp.ToDoApp;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new Home(), Home.TAG);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                replaceFragment(new Home(), Home.TAG);
            } else if (itemId == R.id.todoMenuItem) {
                replaceFragment(new ToDoApp(), ToDoApp.TAG);
            } else if (itemId == R.id.stopwatchMenuItem) {
                replaceFragment(new Stopwatch(), Stopwatch.TAG);
            } else if (itemId == R.id.hobbyTrackerMenuItem) {
                replaceFragment(new HobbyTracker(), HobbyTracker.TAG);
            } else {
                // TODO: Log unexpected behavior
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, fragment, tag)
                .commit();
    }
}