package com.emirMW.gymbuddy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.emirMW.gymbuddy.model.Exercise;

import java.util.ArrayList;

public class ExerciseAdapter extends ArrayAdapter<Exercise> {

    public ExerciseAdapter(Context context, ArrayList<Exercise> exerciseList) {
        super(context, 0, exerciseList);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View currentItemView = convertView;
        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.exercise_view, parent, false);
        }

        Exercise exercise = getItem(position);

        if (exercise != null) {
            TextView name = currentItemView.findViewById(R.id.name_text);
            TextView muscle = currentItemView.findViewById(R.id.muscle_text);

            name.setText(exercise.getName());
            muscle.setText(exercise.getMuscle());
        }

        return currentItemView;
    }
}
