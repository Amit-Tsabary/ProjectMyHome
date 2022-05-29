package com.example.amitproject11.ui.login;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.amitproject11.MainActivity;
import com.example.amitproject11.Networking;
import com.example.amitproject11.R;
import com.example.amitproject11.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;
    public static Activity fa;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        fa = this;
        setContentView(R.layout.activity_login);

        Button btnSignIn = findViewById(R.id.btnLogin);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                EditText email = (EditText) findViewById(R.id.username);
                EditText password = (EditText) findViewById(R.id.password);

                new Networking("login_"+email.getText().toString() + "_"+ password.getText().toString(), fa).execute();

            }
        });
    }
    public void onBackPressed() {
    }
}