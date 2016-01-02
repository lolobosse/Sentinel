package de.tum.in.i22.sentinel.android.app.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import de.tum.in.i22.sentinel.android.app.R;

/**
 * Created by laurentmeyer on 23/12/15.
 */
public class InstrumentFragment extends Fragment{

    public final String SENTINEL = "sentinel", INSTRUMENTED_APPLICATIONS = "instrumentedApplications";
    private final String LOG = "InstrumentFragment", INPUT_APPLICATION = "application/vnd.android.*", INPUT_TXT = "text/plain";
    public String applicationPath, sinksPath, sourcePath, taintPath;
    static final int PICK_APP_REQUEST = 1, PICK_SINKS_REQUEST = 2, PICK_SOURCE_REQUEST = 3, PICK_TAINT_REQUEST = 4;
    private int fromRequest = 0;
    private View tempView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.instrument_fragment, container, false);
        tempView = view;

        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.instrumentationLinearLayout);

        // Test button for instrumented apps counter
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
        });
        // End test

        // Finds the buttons
        Button pickApplicationButton = (Button)view.findViewById(R.id.applicationButton);
        Button pickSinksButton = (Button)view.findViewById(R.id.sinksButton);
        Button pickSourceButton = (Button)view.findViewById(R.id.sourcesButton);
        Button pickTaintButton = (Button)view.findViewById(R.id.taintButton);

        // Applied the listeners
        pickApplicationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromRequest = 1;
                inputChooser(INPUT_APPLICATION);
            }
        });

        pickSinksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromRequest = 2;
                inputChooser(INPUT_TXT);
            }
        });

        pickSourceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromRequest = 3;
                inputChooser(INPUT_TXT);
            }
        });

        pickTaintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromRequest = 4;
                inputChooser(INPUT_TXT);
            }
        });

        return view;
    }

    private void inputChooser(String fileType){
        // ACTION_PICK might be a better choice instead of ACTION_GET_CONTENT when only browsing for
        // files on the local system
        Intent intent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
        // fileType will either be .txt (text/plain) or .apk (application/vnd.android.package-archive)
        intent.setType(fileType);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra("CONTENT_TYPE", fileType);

        // Intent for Samsung file manager
        Intent sIntent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
        sIntent.putExtra("CONTENT_TYPE", fileType);
        sIntent.addCategory(Intent.CATEGORY_DEFAULT);

        // Intent chooseIntent;
        Intent chooseIntent = Intent.createChooser(intent, "Open file");
        if (getActivity().getPackageManager().resolveActivity(sIntent, 0) != null){
            // File manager for Samsung devices
            chooseIntent = Intent.createChooser(sIntent, "Open file");
            chooseIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {
                    intent
            });
        } else {
            chooseIntent = Intent.createChooser(intent, "Open file");
        }

        try {
            // fromRequest is assigned 1-4 based on which button element was pressed and will match
            // with the correct onActivityResult 'if' condition
            startActivityForResult(chooseIntent, fromRequest);
        } catch (android.content.ActivityNotFoundException ex){
            Toast.makeText(getActivity().getApplicationContext(), R.string.activityNoFileManager, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == PICK_APP_REQUEST){
            if (data != null) {
                String path = data.getDataString();
                Log.d("", "Application request received" + path);
                setApplicationPath(path);
            }
            super.onActivityResult(requestCode, resultCode, data);
            Log.d(LOG, "Application request end");

        } else if (requestCode == PICK_SINKS_REQUEST){
            if (data != null) {
                String path = data.getDataString();
                //Uri file = data.getData();
                Log.d(LOG, "Sinks request received " + path);
                setSinksPath(path);
            }
            super.onActivityResult(requestCode,resultCode, data);
            Log.d(LOG, "Sinks request end");
        } else if (requestCode == PICK_SOURCE_REQUEST){
            if (data != null) {
                String path = data.getDataString();
                Log.d(LOG, "Source request received");
                setSourcePath(path);
            }
            super.onActivityResult(requestCode, resultCode, data);
            Log.d(LOG, "Source request end");
        } else if (requestCode == PICK_TAINT_REQUEST) {
            if (data != null){
                String path = data.getDataString();
                Log.d(LOG, "Taint request received");
                setTaintPath(path);
            }
            super.onActivityResult(requestCode, resultCode, data);
            Log.d(LOG, "Taint request end");
        } else {
            Log.d(LOG, "No pick request received");
        }
    }


    public String getApplicationPath() {
        return applicationPath;
    }

    public void setApplicationPath(String applicationPath) {
        EditText appInputText = (EditText)tempView.findViewById(R.id.applicationInput);
        appInputText.setText(applicationPath);
        this.applicationPath = applicationPath;
    }

    public String getSinksPath() {
        return sinksPath;
    }

    public void setSinksPath(String sinksPath) {
        EditText sinksInputText = (EditText) tempView.findViewById(R.id.sinksInput);
        sinksInputText.setText(sinksPath);
        this.sinksPath = sinksPath;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        EditText sourceInputText = (EditText) tempView.findViewById(R.id.sourcesInput);
        sourceInputText.setText(sourcePath);
        this.sourcePath = sourcePath;
    }

    public String getTaintPath() {
        return taintPath;
    }

    public void setTaintPath(String taintPath) {
        EditText taintInputText = (EditText) tempView.findViewById(R.id.taintInput);
        taintInputText.setText(taintPath);
        this.taintPath = taintPath;
    }

}
