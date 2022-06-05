package com.example.amitproject11;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class RoomEdit extends AppCompatActivity implements ButtonDisplay{

    public static ArrayList<Integer> devicesInRoom;

    // a standard method that runs whenever the activity is created
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Bundle extras = getIntent().getExtras();
        String[] extra = extras.getString("key").split("__");
        //The key argument here must match that used in the other activity

        devicesInRoom = toArrayList(extra[2]);

        setContentView(R.layout.activity_room_edit);
        ActionBar actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#4826e0"));
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("Edit Room");

        EditText rename = findViewById(R.id.rename_room);
        rename.setText(extra[1]);

        Activity a = this;
        ImageButton addDeviceToRoom = findViewById(R.id.add_new_device_to_edit_room);
        addDeviceToRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(a, ChooseDevicesForRoomEdit.class);
                //intent.putExtra("key",extras.getString("key"));
                a.startActivity(intent);
            }
        });

        Button submitChanges = findViewById(R.id.btn_submit_changes);
        submitChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Integer> uniqueDevcices  = CreateRoom.removeDuplicates(devicesInRoom);
                ManageRoom.devicesInRoom = new ArrayList<>(devicesInRoom);
                String toSend = CreateRoom.splitArrayList(uniqueDevcices);
                EditText room_name = (EditText) findViewById(R.id.rename_room);

                if (room_name.getText().toString().length() == 0)
                {
                    TextView errmsg = findViewById(R.id.error_msg_edit_room);
                    errmsg.setText(" Please enter room name.");
                    errmsg.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_error_outline_24, 0, 0, 0);
                }
                else if (devicesInRoom.size() == 0)
                {
                    TextView errmsg = findViewById(R.id.error_msg_edit_room);
                    errmsg.setText(" Cannot save empty room.");
                    errmsg.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_error_outline_24, 0, 0, 0);
                }
                else if(room_name.getText().toString().contains("_"))
                {
                    TextView errmsg = findViewById(R.id.error_msg_create_room);
                    errmsg.setText(" Room name must not contain '_'.");
                    errmsg.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_error_outline_24, 0, 0, 0);
                }
                else
                {
                    finish();
                    new Networking("editRoom_"+extra[0]+"_"+room_name.getText().toString()+"__"+toSend).execute();
                }

            }
        });
    }

    // a method inherited from ButtonDisplay that creates a button on this screen
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Button createButton(String params)
    {
        LinearLayout layout = (LinearLayout)findViewById(R.id.room_devices_layout_edit);
        //layout.setVerticalGravity(Gravity.CENTER_VERTICAL);
        layout.setBackgroundResource(R.drawable.button_design);
        LinearLayout room_devices = new LinearLayout(this);
        room_devices.setPadding(26, 0, 26, 20);
        //Button button = new Button(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(32, 28, 32, 0);

        TextView deviceName = new TextView(this);

        deviceName.setText(params.split("_")[1]);
        deviceName.setTextColor(getResources().getColor(R.color.texts));
        //deviceName.setBackgroundResource(R.drawable.button_design);
        deviceName.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        Typeface typeface = getResources().getFont(R.font.open_sans);
        deviceName.setTypeface(typeface);

        ImageButton btn = new ImageButton(this);
        btn.setImageResource(R.drawable.ic_baseline_remove_24);
        btn.setBackground(null);
        btn.setPadding(680-25*params.split("_")[1].length(), 0, 100, 0);
        btn.setForegroundGravity(Gravity.END);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                devicesInRoom.remove(Integer.valueOf(Integer.parseInt(params.split("_")[0])));
                display();
                layout.setBackground(null);
            }
        });
        room_devices.addView(deviceName);
        room_devices.addView(btn);

        layout.addView(room_devices, layoutParams);

        return new Button(this);
    }




    @Override
    protected void onResume() {
        super.onResume();
        display();
    }

    public void display()
    {
        TextView errmsg = findViewById(R.id.error_msg_edit_room);
        errmsg.setText("");
        errmsg.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        LinearLayout layout = (LinearLayout)findViewById(R.id.room_devices_layout_edit);
        layout.removeAllViews();
        new Networking("displayDevById_"+CreateRoom.splitArrayList(devicesInRoom), (ButtonDisplay) this).execute();
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
    public static ArrayList<Integer> toArrayList(String s)
    {
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        String[] arr = s.split("_");
        for (int i = 0; i<arr.length; i++)
        {
            arrayList.add(Integer.parseInt(arr[i]));
        }
        return arrayList;
    }


}