package com.emirMW.gymbuddy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.emirMW.gymbuddy.model.Exercise;
import com.emirMW.gymbuddy.model.Routine;
import com.emirMW.gymbuddy.request.BaseApiService;
import com.emirMW.gymbuddy.request.UtilsAPI;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoutineActivity extends AppCompatActivity {
    private Context mContext;
    private BaseApiService mApiService;
    public RoutineAdapter routineArrayAdapter;
    private ArrayList<Routine> listRoutine = new ArrayList<>();
    private ListView routineListView = null;
    private Button newRoutine;
    public static String selectedRouId;
    private static final String KEY_ROU_ID = "selectedRouId";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.black)));
        getSupportActionBar().setTitle("Routine");

        mApiService = UtilsAPI.getAPIService();
        mContext = this;

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

        routineListView = findViewById(R.id.routine_list_view);
        newRoutine = findViewById(R.id.newRouButton);

        newRoutine.setOnClickListener(v -> {
            startActivity(new Intent(this, addRoutineActivity.class));
        });

        getRoutineList();
    }

    private void getRoutineList(){
        SharedPreferences sharedPreferences = getSharedPreferences("Logged", Context.MODE_PRIVATE);
        String userid = sharedPreferences.getString("userid", "");
        mApiService.getAllRou(userid).enqueue(new Callback<ArrayList<Routine>>() {
            @Override
            public void onResponse(Call<ArrayList<Routine>> call, Response<ArrayList<Routine>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listRoutine = response.body();
                    routineArrayAdapter = new RoutineAdapter(mContext, listRoutine);
                    routineListView.setAdapter(routineArrayAdapter);

                    routineListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Routine selectedRou = listRoutine.get(position);
                            selectedRouId = selectedRou.getRoutineId();

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(KEY_ROU_ID, selectedRouId);
                            editor.apply();

                            Intent intent = new Intent(mContext, VariationActivity.class);
                            mContext.startActivity(intent);
                        }
                    });
                } else {
                    Toast.makeText(mContext, "Failed to fetch routines", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Routine>> call, Throwable t) {
                Toast.makeText(mContext, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
