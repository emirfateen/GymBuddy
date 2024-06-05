package com.emirMW.gymbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.emirMW.gymbuddy.model.DayOfWeek;
import com.emirMW.gymbuddy.model.Reminder;
import com.emirMW.gymbuddy.model.Routine;
import com.emirMW.gymbuddy.request.BaseApiService;
import com.emirMW.gymbuddy.request.UtilsAPI;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class addReminderActivity extends AppCompatActivity {

    private BaseApiService mApiService;
    private Context mContext;
    private Button addButton, backButton;
    private EditText remName;
    private Spinner remTime;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.black)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Reminder");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_rem);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.bottom_exercise) {
                startActivity(new Intent(this, ExerciseActivity.class));
                finish();
                return true;
            } else if (id == R.id.bottom_rou) {
                startActivity(new Intent(this, RoutineActivity.class));
                finish();
                return true;
            } else if (id == R.id.bottom_rem) {
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

        addButton = findViewById(R.id.addReminderButton);
        remName = findViewById(R.id.remnameText);
        remTime = findViewById(R.id.days_spinner);

        DayOfWeek[] days = DayOfWeek.values();
        ArrayAdapter<DayOfWeek> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, days);
        remTime.setAdapter(adapter);

        addButton.setOnClickListener(v -> addReminder());
    }

    private void addReminder(){
        String reminderName = remName.getText().toString();
        DayOfWeek selectedDay = (DayOfWeek) remTime.getSelectedItem();
        String reminderTime = selectedDay.toString();

        SharedPreferences sharedPreferences = getSharedPreferences("Logged", Context.MODE_PRIVATE);
        String userid = sharedPreferences.getString("userid", "");
        if (reminderName.isEmpty()) {
            Toast.makeText(mContext, "Field cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        mApiService.createReminder(userid, reminderName, reminderTime).enqueue(new Callback<Reminder>() {
            @Override
            public void onResponse(Call<Reminder> call, Response<Reminder> response) {
                if (response.isSuccessful()){
                    Toast.makeText(mContext, "Reminder added successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(addReminderActivity.this, ReminderActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Reminder> call, Throwable t) {
                Toast.makeText(mContext, "Failed to add reminder: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}