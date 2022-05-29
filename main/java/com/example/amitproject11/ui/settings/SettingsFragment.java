package com.example.amitproject11.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.amitproject11.MainActivity;
import com.example.amitproject11.Networking;
import com.example.amitproject11.databinding.FragmentSettingsBinding;
import com.example.amitproject11.ui.login.LoginActivity;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private static String[] extras ;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Button signOut = binding.signOut;
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

        CompoundButton simpleSwitch1 = (Switch) binding.doubleAuthentication;
        menageSwitch(simpleSwitch1, extras[3], Integer.parseInt(extras[2]));
        return root;
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}