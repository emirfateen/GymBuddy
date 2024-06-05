package com.emirMW.gymbuddy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.emirMW.gymbuddy.model.Routine;

import java.util.ArrayList;

public class RoutineAdapter extends ArrayAdapter<Routine> {
    public RoutineAdapter(Context context, ArrayList<Routine> routineList) {
        super(context, 0, routineList);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View currentItemView = convertView;
        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.routine_view, parent, false);
        }

        Routine routine = getItem(position);

        if (routine != null) {
            TextView name = currentItemView.findViewById(R.id.name_text);
            TextView description = currentItemView.findViewById(R.id.description_text);

            name.setText(routine.getRouName());
            description.setText(routine.getDescription());
        }

        return currentItemView;
    }
}
