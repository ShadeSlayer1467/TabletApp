package com.github.shadeslayer1467.ui.activitytracker;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.github.shadeslayer1467.R;
import com.github.shadeslayer1467.ui.activitytracker.databases.LocalEventDatabase;
import com.github.shadeslayer1467.ui.activitytracker.models.EventModel;

public class AddNewActivityFragment extends DialogFragment {

    public static final String TAG = "AddNewHobbyFragment";

    private EditText hobbyNameEditText;
    private EditText totalMSEditText;
    private Button saveButton;

    private LocalEventDatabase db;
    private boolean isUpdate = false;
    private int eventId = -1;

    public static AddNewActivityFragment newInstance() {
        return new AddNewActivityFragment();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new LocalEventDatabase(getContext());
        db.openDatabase();
        //setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activitytracker_activity_add, container, false);

        hobbyNameEditText = view.findViewById(R.id.fragment_activitytracker_new_activity_activityNameEditText);
        totalMSEditText = view.findViewById(R.id.fragment_activitytracker_new_activity_totalMSEditText);
        saveButton = view.findViewById(R.id.fragment_activitytracker_new_activity_saveActivityButton);

        final Bundle bundle = getArguments();
        if (bundle != null) {
            isUpdate = true;
            eventId = bundle.getInt("id");
            hobbyNameEditText.setText(bundle.getString("event_name"));
            totalMSEditText.setText(String.valueOf(bundle.getLong("total_ms")));
            totalMSEditText.setEnabled(false);
        }

        saveButton.setOnClickListener(v -> saveHobby());

        return view;
    }
    private void saveHobby() {
        String hobbyName = hobbyNameEditText.getText().toString();
        String totalMsStr = totalMSEditText.getText().toString();

        if (TextUtils.isEmpty(hobbyName) || TextUtils.isEmpty(totalMsStr)) {
            Toast.makeText(getContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        long totalMs = Long.parseLong(totalMsStr);

        if (isUpdate) {
            EventModel event = new EventModel(eventId, hobbyName, 1, null, null, null, totalMs); // Assuming category ID 1 for now
            db.updateEvent(event);
        } else {
            EventModel event = new EventModel(0, hobbyName, 1, null, null, null, totalMs);
            db.createEvent(event);
        }

        dismiss();
    }
    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Fragment navHostFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
        if (navHostFragment != null) {
            Fragment currentFragment = navHostFragment.getChildFragmentManager().getFragments().get(0);
            if (currentFragment instanceof DialogCloseListener) {
                ((DialogCloseListener) currentFragment).handleDialogClose(dialog);
            }
        }
    }
}