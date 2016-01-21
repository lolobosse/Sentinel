package de.tum.in.i22.sentinel.android.app.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.TextView;

import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpResponse;

import de.tum.in.i22.sentinel.android.app.R;
import de.tum.in.i22.sentinel.android.app.backend.APKSender;
import de.tum.in.i22.sentinel.android.app.file_explorer.FileChooser;
import de.tum.in.i22.sentinel.android.app.package_getter.AppPickerDialog;
import de.tum.in.i22.sentinel.android.app.package_getter.PackageGetter;

/**
 * Created by laurentmeyer on 23/12/15.
 */
public class InstrumentFragment extends Fragment implements AppPickerDialog.onFileChooseTriggered{


    // TODO Make this Strings public in a particular static class called constants (and also the colors)
    public static final String INPUT_XML = ".xml";
    private final String LOG = "InstrumentFragment";
    static final int PICK_APPLICATION_REQUEST = 1, PICK_SINKS_REQUEST = 2, PICK_SOURCE_REQUEST = 3, PICK_TAINT_REQUEST = 4;
    private View view;

    public static String applicationPath, sinksPath, sourcePath, taintPath;

    public static final String INSTRUMENTED_APPLICATIONS = "instrumentedApplications";
    public static final String SENTINEL = "sentinel";
    public static final String ABSOLUTE_PATH = "GetAbsolutePath";
    public static final String EXTENSION = "extension";
    public static final String INPUT_APPLICATION = ".apk";
    public static final String INPUT_TXT = ".txt";

    public static final String SP_PATH_APP = "pathApp";
    public static final String SP_PATH_SINKS = "pathSinks";
    public static final String SP_PATH_SOURCES = "pathSources";
    public static final String SP_PATH_TAINT = "pathTaint";

    public static final String APK      = "apkPath";
    public static final String SOURCES  = "sourcePath";
    public static final String SINKS    = "sinkPath";
    public static final String TAINT    = "taintPath";


    TextView taintInputText, sourceInputText, sinksInputText, appInputText;
    Dialog packageDialog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.instrument_fragment, container, false);

        // Finds the textViews
        appInputText = (EditText)view.findViewById(R.id.applicationInput);
        sinksInputText = (EditText) view.findViewById(R.id.sinksInput);
        sourceInputText = (EditText) view.findViewById(R.id.sourcesInput);
        taintInputText= (EditText) view.findViewById(R.id.taintInput);

        // Loads paths to chosen files from SharedPreferences
        SharedPreferences sp = getActivity().getSharedPreferences(SENTINEL, 0);
        String app = sp.getString(SP_PATH_APP, null);
        String sinks = sp.getString(SP_PATH_SINKS, null);
        String sources = sp.getString(SP_PATH_SOURCES, null);
        String taint = sp.getString(SP_PATH_TAINT, null);

        // Displays them in the textViews
        setApplicationPath(app);
        setSinksPath(sinks);
        setSourcePath(sources);
        setTaintPath(taint);

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


        // Opens a dialog with installed packages the user can choose from, alternatively the user
        // can pick one from the file system
        pickApplicationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                packageDialog = new AppPickerDialog(getActivity(), new AppPickerDialog.OnPackageChosen() {
                    @Override
                    public void onPackageSet(PackageGetter.Package selectedPackage) {
                        Log.d("InstrumentFragment", "selectedPackage:" + selectedPackage);
                        setApplicationPath(selectedPackage.getPath());
                        APKSender.getInstance().sendFiles(null, null, null, selectedPackage, new AsyncHttpClient.StringCallback() {


                            @Override
                            public void onCompleted(Exception e, AsyncHttpResponse source, String result) {
                                Log.d("LA BITE", "Completed");
                            }
                        });
                        dismissDialog();
                    }
                }, InstrumentFragment.this);
                showDialog();
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

        // Creates a new fragment and delivers the path to selected files
        nextActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ToServerFragment toServerFragment = new ToServerFragment();
                Bundle b = new Bundle();
                b.putString(APK, applicationPath);
                b.putString(SOURCES, sourcePath);
                b.putString(SINKS, sinksPath);
                b.putString(TAINT, taintPath);
                toServerFragment.setArguments(b);
                ft.replace(R.id.mainViewContainer, toServerFragment);
                ft.commit();
            }
        });

        clearInputs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clears all of the inputs
                setApplicationPath(null);
                setSinksPath(null);
                setSourcePath(null);
                setTaintPath(null);
            }
        });

        return view;
    }

    private void dismissDialog() {
        packageDialog.dismiss();
    }

    private void showDialog() {
        packageDialog.show();
    }

    /**
     * Opens up a file explorer via an intent and lets the user choose files from the local file system.
     * Here the user is limited to .txt files as the source and sink definitions are contained
     * within these type of files.
     * @param requestCode Is determined by which button was pressed in the view fragment and will be
     *                    sent and handled by the onActivityResult.
     */
    public void getFile(int requestCode) {
        Intent intent = new Intent(getActivity(), FileChooser.class);
        intent.putExtra(EXTENSION, INPUT_TXT); // The putExtra is used in FileChooser to stop invalid file types from being selected
        startActivityForResult(intent, requestCode);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Matches the request from which button was pressed, and updates the correct textView with
        // the absolute path retrieved from the selected file
        if (requestCode == PICK_APPLICATION_REQUEST){
            if (resultCode == getActivity().RESULT_OK){
                setApplicationPath(data.getStringExtra(ABSOLUTE_PATH));
                dismissDialog();
            }
        } else if (requestCode == PICK_SINKS_REQUEST){
            if (resultCode == getActivity().RESULT_OK){
                setSinksPath(data.getStringExtra(ABSOLUTE_PATH));
            }
        } else if (requestCode == PICK_SOURCE_REQUEST){
            if (resultCode == getActivity().RESULT_OK){
                setSourcePath(data.getStringExtra(ABSOLUTE_PATH));
            }
        } else if (requestCode == PICK_TAINT_REQUEST){
            if (resultCode == getActivity().RESULT_OK){
                setTaintPath(data.getStringExtra(ABSOLUTE_PATH));
            }
        } else {
            Log.d(LOG, "No pick request received");
        }

    }

    // Displays the selected file's absolute path to the user
    public void setApplicationPath(String applicationPath) {
        if (applicationPath == null) {
            appInputText.setText("");
        } else {
            appInputText.setText(String.valueOf(applicationPath));
        }
        this.applicationPath = applicationPath;
    }

    public void setSinksPath(String sinksPath) {
        if (sinksPath == null) {
            sinksInputText.setText("");
        } else {
            sinksInputText.setText(String.valueOf(sinksPath));
        }
        this.sinksPath = sinksPath;
    }

    public void setSourcePath(String sourcePath) {
        if (sourcePath == null) {
            sourceInputText.setText("");
        } else {
            sourceInputText.setText(String.valueOf(sourcePath));
        }
        this.sourcePath = sourcePath;
    }

    public void setTaintPath(String taintPath) {
        if (taintPath == null) {
            taintInputText.setText("");
        } else {
            taintInputText.setText(String.valueOf(taintPath));
        }
        this.taintPath = taintPath;
    }

    // If the view is destroyed it saves the paths to selected files in shared preferences in case
    // the user navigates to different fragments and then wants to continue selecting files
    @Override
    public void onDestroyView() {
        super.onDestroyView();

        Log.d("InstrumentFragment", "View Destroyed");

        String app = applicationPath, sinks = sinksPath, sources = sourcePath, taint = taintPath;

        SharedPreferences sp = getActivity().getSharedPreferences(SENTINEL, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SP_PATH_APP, app);
        editor.putString(SP_PATH_SINKS, sinks);
        editor.putString(SP_PATH_SOURCES, sources);
        editor.putString(SP_PATH_TAINT, taint);
        editor.apply();
    }

    // Implemented interface method from AppPickerDialog so the path to chosen APK file can be passed to this fragment
    @Override
    public void onClick() {
        Intent intent = new Intent(getActivity(), FileChooser.class);
        intent.putExtra(EXTENSION, INPUT_APPLICATION);
        startActivityForResult(intent, PICK_APPLICATION_REQUEST);
    }
}
