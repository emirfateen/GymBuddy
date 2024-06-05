package com.emirMW.gymbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.emirMW.gymbuddy.model.Routine;
import com.emirMW.gymbuddy.request.BaseApiService;
import com.emirMW.gymbuddy.request.UtilsAPI;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class addRoutineActivity extends AppCompatActivity {
    private BaseApiService mApiService;
    private Context mContext;
    private Button addButton, backButton;
    private EditText rouName, rouDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_routine);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.black)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Routine");

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

        mApiService = UtilsAPI.getAPIService();
        mContext = this;

        addButton = findViewById(R.id.addRoutineButton);
        rouName = findViewById(R.id.rounameText);
        rouDesc = findViewById(R.id.roudescText);

        addButton.setOnClickListener(v -> addRoutine());
    }

    private void addRoutine(){
        String routineName = rouName.getText().toString();
        String routineDescription= rouDesc.getText().toString();
        SharedPreferences sharedPreferences = getSharedPreferences("Logged", Context.MODE_PRIVATE);
        String userid = sharedPreferences.getString("userid", "");
        if (routineName.isEmpty()) {
            Toast.makeText(mContext, "Field cannot be empty", Toast.LENGTH_SHORT).show();
        }

        mApiService.createRoutine(userid, routineName, routineDescription).enqueue(new Callback<Routine>() {
            @Override
            public void onResponse(Call<Routine> call, Response<Routine> response) {
                if (response.isSuccessful()){
                    Routine routine = response.body();
                    Toast.makeText(mContext, "Routine added successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(addRoutineActivity.this, RoutineActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Routine> call, Throwable t) {
                Toast.makeText(mContext, "Failed to add routine: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }
}