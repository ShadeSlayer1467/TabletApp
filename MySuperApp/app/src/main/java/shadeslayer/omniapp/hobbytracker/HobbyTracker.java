package shadeslayer.omniapp.hobbytracker;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import shadeslayer.omniapp.R;
import shadeslayer.omniapp.hobbytracker.Models.Activity;
import shadeslayer.omniapp.hobbytracker.Utils.DatabaseHandler;

public class HobbyTracker extends Fragment {
    public static final String TAG = "hobbyTrackerFragment";
    private ListView listView;
    private DatabaseHandler dbHandler;

    public HobbyTracker() {
        // Required empty public constructor
    }
    public static HobbyTracker newInstance() {
        HobbyTracker fragment = new HobbyTracker();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hobbytracker, container, false);
        listView = view.findViewById(R.id.listView);
        dbHandler = new DatabaseHandler(getContext());

        List<Activity> activities = dbHandler.getAllActivities();
        ArrayAdapter<Activity> adapter = new ArrayAdapter<Activity>(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, activities) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = view.findViewById(android.R.id.text1);
                Activity activity = (Activity) getItem(position);
                text1.setText(activity.getName() + " - " + activity.getDescription());
                return view;
            }
        };
        listView.setAdapter(adapter);
        return view;
    }
}