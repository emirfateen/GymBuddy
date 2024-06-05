package com.emirMW.gymbuddy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.emirMW.gymbuddy.model.Exercise;
import com.emirMW.gymbuddy.model.Variation;

import java.util.ArrayList;
import java.util.Map;

public class VariationAdapter extends ArrayAdapter<Variation> {
    private Map<String, Exercise> exerciseMap;

    public VariationAdapter(Context context, ArrayList<Variation> variationList, Map<String, Exercise> exerciseMap) {
        super(context, 0, variationList);
        this.exerciseMap = exerciseMap;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View currentItemView = convertView;
        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.variation_view, parent, false);
        }

        Variation variation = getItem(position);

        if (variation != null) {
            TextView name = currentItemView.findViewById(R.id.name_text);
            TextView sets = currentItemView.findViewById(R.id.sets_text);
            TextView reps = currentItemView.findViewById(R.id.reps_text);

            Exercise exercise = exerciseMap.get(variation.getExeId());
            if (exercise != null) {
                name.setText(exercise.getName());
            } else {
                name.setText("Unknown Exercise");
            }

            sets.setText(variation.getSets());
            reps.setText(variation.getReps());
        }

        return currentItemView;
    }
}

