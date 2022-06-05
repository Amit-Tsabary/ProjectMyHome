package com.example.amitproject11.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.amitproject11.Networking;
import com.example.amitproject11.R;
import com.example.amitproject11.ui.login.LoginActivity;

public class SettingsFragment extends Fragment {

    private static String[] extras ;
    View view;

    // a standard method that runs whenever the view is created
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_settings, container, false);
        Button signOut = view.findViewById(R.id.sign_out);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        try {
            if (getActivity().getIntent().getExtras() != null)
                extras = getActivity().getIntent().getExtras().getString("key").split("_");
        }catch (Exception e){}

        CompoundButton simpleSwitch1 = view.findViewById(R.id.double_authentication);
        menageSwitch(simpleSwitch1, extras[3], Integer.parseInt(extras[2]));
        return view;
    }
    public void menageSwitch(CompoundButton simpleSwitch1, String want_active, int id)
    {
        if (want_active.equals("true"))
            simpleSwitch1.setChecked(true);

        simpleSwitch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    new Networking("DATrue_"+id).execute();
                } else {
                    new Networking("DAFalse_"+id).execute();
                }
            }
        });
    }

    //ru
    public void onDestroyView() {
        super.onDestroyView();
    }
}