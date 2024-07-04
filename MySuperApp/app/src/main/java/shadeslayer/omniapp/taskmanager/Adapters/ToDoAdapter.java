package shadeslayer.omniapp.taskmanager.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import shadeslayer.omniapp.taskmanager.AddNewTask;
import shadeslayer.omniapp.taskmanager.MainActivity;
import shadeslayer.omniapp.taskmanager.Models.ToDoModel;
import shadeslayer.omniapp.R;
import shadeslayer.omniapp.taskmanager.Utils.DatabaseHandler;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {

    private List<ToDoModel> todoList;
    private FragmentActivity fragmentActivity;
    private DatabaseHandler db;

    public  ToDoAdapter(DatabaseHandler db, FragmentActivity fragmentActivity){
        this.db = db;
        this.fragmentActivity = fragmentActivity;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout, parent, false);
        return new ViewHolder(itemView);
    }

    public void onBindViewHolder(ViewHolder holder, int position){
        db.openDatabase();
        ToDoModel item = todoList.get(position);
        holder.task.setText(item.getTask());
        holder.task.setChecked(item.getStatus() == 1);
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    db.updateStatus(item.getId(), 1);
                } else {
                    db.updateStatus(item.getId(), 0);
                }
            }
        });
    }

    public int getItemCount(){
        return todoList.size();
    }

    public void setTasks(List<ToDoModel> todoList){
        this.todoList = todoList;
        notifyDataSetChanged();
    }

    public Context getContext(){
        return fragmentActivity;
    }

    public void deleteItem(int position){
        ToDoModel item = todoList.get(position);
        db.deleteTask(item.getId());
        todoList.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position){
        ToDoModel item = todoList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());
        AddNewTask fragment = new AddNewTask();
        fragment.setArguments(bundle);
        fragment.show(fragmentActivity.getSupportFragmentManager(), AddNewTask.TAG);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox task;

        ViewHolder(View view) {
            super(view);
            task = view.findViewById(R.id.todoCheckBox);
        }
    }
}
