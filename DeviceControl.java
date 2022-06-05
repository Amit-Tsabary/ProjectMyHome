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
import android.widget.CompoundButton;
import android.widget.Switch;

public class DeviceControl extends AppCompatActivity{

    // a standard method that runs whenever the activity is created
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_control);

        ActionBar actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#4826e0"));
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        String[] extra = extras.getString("key").split("_");
        String info = "";
        String status = "";
        if (extras != null) {
            info = extra[1];
            status = extra[4];
            //The key argument here must match that used in the other activity
        }
        setTitle("Control Device " + info);


        CompoundButton simpleSwitch1 = (Switch) findViewById(R.id.switch1);
        if (status.equals("on"))
            simpleSwitch1.setChecked(true);

        simpleSwitch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    new Networking("turnOn_"+extra[3]).execute();
                } else {
                    new Networking("turnOff_"+extra[3]).execute();
                }
            }
        });

        Button delete = findViewById(R.id.delete_device);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Networking("deleteDevice_"+extra[0]).execute();
                finish();
            }
        });
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

}