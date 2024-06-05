package com.emirMW.gymbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.emirMW.gymbuddy.model.Exercise;
import com.emirMW.gymbuddy.model.LoginResponse;
import com.emirMW.gymbuddy.model.Variation;
import com.emirMW.gymbuddy.request.BaseApiService;
import com.emirMW.gymbuddy.request.UtilsAPI;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VariationDetailActivity extends AppCompatActivity {
    private BaseApiService mApiService;
    private Context mContext;
    private Button backButton, saveButton;
    private EditText setsNum, repsNum;
    private TextView exeName, deleteVar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_variation_detail);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.black)));
        getSupportActionBar().setTitle("Variation Detail");

        mContext = this;
        mApiService = UtilsAPI.getAPIService();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_rou);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.bottom_exercise) {
                startActivity(new Intent(this, ExerciseActivity.class));
                finish();
                return true;
            } else if (id == R.id.bottom_rou) {
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

        exeName = findViewById(R.id.exeName);
        setsNum = findViewById(R.id.setsNumber);
        repsNum = findViewById(R.id.repsNumber);
        deleteVar = findViewById(R.id.deleteVar);
        saveButton = findViewById(R.id.saveButton);
        backButton = findViewById(R.id.backButton);

        SharedPreferences sharedPreferences = getSharedPreferences("Logged", Context.MODE_PRIVATE);
        String selectedVarId = sharedPreferences.getString("selectedVarId", "");

        getVariationDetail(selectedVarId);

        // Delete Variation
        deleteVar.setOnClickListener(v -> {
            deleteVariation(selectedVarId);
        });

        // Update Variation
        saveButton.setOnClickListener(v -> {
            updateVariation(selectedVarId);
            Intent intent = new Intent(mContext, VariationActivity.class);
            startActivity(intent);
        });

        // Back to Variation Activity and Cancel Update
        backButton.setOnClickListener(v->{
            Intent intent = new Intent(mContext, VariationActivity.class);
            startActivity(intent);
        });

    }

    private void getVariationDetail(String selectedVarId) {
        mApiService.getDetailVariation(selectedVarId).enqueue(new Callback<Variation>() {
            @Override
            public void onResponse(Call<Variation> call, Response<Variation> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Variation variation = response.body();
                    setsNum.setText(variation.getSets());
                    repsNum.setText(variation.getReps());
                    fetchExerciseDetails(variation.getExeId());
                } else {
                    Toast.makeText(mContext, "No variation details found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Variation> call, Throwable t) {
                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchExerciseDetails(String exeId) {
        mApiService.getExerciseById(exeId).enqueue(new Callback<Exercise>() {
            @Override
            public void onResponse(Call<Exercise> call, Response<Exercise> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Exercise exercise = response.body();
                    exeName.setText(exercise.getName());
                } else {
                    Toast.makeText(mContext, "No exercise details found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Exercise> call, Throwable t) {
                Toast.makeText(mContext, "Failed to retrieve exercise: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteVariation(String selectedVarId){
        mApiService.deleteVariation(selectedVarId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(mContext, "Variation deleted successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mContext, VariationActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(mContext, "Failed to delete variation: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(mContext, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private void updateVariation(String selectedVarId){
        String setS = setsNum.getText().toString();
        String repS = repsNum.getText().toString();

        mApiService.updateVariation(selectedVarId, setS, repS).enqueue(new Callback<Variation>() {
            @Override
            public void onResponse(Call<Variation> call, Response<Variation> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(mContext, "Update Success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "Update Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Variation> call, Throwable t) {
                Toast.makeText(mContext, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

}