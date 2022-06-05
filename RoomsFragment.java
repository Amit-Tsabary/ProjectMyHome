package com.example.amitproject11.ui.rooms;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.amitproject11.ButtonDisplay;
import com.example.amitproject11.Networking;
import com.example.amitproject11.R;
import com.example.amitproject11.CreateRoom;

public class RoomsFragment extends Fragment implements View.OnClickListener, ButtonDisplay {

    View view;
    // a standard method that runs whenever the view is created
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_rooms, container, false);
        Button button = view.findViewById(R.id.new_room);
        button.setOnClickListener(this);

        return view;

    }

    public Button createButton(String params)
    {
        LinearLayout layout = view.findViewById(R.id.rooms_layout);
        Button button = new Button(getContext());

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(32, 28, 32, 0);
        try {
            button.setText(params.split("__")[1]);
        }catch (Exception e){}
        button.setBackgroundResource(R.drawable.button_design);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Networking("getRooms", getActivity(), params.split("__")[0]).execute();
            }
        });
        layout.addView(button, layoutParams);
        return button;
    }

    //a method that displays all relevant buttons on the screen
    public void display()
    {
        LinearLayout layout = (LinearLayout)view.findViewById(R.id.rooms_layout);
        layout.removeAllViews();
        new Networking("displayRooms", this).execute();
    }


    @Override
    public void onResume() {
        super.onResume();
        display();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getActivity(), CreateRoom.class);
        startActivity(intent);
    }
}
