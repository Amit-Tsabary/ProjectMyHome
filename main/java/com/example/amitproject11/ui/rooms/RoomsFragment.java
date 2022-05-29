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
import com.example.amitproject11.MainActivity;
import com.example.amitproject11.Networking;
import com.example.amitproject11.R;
import com.example.amitproject11.CreateRoom;
import com.example.amitproject11.databinding.FragmentSettingsBinding;

import java.util.ArrayList;

public class RoomsFragment extends Fragment implements View.OnClickListener, ButtonDisplay {

    private FragmentSettingsBinding binding;
    View view;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_rooms, container, false);
        Button button = (Button) view.findViewById(R.id.new_room);
        button.setOnClickListener(this);
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return view;

    }

    public Button createButton(String params)
    {
        LinearLayout layout = (LinearLayout)view.findViewById(R.id.rooms_layout);
        Button button = new Button(getContext());

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(32, 28, 32, 0);

        button.setText(params.split("__")[1]);

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
        binding = null;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getActivity(), CreateRoom.class);
        startActivity(intent);
    }
}
