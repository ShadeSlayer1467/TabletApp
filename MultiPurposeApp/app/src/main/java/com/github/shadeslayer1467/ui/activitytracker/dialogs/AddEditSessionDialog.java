package com.github.shadeslayer1467.ui.activitytracker.dialogs;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.Button;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.github.shadeslayer1467.R;
import com.github.shadeslayer1467.ui.activitytracker.ViewActivityFragment;
import com.github.shadeslayer1467.ui.activitytracker.databases.EventDatabase;
import com.github.shadeslayer1467.ui.activitytracker.databases.LocalEventDatabase;
import com.github.shadeslayer1467.ui.activitytracker.models.EventSessionModel;
import com.github.shadeslayer1467.ui.todoapp.DialogCloseListener;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class AddEditSessionDialog extends DialogFragment {
    public static final String TAG = "AddEditSessionDialog";
    EventSessionModel sessionModel;
    private boolean isEditMode = false;

    private TextInputEditText startTimeEditText;
    private TextInputEditText endTimeEditText;
    private TextView durationTextView;
    private Button saveSessionButton;
    private Button cancelSessionButton;

    private Calendar startTimeCalendar;
    private Calendar endTimeCalendar;

    public static AddEditSessionDialog newInstance(@Nullable EventSessionModel sessionModel, int eventId) {
        AddEditSessionDialog dialog = new AddEditSessionDialog();
        Bundle args = new Bundle();
        if (sessionModel != null) {
            args.putSerializable("session", sessionModel);
        }
        args.putInt("eventId", eventId);
        dialog.setArguments(args);
        return dialog;
    }
    @Override
    public void onStart() {
        super.onStart();

        if (getDialog() != null && getDialog().getWindow() != null) {
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.30);
            getDialog().getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (getArguments().containsKey("session")) {
                sessionModel = (EventSessionModel) getArguments().getSerializable("session");
                isEditMode = true;
            } else {
                sessionModel = new EventSessionModel();
                isEditMode = false;
            }

            if (getArguments().containsKey("eventId")) {
                sessionModel.setEventId(getArguments().getInt("eventId"));
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activitytracker_activity_session_add_edit_dialog, container, false);

        startTimeEditText = view.findViewById(R.id.fragment_activitytracker_activity_session_add_edit_dialog_startTimeEditText);
        endTimeEditText = view.findViewById(R.id.fragment_activitytracker_activity_session_add_edit_dialog_endTimeEditText);
        durationTextView = view.findViewById(R.id.fragment_activitytracker_activity_session_add_edit_dialog_durationValue);
        saveSessionButton = view.findViewById(R.id.fragment_activitytracker_activity_session_add_edit_dialog_saveSessionButton);
        cancelSessionButton = view.findViewById(R.id.fragment_activitytracker_activity_session_add_edit_dialog_cancelSessionButton);

        startTimeCalendar = Calendar.getInstance();
        endTimeCalendar = Calendar.getInstance();

        if (isEditMode) {
            populateSessionDetails();
        }

        startTimeEditText.setOnClickListener(v -> showTimePicker(startTimeCalendar, startTimeEditText, this::updateDuration));
        endTimeEditText.setOnClickListener(v -> showTimePicker(endTimeCalendar, endTimeEditText, this::updateDuration));

        saveSessionButton.setOnClickListener(v -> saveSession());

        cancelSessionButton.setOnClickListener(v -> dismiss());

        return view;
    }
    private void populateSessionDetails() {
        if (!sessionModel.getStartTime().isEmpty()) {
            startTimeCalendar.setTimeInMillis(Long.parseLong(sessionModel.getStartTime()));
            startTimeEditText.setText(formatTime(startTimeCalendar));
        } else {
            startTimeEditText.setText("");
        }

        if (!sessionModel.getEndTime().isEmpty()) {
            endTimeCalendar.setTimeInMillis(Long.parseLong(sessionModel.getEndTime()));
            endTimeEditText.setText(formatTime(endTimeCalendar));
        } else {
            endTimeEditText.setText("");
        }

        updateDuration();
    }
    private void showTimePicker(Calendar calendar, TextInputEditText timeEditText, Runnable onTimeSetCallback) {
        new android.app.TimePickerDialog(
                getContext(),
                (view, hourOfDay, minute) -> {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                    timeEditText.setText(formatTime(calendar));
                    onTimeSetCallback.run();
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                DateFormat.is24HourFormat(getContext())
        ).show();
    }
    private String formatTime(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        return sdf.format(calendar.getTime());
    }
    private void updateDuration() {
        long durationInMillis = endTimeCalendar.getTimeInMillis() - startTimeCalendar.getTimeInMillis();
        if (durationInMillis > 0) {
            int minutes = (int) (durationInMillis / (1000 * 60));
            int hours = minutes / 60;
            minutes = minutes % 60;
            durationTextView.setText(String.format(Locale.getDefault(), "%02d:%02d", hours, minutes));
        } else {
            durationTextView.setText("00:00");
        }
    }
    private void saveSession() {
        if (sessionModel == null) {
            sessionModel = new EventSessionModel();
        }
        sessionModel.setStartTime(String.valueOf(startTimeCalendar.getTimeInMillis()));
        sessionModel.setEndTime(String.valueOf(endTimeCalendar.getTimeInMillis()));
        sessionModel.setDuration((int) (endTimeCalendar.getTimeInMillis() - startTimeCalendar.getTimeInMillis()));

        if (!isEditMode && getArguments().containsKey("eventId")) {
            sessionModel.setEventId(getArguments().getInt("eventId"));
        }

        EventDatabase db = new LocalEventDatabase(getContext());
        db.openDatabase();
        if (isEditMode) {
            db.updateSession(sessionModel);
        } else {
            db.createSession(sessionModel);
        }

        if (getTargetFragment() instanceof ViewActivityFragment) {
            ((DialogCloseListener) getTargetFragment()).handleDialogClose(getDialog());
        }

        dismiss();
    }
}
