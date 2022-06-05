package com.example.amitproject11.ui.login;

import android.app.Activity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.amitproject11.Networking;
import com.example.amitproject11.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public static Activity fa;

    // a standard method that runs whenever the activity is created
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        fa = this;
        setContentView(R.layout.activity_login);

        Button btnSignIn = findViewById(R.id.btnLogin);
        btnSignIn.setOnClickListener(this);
    }
    public void onBackPressed() {
    }

    //runs when the login button is pressed
    public void onClick(View view) {
        EditText email = findViewById(R.id.username);
        EditText password = findViewById(R.id.password);
        new Networking("login_"+email.getText().toString() + "_"+ password.getText().toString(), fa).execute();

    }
}