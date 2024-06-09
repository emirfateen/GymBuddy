package com.emirMW.gymbuddy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.emirMW.gymbuddy.model.User;
import com.emirMW.gymbuddy.request.BaseApiService;
import com.emirMW.gymbuddy.request.UtilsAPI;

import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    private BaseApiService mApiService;
    private Context mContext;
    private EditText name, email, password;
    private Button register = null;
    private TextView login;
    public static final Pattern EMAIL_ADDRESS
            = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-]{1,256}" + "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+"
    );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().hide();

        mContext = this;
        mApiService = UtilsAPI.getAPIService();

        name = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.registerButton);
        login = findViewById(R.id.loginButton);

        register.setOnClickListener(v ->{
            handleRegister();
        });
        login.setOnClickListener(v -> {
            moveActivity(this, LoginActivity.class);
        });
    }

    private void moveActivity(Context ctx, Class <?> cls){
        Intent intent = new Intent(ctx,cls);
        startActivity(intent);
    }

    protected void handleRegister() {
        String userName = name.getText().toString();
        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();
        if (userName.isEmpty() || userEmail.isEmpty() || userPassword.isEmpty()) {
            Toast.makeText(mContext, "Please fill all the fields",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (!EMAIL_ADDRESS.matcher(userEmail).matches()) {
            Toast.makeText(mContext, "Invalid email format", Toast.LENGTH_SHORT).show();
            return;
        }
        if (userPassword.length() < 8) {
            Toast.makeText(mContext, "Password must be at least 8 characters long.", Toast.LENGTH_SHORT).show();
            return;
        }
        mApiService.register(userName, userEmail, userPassword).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();

                    Toast.makeText(mContext, "Sign Up Success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(mContext, "Sign Up Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(mContext, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }
}
