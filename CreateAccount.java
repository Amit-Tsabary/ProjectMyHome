package com.example.amitproject11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CreateAccount extends AppCompatActivity implements View.OnClickListener{
    public Activity fa;

    // a standard method that runs whenever the activity is created
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#4826e0"));
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("Create Account");
        setContentView(R.layout.activity_create_account);
        fa = this;

        Button btnSignIn = findViewById(R.id.btn_create);
        btnSignIn.setOnClickListener(this);

    }

    //runs when back arrow in pressed
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View view) {
        EditText first_name = findViewById(R.id.first_name);
        EditText last_name = findViewById(R.id.last_name);
        EditText email = findViewById(R.id.username_create);
        EditText password = findViewById(R.id.password_create);
        TextView errmsg = findViewById(R.id.error_msg_crate_account);
        if ((first_name.getText().toString().length() == 0 || last_name.getText().toString().length() == 0)
                || (email.getText().toString().length() == 0 || password.getText().toString().length() == 0))
        {
            errmsg.setText(" Please fill all fields.");
            errmsg.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_error_outline_24, 0, 0, 0);
        }
        else if (first_name.getText().toString().contains("_"))
        {
            errmsg.setText(" First name must not contain '_'.");
            errmsg.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_error_outline_24, 0, 0, 0);
        }
        else if (last_name.getText().toString().contains("_"))
        {
            errmsg.setText(" Last name must not contain '_'.");
            errmsg.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_error_outline_24, 0, 0, 0);
        }
        else if (password.getText().toString().contains("_"))
        {
            errmsg.setText(" Password must not contain '_'.");
            errmsg.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_error_outline_24, 0, 0, 0);
        }
        else if (!password.getText().toString().contains("@"))
        {
            errmsg.setText(" Password must not contain '_'.");
            errmsg.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_error_outline_24, 0, 0, 0);
        }
        else
        {
            new Networking("create_"+first_name.getText().toString()+"_"+last_name.getText().toString()+"_"+email.getText().toString() + "_"+ password.getText().toString(), fa).execute();
        }
    }
}