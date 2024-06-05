package com.emirMW.gymbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.emirMW.gymbuddy.model.Reminder;

import java.util.ArrayList;

public class ReminderAdapter extends ArrayAdapter<Reminder> {
    public ReminderAdapter(Context context, ArrayList<Reminder> reminderList) {
        super(context, 0, reminderList);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View currentItemView = convertView;
        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.reminder_view, parent, false);
        }

        Reminder reminder = getItem(position);

        if (reminder != null) {
            TextView name = currentItemView.findViewById(R.id.name_text);
            TextView reminder_time = currentItemView.findViewById(R.id.days_text);

            name.setText(reminder.getRemName());
            reminder_time.setText(reminder.getReminder_time());
        }

        return currentItemView;
    }
}