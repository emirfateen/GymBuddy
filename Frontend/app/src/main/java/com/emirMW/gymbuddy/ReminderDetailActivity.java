package com.emirMW.gymbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.emirMW.gymbuddy.model.DayOfWeek;
import com.emirMW.gymbuddy.model.Reminder;
import com.emirMW.gymbuddy.model.Variation;
import com.emirMW.gymbuddy.request.BaseApiService;
import com.emirMW.gymbuddy.request.UtilsAPI;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import kotlin.collections.SlidingWindowKt;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReminderDetailActivity extends AppCompatActivity {
    private BaseApiService mApiService;
    private Context mContext;
    private Button backButton, saveButton;
    private TextView deleteRem;
    private EditText remName;
    private Spinner remTime;
    private Switch switchNotif;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_detail);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.black)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Reminder Detail");

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

        mContext = this;
        mApiService = UtilsAPI.getAPIService();

        remName = findViewById(R.id.remName);
        remTime = findViewById(R.id.days_spinner);
        setupSpinner();

        saveButton = findViewById(R.id.saveButton);
        deleteRem = findViewById(R.id.deleteRem);
        switchNotif = findViewById(R.id.switchNotif);

        SharedPreferences sharedPreferences = getSharedPreferences("Logged", Context.MODE_PRIVATE);
        String selectedRemId = sharedPreferences.getString("selectedRemId", "");

        getReminderDetail(selectedRemId);

        deleteRem.setOnClickListener(v -> {
            deleteReminder(selectedRemId);
        });

        saveButton.setOnClickListener(v -> {
            updateReminder(selectedRemId);
            Intent intent = new Intent(mContext, ReminderActivity.class);
            startActivity(intent);
        });
    }
    private void setupSpinner() {
        DayOfWeek[] days = DayOfWeek.values();
        ArrayAdapter<DayOfWeek> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, days);
        remTime.setAdapter(adapter);
    }
    private void getReminderDetail(String selectedRemId) {
        mApiService.getDetailReminder(selectedRemId).enqueue(new Callback<Reminder>() {
            @Override
            public void onResponse(Call<Reminder> call, Response<Reminder> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Reminder reminder = response.body();
                    remName.setText(reminder.getRemName());
                    setSpinnerToValue(reminder.getReminder_time());
                } else {
                    Log.e("ReminderDetail", "No details found, Status Code: " + response.code());
                    Toast.makeText(mContext, "No reminder details found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Reminder> call, Throwable t) {
                Toast.makeText(mContext, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setSpinnerToValue(String reminderTime) {
        ArrayAdapter<DayOfWeek> adapter = (ArrayAdapter<DayOfWeek>) remTime.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).toString().equalsIgnoreCase(reminderTime)) {
                remTime.setSelection(i);
                break;
            }
        }
    }

    private void deleteReminder(String selectedRemId){
        mApiService.deleteReminder(selectedRemId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(mContext, "Reminder deleted successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mContext, ReminderActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(mContext, "Failed to delete reminder: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(mContext, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private void updateReminder(String selectedRemId){
        DayOfWeek selectedDay = (DayOfWeek) remTime.getSelectedItem();
        String reminderTime = selectedDay.toString();
        String remNameS = remName.getText().toString();

        mApiService.updateReminder(selectedRemId, remNameS, reminderTime).enqueue(new Callback<Reminder>() {
            @Override
            public void onResponse(Call<Reminder> call, Response<Reminder> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(mContext, "Update Success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "Update Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Reminder> call, Throwable t) {
                Toast.makeText(mContext, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }
}