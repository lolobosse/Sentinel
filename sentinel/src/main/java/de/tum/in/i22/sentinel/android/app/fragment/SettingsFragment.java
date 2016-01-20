package de.tum.in.i22.sentinel.android.app.fragment;

import android.content.Intent;
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
import de.tum.in.i22.sentinel.android.app.file_explorer.DirectoryChooser;

/**
 * Created by laurentmeyer on 23/12/15.
 */
public class SettingsFragment extends Fragment{

    public static final String FOLDER_PATH = "GetFolderPath";
    public static final String COLOR_DARK = "#202020";
    public static final String COLOR_GREY = "#c5c5c5";
    private final int FOLDER_REQUEST = 1;
    private TextView saveToPath;
    private Switch saveToSwitch, postInstallSwitch;

    public static String savedAPKfolder;
    public static boolean saveAPK = false, postInstallAPK;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_fragment, container, false);

        saveToSwitch = (Switch) view.findViewById(R.id.saveToSwitch);
        postInstallSwitch = (Switch) view.findViewById(R.id.installSwitch);
        saveToPath = (TextView) view.findViewById(R.id.saveToPath);

        // Toggles if the user automatically wants to save applications retrieved from the server
        saveToSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Changes the colour on the saveToFile textview for feedback to user
                int active = Color.parseColor(COLOR_DARK);
                int inactive = Color.parseColor(COLOR_GREY);
                if (isChecked){
                    saveAPK = true;
                    saveToPath.setTextColor(active);
                    saveToPath.setEnabled(true);

                    // Opens a file explorer of which the user can choose directory to save downloaded
                    // application files into
                    saveToPath.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), DirectoryChooser.class);
                            startActivityForResult(intent, FOLDER_REQUEST);
                        }
                    });
                    
                } else {
                    saveAPK = false;
                    saveToPath.setTextColor(inactive);
                    saveToPath.setEnabled(false);
                }
            }
        });

        // Toggles if the user wants to install given application retrieved from the server upon completion
        postInstallSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    postInstallAPK = true;
                } else {
                    postInstallAPK = false;
                }
            }
        });

        return view;
    }

    // If a directory is chosen it is displayed to the user here
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FOLDER_REQUEST){
            if (resultCode == getActivity().RESULT_OK){
                savedAPKfolder = String.valueOf(data.getStringExtra(FOLDER_PATH));
                saveToPath.setText(savedAPKfolder);
            }
        }

    }
}
