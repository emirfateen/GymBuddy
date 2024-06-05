package com.emirMW.gymbuddy;

import androidx.appcompat.app.AppCompatActivity;

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

import com.emirMW.gymbuddy.model.Reminder;
import com.emirMW.gymbuddy.model.Routine;
import com.emirMW.gymbuddy.model.Variation;
import com.emirMW.gymbuddy.request.BaseApiService;
import com.emirMW.gymbuddy.request.UtilsAPI;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReminderActivity extends AppCompatActivity {
    private Context mContext;
    private BaseApiService mApiService;
    public ReminderAdapter reminderArrayAdapter;
    private ArrayList<Reminder> listReminder = new ArrayList<>();
    private ListView reminderListView = null;
    private Button newReminder;
    public static String selectedRemId;
    private static final String KEY_ROU_ID = "selectedRemId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.black)));
        getSupportActionBar().setTitle("Reminder");

        mApiService = UtilsAPI.getAPIService();
        mContext = this;

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

        reminderListView = findViewById(R.id.reminder_list_view);
        newReminder = findViewById(R.id.newRemButton);

        newReminder.setOnClickListener(v -> {
            startActivity(new Intent(this, addReminderActivity.class));
        });

        getReminderList();
    }

    private void getReminderList(){
        SharedPreferences sharedPreferences = getSharedPreferences("Logged", Context.MODE_PRIVATE);
        String userid = sharedPreferences.getString("userid", "");
        mApiService.getAllRem(userid).enqueue(new Callback<ArrayList<Reminder>>() {
            @Override
            public void onResponse(Call<ArrayList<Reminder>> call, Response<ArrayList<Reminder>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listReminder = response.body();
                    reminderArrayAdapter = new ReminderAdapter(mContext, listReminder);
                    reminderListView.setAdapter(reminderArrayAdapter);

                    reminderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Reminder selectedRem = listReminder.get(position);
                            selectedRemId = selectedRem.getReminderId();

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(KEY_ROU_ID, selectedRemId);
                            editor.apply();

                            Intent intent = new Intent(mContext, ReminderDetailActivity.class);
                            mContext.startActivity(intent);
                        }
                    });
                } else {
                    Toast.makeText(mContext, "Failed to fetch routines", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Reminder>> call, Throwable t) {
                Toast.makeText(mContext, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}