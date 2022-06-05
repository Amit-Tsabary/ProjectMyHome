package com.example.amitproject11;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;

public class ManageRoom extends AppCompatActivity implements ButtonDisplay{
    public static ArrayList<Integer> devicesInRoom;

    // a standard method that runs whenever the activity is created
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_room);

        Bundle extras = getIntent().getExtras();
        String s = extras.getString("key");
        String[] extra = s.split("__");

        devicesInRoom = RoomEdit.toArrayList(extra[2]);

        TextView roomName = (TextView) findViewById(R.id.room_name_manage);
        roomName.setText(extra[1]+":");

        ActionBar actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#4826e0"));
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("Room");
        Activity a = this;
        ImageButton edit = findViewById(R.id.edit_room);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(a, RoomEdit.class);
                intent.putExtra("key", extra[0]+"__"+extra[1]+"__"+CreateRoom.splitArrayList(devicesInRoom));
                a.startActivity(intent);
            }
        });

        Button turnAllOn = findViewById(R.id.turn_all_on);
        turnAllOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Networking("turnOnAll_"+CreateRoom.splitArrayList(devicesInRoom)).execute();
                onResume();
            }
        });
        Button turnAllOff = findViewById(R.id.turn_all_off);
        turnAllOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Networking("turnOffAll_"+CreateRoom.splitArrayList(devicesInRoom)).execute();
                onResume();
            }
        });
        Button delete = findViewById(R.id.delete_room);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Networking("deleteRoom_"+extra[0]).execute();
                finish();
            }
        });

    }

    // a method inherited from ButtonDisplay that creates a button on this screen
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Button createButton(String params)
    {
        LinearLayout layout = findViewById(R.id.room_devices_layout_manage);
        Button button = new Button(this);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(32, 28, 32, 0);

        button.setText(params.split("_")[1]);
        if (params.split("_")[4].equals("off"))
        {
            button.setBackgroundResource(R.drawable.button_design_off);
        }
        else
        {
            button.setBackgroundResource(R.drawable.button_design);
        }
        ButtonDisplay b = this;
        Activity a = this;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Networking("getDevices", a, params.split("_")[0], b).execute();
            }
        });
        layout.addView(button, layoutParams);
        return button;
    }


    @Override
    protected void onResume() {
        super.onResume();
        display();
    }
    //a method that displays all relevant buttons on the screen
    public void display()
    {
        LinearLayout layout = findViewById(R.id.room_devices_layout_manage);
        layout.removeAllViews();
        new Networking("displayDevById_"+CreateRoom.splitArrayList(devicesInRoom), (ButtonDisplay) this).execute();
    }

    //runs when back arrow in pressed
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                devicesInRoom = new ArrayList<>();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}