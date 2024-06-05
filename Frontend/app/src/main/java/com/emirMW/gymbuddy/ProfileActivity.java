package com.emirMW.gymbuddy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.emirMW.gymbuddy.LoginActivity;
import com.emirMW.gymbuddy.model.Exercise;
import com.emirMW.gymbuddy.request.BaseApiService;
import com.emirMW.gymbuddy.request.UtilsAPI;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    private TextView username, email;
    private Button logout;
    private Context mContext;
    private BaseApiService mApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.black)));
        getSupportActionBar().setTitle("Profile");

        mApiService = UtilsAPI.getAPIService();
        mContext = this;

        username = findViewById(R.id.usernameText);
        email = findViewById(R.id.emailText);
        logout = findViewById(R.id.logout_button);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_pro);

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
                startActivity(new Intent(this, ReminderActivity.class));
                finish();
                return true;
            } else if (id == R.id.bottom_pro) {
                return true;
            }
            return false;
        });

        SharedPreferences sharedPreferences = getSharedPreferences("Logged", Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString("username", "");
        String userEmail = sharedPreferences.getString("email", "");

        username.setText(userName);
        email.setText(userEmail);

        logout.setOnClickListener(v -> logout());
    }

    private void logout() {
        mApiService.logout().enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(mContext, "Logout Success", Toast.LENGTH_SHORT).show();
                    SharedPreferences sharedPreferences = getSharedPreferences("Logged", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();

                    Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(mContext, "Logout failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(mContext, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
