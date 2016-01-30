package de.tum.in.i22.sentinel.android.app.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import de.tum.in.i22.sentinel.android.app.Constants;
import de.tum.in.i22.sentinel.android.app.R;
import de.tum.in.i22.sentinel.android.app.file_explorer.DirectoryChooser;

/**
 * Created by laurentmeyer on 23/12/15.
 */
public class SettingsFragment extends Fragment {

    private final int FOLDER_REQUEST = 1;
    private TextView saveToPath;
    private Switch saveToSwitch;

    public static String savedAPKFolder;
    public static boolean saveAPK;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_fragment, container, false);
        getActivity().setTitle("Settings");

        saveToSwitch = (Switch) view.findViewById(R.id.saveToSwitch);
        saveToPath = (TextView) view.findViewById(R.id.saveToPath);

        // Toggles if the user automatically wants to save applications retrieved from the server
        saveToSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Colour change for interactive feedback
                int active = Color.parseColor(Constants.COLOR_DARK);
                int inactive = Color.parseColor(Constants.COLOR_GREY);
                if (isChecked) {
                    // Switch is active
                    saveAPK = true;
                    saveToPath.setTextColor(active);
                    saveToPath.setEnabled(true); // Enables the component to be clickable

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
                    // Switch is inactive
                    saveAPK = false;
                    saveToPath.setTextColor(inactive);
                    saveToPath.setEnabled(false); // Disables the component, but remains visible
                }
            }
        });

        // Set switch to active and if folder has been specified it also displays that in
        // the TextView
        // Condition: User has enabled this setting from previous session
        if (saveAPK) {
            saveToSwitch.performClick();
        }
        // Outside 'if statement' so path is still visible
        String folderPath = (savedAPKFolder != null) ? savedAPKFolder : Environment.getExternalStorageDirectory() + "/instrumentedApk/";
        saveToPath.setText(folderPath);

        return view;
    }

    /**
     * If an intent was sent to {@see de.tum.in.i22.sentinel.android.app.file_explorer.DirectoryChooser}
     * the result is handled by this method.
     *
     * @param requestCode Determined by which activity that is trying to send data
     * @param resultCode  Integer value based on operation success, cancellation, or pre-defined activity results
     * @param data        The intent data that was returned from the launched activity
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FOLDER_REQUEST) {
            if (resultCode == getActivity().RESULT_OK) {
                savedAPKFolder = String.valueOf(data.getStringExtra(Constants.DIRECTORY_PATH));
                saveToPath.setText(savedAPKFolder);
            }
        }
    }

    /**
     * Saves the settings into SharedPreferences which is initialized in the MainActivity upon startup
     */
    @Override
    public void onDestroy() {
        super.onDestroyView();
        SharedPreferences sp = getActivity().getSharedPreferences(Constants.SENTINEL, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        String path = (savedAPKFolder != null) ? savedAPKFolder : Environment.getExternalStorageDirectory() + "/instrumentedApk/";

        editor.putString(Constants.SP_SAVE_APK_FOLDER, path);
        editor.putBoolean(Constants.SP_SAVE_APK, saveAPK);
        editor.apply();
    }
}
