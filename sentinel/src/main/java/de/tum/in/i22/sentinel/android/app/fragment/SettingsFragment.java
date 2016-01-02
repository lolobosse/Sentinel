package de.tum.in.i22.sentinel.android.app.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import de.tum.in.i22.sentinel.android.app.R;

/**
 * Created by laurentmeyer on 23/12/15.
 */
public class SettingsFragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_fragment, container, false);

        final TextView saveToPath = (TextView) view.findViewById(R.id.saveToPath);
        final Switch saveToSwitch = (Switch) view.findViewById(R.id.saveToSwitch);
        saveToSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int dark = Color.parseColor("#202020");
                int light = Color.parseColor("#c5c5c5");
                if (isChecked){
                    saveToPath.setTextColor(dark);
                } else {
                    saveToPath.setTextColor(light);
                }
            }
        });

        return view;
    }
}
