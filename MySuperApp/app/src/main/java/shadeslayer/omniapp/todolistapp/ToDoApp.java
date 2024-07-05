package shadeslayer.omniapp.todolistapp;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import shadeslayer.omniapp.R;
import shadeslayer.omniapp.todolistapp.Adapters.ToDoAdapter;
import shadeslayer.omniapp.todolistapp.Models.ToDoModel;
import shadeslayer.omniapp.todolistapp.Utils.DatabaseHandler;

public class ToDoApp extends Fragment implements DialogCloseListener {
    public static final String TAG = "ToDoFragment";

    private RecyclerView tasksRecyclerView;
    private ToDoAdapter tasksAdapter;
    private FloatingActionButton fab;

    private List<ToDoModel> tasksList;
    private DatabaseHandler db;

    public ToDoApp() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_to_do_app, container, false);

        db = new DatabaseHandler(this.getActivity());
        db.openDatabase();

        // Initialize your task list here or in a separate method called during onCreateView
        tasksList = new ArrayList<>();

        tasksRecyclerView = view.findViewById(R.id.tasksRecyclerView);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Set up the adapter and RecyclerView
        tasksAdapter = new ToDoAdapter(db,this.getActivity());
        tasksRecyclerView.setAdapter(tasksAdapter);

        fab = view.findViewById(R.id.fab);

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new RecyclerItemTouchHelper(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView);

        // Populate the task list with existing tasks if needed
        tasksList = db.getAllTasks();
        Collections.reverse(tasksList);
        tasksAdapter.setTasks(tasksList);

        fab.setOnClickListener(view1 -> AddNewTask.newInstance().show(getActivity().getSupportFragmentManager(), AddNewTask.TAG));

        return view;
    }
    @Override
    public void handleDialogClose(DialogInterface dialog){
        tasksList = db.getAllTasks();
        Collections.reverse(tasksList);
        tasksAdapter.setTasks(tasksList);
        tasksAdapter.notifyDataSetChanged();
    }

}