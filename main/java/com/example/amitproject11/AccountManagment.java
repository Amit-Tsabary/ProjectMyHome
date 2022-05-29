package com.example.amitproject11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AccountManagment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_managment);
        ActionBar actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#4826e0"));
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        String[] extra = extras.getString("key").split("_");

        setTitle("Menage " + extra[1] + "'s Account");

        EditText firstName = findViewById(R.id.first_name_edit);
        firstName.setText(extra[1]);
        EditText lastName = findViewById(R.id.last_name_edit);
        lastName.setText(extra[2]);
        EditText email = findViewById(R.id.username_edit);
        email.setText(extra[3]);
        email.setEnabled(false);

        Activity fa = this;
        Button btnSignIn = findViewById(R.id.btn_submit_changes_account);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                EditText first_name = (EditText) findViewById(R.id.first_name_edit);
                EditText last_name = (EditText) findViewById(R.id.last_name_edit);
                String email = extra[3];
                EditText password = (EditText) findViewById(R.id.password_edit);
                TextView errmsg = findViewById(R.id.error_msg_crate_account);
                if ((first_name.getText().toString().length() == 0 || last_name.getText().toString().length() == 0)
                        || (email.length() == 0 || password.getText().toString().length() == 0))
                {
                    errmsg.setText(" Please fill all fields.");
                    errmsg.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_error_outline_24, 0, 0, 0);
                }
                finish();
                new Networking("editAccount_"+first_name.getText().toString()+"_"+last_name.getText().toString()+"_"+email + "_"+ password.getText().toString()).execute();
            }
        });


    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}