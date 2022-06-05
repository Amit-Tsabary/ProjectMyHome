package com.example.amitproject11.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.amitproject11.AddDevice;
import com.example.amitproject11.ButtonDisplay;
import com.example.amitproject11.MainActivity;
import com.example.amitproject11.Networking;
import com.example.amitproject11.R;

public class HomeFragment extends Fragment implements View.OnClickListener, ButtonDisplay {

    View view;
    // a standard method that runs whenever the view is created
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);
        ImageButton addDevice = view.findViewById(R.id.add_new_device);
        addDevice.setOnClickListener(this);
        TextView text = view.findViewById(R.id.welcome);
        text.setText("Welcome " + MainActivity.welcomeName + "!");
        return view;
    }

    // a method inherited from ButtonDisplay that creates a button on this screen
    public Button createButton(String params)
    {
        LinearLayout layout = view.findViewById(R.id.button_layout);
        Button button = new Button(getContext());

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
        button.setAllCaps(false);
        HomeFragment h = this;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Networking("getDevices", getActivity(), params.split("_")[0], h).execute();
            }
        });
        layout.addView(button, layoutParams);
        return button;
    }
    //a method that displays all relevant buttons on the screen
    public void display()
    {
        LinearLayout layout = view.findViewById(R.id.button_layout);
        layout.removeAllViews();
        new Networking("displayDevices", this).execute();
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
        Intent intent = new Intent(getActivity(), AddDevice.class);
        startActivity(intent);
    }

}