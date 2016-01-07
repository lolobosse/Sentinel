package de.tum.in.i22.sentinel.android.app.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import de.tum.in.i22.sentinel.android.app.R;
import de.tum.in.i22.sentinel.android.app.file_explorer.FileChooser;
import de.tum.in.i22.sentinel.android.app.package_getter.AppPickerDialog;
import de.tum.in.i22.sentinel.android.app.package_getter.PackageGetter;

/**
 * Created by laurentmeyer on 23/12/15.
 */
public class InstrumentFragment extends Fragment{

    public final String SENTINEL = "sentinel", INSTRUMENTED_APPLICATIONS = "instrumentedApplications";
    private final String LOG = "InstrumentFragment", INPUT_APPLICATION = ".apk", INPUT_TXT = ".txt";
    public String applicationPath, sinksPath, sourcePath, taintPath;
    static final int PICK_APPLICATION_REQUEST = 1, PICK_SINKS_REQUEST = 2, PICK_SOURCE_REQUEST = 3, PICK_TAINT_REQUEST = 4;
    private View view;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.instrument_fragment, container, false);

        // Test button for instrumented apps counter
        /* LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.instrumentationLinearLayout);
        Button button = new Button(getActivity());
        linearLayout.addView(button);
        button.setText("Counter++");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getActivity().getSharedPreferences(SENTINEL, 0);
                SharedPreferences.Editor editor = sp.edit();
                int retrievedAmount = sp.getInt(INSTRUMENTED_APPLICATIONS, 0);
                int newAmount = retrievedAmount + 1;
                editor.putInt(INSTRUMENTED_APPLICATIONS, newAmount);
                editor.commit();
            }
        }); */
        // End test

        // Finds the buttons
        Button pickApplicationButton = (Button)view.findViewById(R.id.applicationButton);
        Button pickSinksButton = (Button)view.findViewById(R.id.sinksButton);
        Button pickSourceButton = (Button)view.findViewById(R.id.sourcesButton);
        Button pickTaintButton = (Button)view.findViewById(R.id.taintButton);
        Button nextActivity = (Button)view.findViewById(R.id.nextActivityButton);
        Button clearInputs = (Button)view.findViewById(R.id.clearButton);

        /*
        pickApplicationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFile();
            }
        });
        */

        // Applied the listeners
        pickApplicationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog d = new AppPickerDialog(getActivity(), new AppPickerDialog.OnPackageChosen() {
                    @Override
                    public void onPackageSet(PackageGetter.Package selectedPackage) {
                        Log.d("InstrumentFragment", "selectedPackage:" + selectedPackage);
                    }
                });
                d.show();
            }
        });

        pickSinksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFile(PICK_SINKS_REQUEST);
            }
        });

        pickSourceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFile(PICK_SOURCE_REQUEST);
            }
        });

        pickTaintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFile(PICK_TAINT_REQUEST);
            }
        });

        nextActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.mainViewContainer, new ToServerFragment());
                ft.commit();
            }
        });

        clearInputs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clears all of the inputs
                setApplicationPath("");
                setSinksPath("");
                setSourcePath("");
                setTaintPath("");
            }
        });

        return view;
    }

    private void getFile(int fromRequest) {
        Intent intent = new Intent(this.getActivity(), FileChooser.class);
        intent.putExtra("extension", INPUT_TXT);
        startActivityForResult(intent, fromRequest);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PICK_APPLICATION_REQUEST){
            if (resultCode == getActivity().RESULT_OK){
                setApplicationPath(data.getStringExtra("GetAbsolutePath"));
            }
        } else if (requestCode == PICK_SINKS_REQUEST){
            if (resultCode == getActivity().RESULT_OK){
                setSinksPath(data.getStringExtra("GetAbsolutePath"));
            }
        } else if (requestCode == PICK_SOURCE_REQUEST){
            if (resultCode == getActivity().RESULT_OK){
                setSourcePath(data.getStringExtra("GetAbsolutePath"));
            }
        } else if (requestCode == PICK_TAINT_REQUEST){
            if (resultCode == getActivity().RESULT_OK){
                setTaintPath(data.getStringExtra("GetAbsolutePath"));
            }
        } else {
            Log.d(LOG, "No pick request received");
        }

    }

    public String getApplicationPath() {
        return applicationPath;
    }

    public void setApplicationPath(String applicationPath) {
        EditText appInputText = (EditText)view.findViewById(R.id.applicationInput);
        appInputText.setText(String.valueOf(applicationPath));
        this.applicationPath = applicationPath;
    }

    public String getSinksPath() {
        return sinksPath;
    }

    public void setSinksPath(String sinksPath) {
        EditText sinksInputText = (EditText) view.findViewById(R.id.sinksInput);
        sinksInputText.setText(String.valueOf(sinksPath));
        this.sinksPath = sinksPath;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        EditText sourceInputText = (EditText) view.findViewById(R.id.sourcesInput);
        sourceInputText.setText(String.valueOf(sourcePath));
        this.sourcePath = sourcePath;
    }

    public String getTaintPath() {
        return taintPath;
    }

    public void setTaintPath(String taintPath) {
        EditText taintInputText = (EditText) view.findViewById(R.id.taintInput);
        taintInputText.setText(String.valueOf(taintPath));
        this.taintPath = taintPath;
    }

}
