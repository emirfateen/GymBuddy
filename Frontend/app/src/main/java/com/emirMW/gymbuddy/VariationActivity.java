package com.emirMW.gymbuddy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.view.Menu;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.emirMW.gymbuddy.model.Exercise;
import com.emirMW.gymbuddy.model.Routine;
import com.emirMW.gymbuddy.model.Variation;
import com.emirMW.gymbuddy.request.BaseApiService;
import com.emirMW.gymbuddy.request.UtilsAPI;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VariationActivity extends AppCompatActivity {
    private Context mContext;
    private BaseApiService mApiService;
    public VariationAdapter variationArrayAdapter;
    private ArrayList<Variation> listVariation = new ArrayList<>();
    private ListView variationListView = null;
    private Button newVar;
    public static String selectedVarId;
    private static final String KEY_VAR_ID = "selectedVarId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_variation);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.black)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Variation");

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

        variationListView = findViewById(R.id.variation_list_view);
        newVar = findViewById(R.id.newVarButton);

        newVar.setOnClickListener(v->{
            startActivity(new Intent(this, addVariationActivity.class));
        });

        SharedPreferences sharedPreferences = getSharedPreferences("Logged", Context.MODE_PRIVATE);
        String selectedRouId = sharedPreferences.getString("selectedRouId", "");

        if (!selectedRouId.isEmpty()) {
            getExercisesAndVariations(selectedRouId);
        } else {
            Toast.makeText(this, "No routine selected", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menu) {
        SharedPreferences sharedPreferences = getSharedPreferences("Logged", Context.MODE_PRIVATE);
        String selectedRouId = sharedPreferences.getString("selectedRouId", "");
        if (menu.getItemId() == R.id.deleteRouButton) {
            deleteRoutine(selectedRouId);
            return true;
        }else {
            return super.onOptionsItemSelected(menu);
        }
    }

    protected void getVariationList(Map<String, Exercise> exerciseMap, String selectedRouId) {
        SharedPreferences sharedPreferences = getSharedPreferences("Logged", Context.MODE_PRIVATE);
        mApiService.getAllVar(selectedRouId).enqueue(new Callback<ArrayList<Variation>>() {
            @Override
            public void onResponse(Call<ArrayList<Variation>> call, Response<ArrayList<Variation>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listVariation = response.body();
                    variationArrayAdapter = new VariationAdapter(mContext, listVariation, exerciseMap);
                    variationListView.setAdapter(variationArrayAdapter);

                    variationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Variation selectedVar = listVariation.get(position);
                            selectedVarId = selectedVar.getVarId();

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(KEY_VAR_ID, selectedVarId);
                            editor.apply();

                            Intent intent = new Intent(mContext, VariationDetailActivity.class);
                            mContext.startActivity(intent);
                        }
                    });
                } else {
                    Toast.makeText(mContext, "Failed to fetch variations", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Variation>> call, Throwable t) {
                Toast.makeText(mContext, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getExercisesAndVariations(String selectedRouId) {
        mApiService.getExercise().enqueue(new Callback<ArrayList<Exercise>>() {
            @Override
            public void onResponse(Call<ArrayList<Exercise>> call, Response<ArrayList<Exercise>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Exercise> exercises = response.body();
                    Map<String, Exercise> exerciseMap = new HashMap<>();
                    for (Exercise exercise : exercises) {
                        exerciseMap.put(exercise.getExeId(), exercise);
                    }
                    getVariationList(exerciseMap, selectedRouId);
                } else {
                    Toast.makeText(mContext, "Failed to fetch exercises", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Exercise>> call, Throwable t) {
                Toast.makeText(mContext, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteRoutine(String selectedRouId){
        mApiService.deleteRoutine(selectedRouId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Intent intent = new Intent(mContext, RoutineActivity.class);
                    mContext.startActivity(intent);
                    Toast.makeText(mContext, "Routine deleted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "Failed to delete routine: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(mContext, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }
}
