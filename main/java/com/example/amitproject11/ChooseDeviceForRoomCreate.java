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
import android.widget.LinearLayout;

import java.io.Serializable;

public class ChooseDeviceForRoomCreate extends AppCompatActivity  implements Serializable, ButtonDisplay{
    //private ArrayList devicesInRoom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_device_for_room_create);

        ActionBar actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#4826e0"));
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("Choose Device");

        //devicesInRoom = (ArrayList) getIntent().getSerializableExtra("key");

        new Networking("displayDevices", (ButtonDisplay) this, CreateRoom.devicesInRoom).execute();
    }
    @Override
    public Button createButton(String params)
    {
        LinearLayout layout = (LinearLayout)findViewById(R.id.choose_room_button_layout_create);
        Button button = new Button(this);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(32, 28, 32, 0);

        button.setText(params.split("_")[1]);

        button.setBackgroundResource(R.drawable.button_design);

        Activity activity = this;
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                CreateRoom.devicesInRoom.add(Integer.parseInt(params.split("_")[0]));
                new Networking("getDevices", activity, params.split("_")[0], CreateRoom.devicesInRoom).execute();
                finish();
            }
        });
        layout.addView(button, layoutParams);
        return button;
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