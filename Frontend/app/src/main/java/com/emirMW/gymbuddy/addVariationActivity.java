package com.emirMW.gymbuddy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.media.CamcorderProfile;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.emirMW.gymbuddy.model.Exercise;
import com.emirMW.gymbuddy.model.Variation;
import com.emirMW.gymbuddy.request.BaseApiService;
import com.emirMW.gymbuddy.request.UtilsAPI;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class addVariationActivity extends AppCompatActivity {
    private Spinner exerciseSpinner;
    private BaseApiService mApiService;
    private Context mContext;
    private Button saveButton;
    private EditText setsInput, repsInput;
    private Exercise selectedExe;
    private List<Exercise> exercises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_variation);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.black)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Variation");

        mApiService = UtilsAPI.getAPIService();
        mContext = this;

        exerciseSpinner = findViewById(R.id.exercise_spinner);
        setsInput = findViewById(R.id.setsNumber);
        repsInput = findViewById(R.id.repsNumber);
        saveButton = findViewById(R.id.saveButton);

        getExercises();

        exerciseSpinner.setOnItemSelectedListener(setExe());

        saveButton.setOnClickListener(view -> addVariation());
    }

    private void getExercises() {
        mApiService.getExercise().enqueue(new Callback<ArrayList<Exercise>>() {
            @Override
            public void onResponse(Call<ArrayList<Exercise>> call, Response<ArrayList<Exercise>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    exercises = response.body();
                    List<String> exerciseNames = new ArrayList<>();

                    for (Exercise exercise : exercises) {
                        exerciseNames.add(exercise.getName());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(addVariationActivity.this,
                            android.R.layout.simple_spinner_item, exerciseNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    exerciseSpinner.setAdapter(adapter);
                } else {
                    Toast.makeText(mContext
                            , "Failed to fetch exercises", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Exercise>> call, Throwable t) {
                Toast.makeText(mContext, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected AdapterView.OnItemSelectedListener setExe() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (exercises != null && position >= 0 && position < exercises.size()) {
                    selectedExe = exercises.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedExe = null;
            }
        };
    }

    private void addVariation() {
        if (selectedExe == null) {
            Toast.makeText(this, "Please select an exercise", Toast.LENGTH_SHORT).show();
            return;
        }

        String exeId = selectedExe.getExeId();
        String sets = setsInput.getText().toString();
        String reps = repsInput.getText().toString();

        if (sets.isEmpty() || reps.isEmpty()) {
            Toast.makeText(this, "Please fill in sets and reps", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences sharedPreferences = getSharedPreferences("Logged", Context.MODE_PRIVATE);
        String selectedRouId = sharedPreferences.getString("selectedRouId", "");

        mApiService.createVariation(exeId, selectedRouId, sets, reps).enqueue(new Callback<Variation>() {
            @Override
            public void onResponse(Call<Variation> call, Response<Variation> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(mContext, "Variation added successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mContext, VariationActivity.class);
                    mContext.startActivity(intent);
                } else {
                    Toast.makeText(mContext, "Failed to add variation", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Variation> call, Throwable t) {
                Toast.makeText(mContext, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
