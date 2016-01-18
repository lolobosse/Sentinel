package de.tum.in.i22.sentinel.android.app.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
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

    public final String SENTINEL = "sentinel", INSTRUMENTED_APPLICATIONS = "instrumentedApplications";
    private final String LOG = "InstrumentFragment", INPUT_APPLICATION = ".apk", INPUT_TXT = ".txt";
    public String applicationPath, sinksPath, sourcePath, taintPath;
    static final int PICK_APPLICATION_REQUEST = 1, PICK_SINKS_REQUEST = 2, PICK_SOURCE_REQUEST = 3, PICK_TAINT_REQUEST = 4;
    private View view;

    public static final String APK      = "apkPath";
    public static final String SOURCES  = "sourcePath";
    public static final String SINKS    = "sinkPath";
    public static final String TAINT    = "taintPath";


    TextView taintInputText, sourceInputText, sinksInputText, appInputText;
    Dialog d;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.instrument_fragment, container, false);

        // Fins the textViews
        appInputText = (EditText)view.findViewById(R.id.applicationInput);
        sinksInputText = (EditText) view.findViewById(R.id.sinksInput);
        sourceInputText = (EditText) view.findViewById(R.id.sourcesInput);
        taintInputText= (EditText) view.findViewById(R.id.taintInput);

        // Loads paths to chosen files from SharedPreferences
        SharedPreferences sp = getActivity().getSharedPreferences(SENTINEL, 0);
        String app = sp.getString("pathApp", null);
        String sinks = sp.getString("pathSinks", null);
        String sources = sp.getString("pathSources", null);
        String taint = sp.getString("pathTaint", null);

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


        // Applied the listeners
        pickApplicationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d = new AppPickerDialog(getActivity(), new AppPickerDialog.OnPackageChosen() {
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
        d.dismiss();
    }

    private void showDialog() {
        d.show();
    }

    public void getFile(int fromRequest) {
        Intent intent = new Intent(getActivity(), FileChooser.class);
        intent.putExtra("extension", INPUT_TXT);
        // TODO Refactor fromRequest
        startActivityForResult(intent, fromRequest);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PICK_APPLICATION_REQUEST){
            if (resultCode == getActivity().RESULT_OK){
                setApplicationPath(data.getStringExtra("GetAbsolutePath"));
                dismissDialog();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        Log.d("InstrumentFragment", "View Destroyed");

        String app = applicationPath, sinks = sinksPath, sources = sourcePath, taint = taintPath;

        SharedPreferences sp = getActivity().getSharedPreferences(SENTINEL, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("pathApp", app);
        editor.putString("pathSinks", sinks);
        editor.putString("pathSources", sources);
        editor.putString("pathTaint", taint);
        editor.apply();
    }

    @Override
    public void onClick() {
        Intent intent = new Intent(getActivity(), FileChooser.class);
        intent.putExtra("extension", INPUT_APPLICATION);
        startActivityForResult(intent, PICK_APPLICATION_REQUEST);
    }
}
