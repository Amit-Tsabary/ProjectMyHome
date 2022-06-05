package com.example.amitproject11.ui.accounts;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.amitproject11.ButtonDisplay;
import com.example.amitproject11.CreateAccount;
import com.example.amitproject11.Networking;
import com.example.amitproject11.R;


public class AccountsFragment extends Fragment implements View.OnClickListener, ButtonDisplay {

    View view;
    // a standard method that runs whenever the view is created
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_accounts, container, false);
        Button button = (Button) view.findViewById(R.id.new_account);
        button.setOnClickListener(this);

        return view;
    }

    // a method inherited from ButtonDisplay that creates a button on this screen
    public Button createButton(String params)
    {
        LinearLayout layout = view.findViewById(R.id.accounts_layout);
        Button button = new Button(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(32, 28, 32, 0);

        button.setText(params.split("_")[1]);
        button.setBackgroundResource(R.drawable.button_design);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Networking("getAccounts", getActivity(), params.split("_")[0]).execute();
            }
        });

        layout.addView(button, layoutParams);
        return button;
    }

    public void onResume() {
        super.onResume();
        display();
    }
    //a method that displays all relevant buttons on the screen
    public void display(){
        LinearLayout layout = (LinearLayout)view.findViewById(R.id.accounts_layout);
        layout.removeAllViews();
        new Networking("displayAccounts", this).execute();
    }

    public void onDestroyView() {
        super.onDestroyView();
    }

    public void onClick(View view) {
        Intent intent = new Intent(getActivity(), CreateAccount.class);
        startActivity(intent);
    }
}