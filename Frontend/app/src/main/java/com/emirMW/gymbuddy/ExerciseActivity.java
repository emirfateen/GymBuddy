package com.emirMW.gymbuddy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.emirMW.gymbuddy.model.Exercise;
import com.emirMW.gymbuddy.model.Variation;
import com.emirMW.gymbuddy.request.BaseApiService;
import com.emirMW.gymbuddy.request.UtilsAPI;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExerciseActivity extends AppCompatActivity {
    private Context mContext;
    private BaseApiService mApiService;
    public ExerciseAdapter exerciseArrayAdapter;
    private ArrayList<Exercise> listExercise = new ArrayList<>();
    private ListView exerciseListView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.black)));
        getSupportActionBar().setTitle("Exercise");

        mApiService = UtilsAPI.getAPIService();
        mContext = this;

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_exercise);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.bottom_exercise) {
                return true;
            } else if (id == R.id.bottom_rou) {
                startActivity(new Intent(this, RoutineActivity.class));
                finish();
                return true;
            } else if (id == R.id.bottom_rem) {
                startActivity(new Intent(this, ReminderActivity.class));
                finish();
                return true;
            } else if (id == R.id.bottom_pro) {
                startActivity(new Intent(this, ProfileActivity.class));
                finish();
                return true;
            }
            return false;
        });


        exerciseListView = findViewById(R.id.exercise_list_view);
        getExerciseList();
    }
    protected void getExerciseList(){
        mApiService.getExercise().enqueue(new Callback<ArrayList<Exercise>>() {
            @Override
            public void onResponse(Call<ArrayList<Exercise>> call, Response<ArrayList<Exercise>> response) {
                if (response.isSuccessful()){
                    listExercise = response.body();
                    exerciseArrayAdapter = new ExerciseAdapter(mContext, listExercise);
                    exerciseListView.setAdapter(exerciseArrayAdapter);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Exercise>> call, Throwable t) {

            }
        });
    }
}
