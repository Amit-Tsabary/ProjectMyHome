package com.example.amitproject11;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class DoubleAuthentication extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_double_authentication);

        ActionBar actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#4826e0"));
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setTitle("Double Authentication");
        Bundle extras = getIntent().getExtras();
        String extra = extras.getString("key");
        String id = extra.split("_")[2];
        String email = extra.split("_")[1];

        TextView header = findViewById(R.id.authentication_text);
        header.setText("Please enter the 6-digit code you received on: "+email);

        new Networking("authenticate_"+email+"_"+id, this).execute();
        Activity a = this;
        Button verify = findViewById(R.id.verify);
        verify.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                EditText code = (EditText) findViewById(R.id.code);
                new Networking("checkCode_"+code.getText().toString()+"_"+id, a).execute();
            }
        });


    }
}