package com.example.amitproject11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AccountManagement extends AppCompatActivity implements View.OnClickListener{

    // a standard method that runs whenever the activity is created
    String emailAddress;
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
        emailAddress = extra[3];
        email.setText(emailAddress);
        email.setEnabled(false);

        Button btnSignIn = findViewById(R.id.btn_submit_changes_account);
        btnSignIn.setOnClickListener(this);

        Button delete = findViewById(R.id.delete_account);
        if(extra[0].equals("1"))
        {
            delete.setEnabled(false);
            delete.setVisibility(View.GONE);
        }
        else {
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!extra[0].equals("1")) {
                        new Networking("deleteAccount_" + extra[0]).execute();
                        finish();
                    }
                }
            });
        }

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

    //runs when the submit changes button is pressed
    public void onClick(View view) {
        EditText first_name = findViewById(R.id.first_name_edit);
        EditText last_name = findViewById(R.id.last_name_edit);
        EditText password = findViewById(R.id.password_edit);
        TextView errmsg = findViewById(R.id.error_msg_crate_account);
        if ((first_name.getText().toString().length() == 0 || last_name.getText().toString().length() == 0)
                || (emailAddress.length() == 0 || password.getText().toString().length() == 0))
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
        else
        {
            finish();
            new Networking("editAccount_"+first_name.getText().toString()+"_"+last_name.getText().toString()+"_"+emailAddress + "_"+ password.getText().toString()).execute();
        }
    }
}