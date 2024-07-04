package shadeslayer.omniapp.taskmanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import shadeslayer.omniapp.R;
import shadeslayer.omniapp.taskmanager.Adapters.ToDoAdapter;
import shadeslayer.omniapp.taskmanager.Utils.DatabaseHandler;

public class TaskManagerFragment extends Fragment {

    private ToDoAdapter adapter;
    private RecyclerView tasksRecyclerView;
    private DatabaseHandler db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.todo_activity_main, container, false);

        tasksRecyclerView = view.findViewById(R.id.tasksRecyclerView);
        setupRecyclerView();

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show add task dialog or activity
            }
        });

        return view;
    }

    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        tasksRecyclerView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(tasksRecyclerView.getContext(), linearLayoutManager.getOrientation());
        tasksRecyclerView.addItemDecoration(dividerItemDecoration);

        db = new DatabaseHandler(getContext());
        db.openDatabase();

        adapter = new ToDoAdapter(db, getActivity());
        tasksRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();
        }
    }
}
