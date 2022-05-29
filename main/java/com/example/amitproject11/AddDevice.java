package com.example.amitproject11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.amitproject11.ui.home.HomeFragment;

public class AddDevice extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public static Activity fa;
    private Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#4826e0"));
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("Add Device");
        setContentView(R.layout.activity_add_device);
        fa = this;

        Button btnSignIn = findViewById(R.id.btn_create_device);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                EditText device_name = (EditText) findViewById(R.id.device_name);
                EditText device_type = (EditText) findViewById(R.id.device_type);
                EditText device_pin = (EditText) findViewById(R.id.device_pin);
                TextView errmsg = findViewById(R.id.error_msg_add_device);
                if ((device_name.getText().toString().length() == 0 || device_type.getText().toString().length() == 0) ||  device_pin.getText().toString().length() == 0)
                {

                    errmsg.setText(" Please fill all fields.");
                    errmsg.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_error_outline_24, 0, 0, 0);
                }
                else
                {
                    try {
                        int pin = Integer.parseInt(device_pin.getText().toString());
                        if(pin < 2 || pin > 13) {
                            errmsg.setText(" pin must be between 2-13.");
                            errmsg.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_error_outline_24, 0, 0, 0);
                        }
                        else if (device_name.getText().toString().contains("_"))
                        {
                            errmsg.setText(" Device name must not contain '_'.");
                            errmsg.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_error_outline_24, 0, 0, 0);
                        }
                        else if (device_type.getText().toString().contains("_"))
                        {
                            errmsg.setText(" Device type must not contain '_'.");
                            errmsg.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_error_outline_24, 0, 0, 0);
                        }
                        else
                        {
                            finish();
                            new Networking("createDevice_" + device_name.getText().toString() + "_" + device_type.getText().toString() + "_" + device_pin.getText().toString(), fa).execute();
                        }
                    } catch (NumberFormatException e) {
                        errmsg.setText(" pin must be a numeric value.");
                        errmsg.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_error_outline_24, 0, 0, 0);
                    }
                }

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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String choice = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}