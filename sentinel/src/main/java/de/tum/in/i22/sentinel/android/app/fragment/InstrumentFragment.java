package de.tum.in.i22.sentinel.android.app.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;

import de.tum.in.i22.sentinel.android.app.Constants;
import de.tum.in.i22.sentinel.android.app.R;
import de.tum.in.i22.sentinel.android.app.file_explorer.DirectoryChooser;
import de.tum.in.i22.sentinel.android.app.file_explorer.FileChooser;
import de.tum.in.i22.sentinel.android.app.package_getter.AppPickerDialog;
import de.tum.in.i22.sentinel.android.app.package_getter.PackageGetter;

/**
 * Created by laurentmeyer on 23/12/15.
 */
public class InstrumentFragment extends Fragment implements AppPickerDialog.onFileChooseTriggered {

    static final int PICK_APPLICATION_REQUEST = 1, PICK_SINKS_REQUEST = 2, PICK_SOURCE_REQUEST = 3, PICK_TAINT_REQUEST = 4;
    private View view;

    private PackageGetter.Package selectedPackage;

    public static String applicationPath, sinksPath, sourcePath, taintPath;

    TextView taintInputText, sourceInputText, sinksInputText, appInputText;
    Dialog packageDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.instrument_fragment, container, false);

        // Finds the textViews
        appInputText = (EditText) view.findViewById(R.id.applicationInput);
        sinksInputText = (EditText) view.findViewById(R.id.sinksInput);
        sourceInputText = (EditText) view.findViewById(R.id.sourcesInput);
        taintInputText = (EditText) view.findViewById(R.id.taintInput);

        // Loads paths to chosen files from SharedPreferences
        SharedPreferences sp = getActivity().getSharedPreferences(Constants.SENTINEL, Context.MODE_PRIVATE);
        String app = sp.getString(Constants.SP_PATH_APP, null);
        String sinks = sp.getString(Constants.SP_PATH_SINKS, null);
        String sources = sp.getString(Constants.SP_PATH_SOURCES, null);
        String taint = sp.getString(Constants.SP_PATH_TAINT, null);

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
        Button pickApplicationButton = (Button) view.findViewById(R.id.applicationButton);
        Button pickSinksButton = (Button) view.findViewById(R.id.sinksButton);
        Button pickSourceButton = (Button) view.findViewById(R.id.sourcesButton);
        Button pickTaintButton = (Button) view.findViewById(R.id.taintButton);
        Button nextActivity = (Button) view.findViewById(R.id.nextActivityButton);
        Button clearInputs = (Button) view.findViewById(R.id.clearButton);


        // Opens a dialog with installed packages the user can choose from, alternatively the user
        // can pick one from the file system
        pickApplicationButton.setOnClickListener(new PickApplicationListener());

        appInputText.setOnClickListener(new PickApplicationListener());

        pickSinksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFile(PICK_SINKS_REQUEST);
            }
        });

        sinksInputText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFile(PICK_SINKS_REQUEST);
            }
        });

        pickSourceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {getFile(PICK_SOURCE_REQUEST);
            }
        });

        sourceInputText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {getFile(PICK_SOURCE_REQUEST);
            }
        });

        pickTaintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFile(PICK_TAINT_REQUEST);
            }
        });

        taintInputText.setOnClickListener(new View.OnClickListener() {
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
                b.putString(Constants.APK, applicationPath);
                b.putString(Constants.SOURCES, sourcePath);
                b.putString(Constants.SINKS, sinksPath);
                b.putString(Constants.TAINT, taintPath);
                if (selectedPackage != null) {
                    b.putString(Constants.LOGO, createFileFromDrawable(selectedPackage.getPackagePicture()).getAbsolutePath());
                    b.putString(Constants.APP_NAME, selectedPackage.getName());
                    b.putString(Constants.PACKAGE_NAME, selectedPackage.getPackageName());
                }
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

    /**
     * Dismisses the dialog from {@see de.tum.in.i22.sentinel.android.app.package_getter.AppPickerDialog} shown in this fragment
     */
    private void dismissDialog() {
        if (packageDialog != null) {
            packageDialog.dismiss();
        }
    }

    /**
     * Displays the dialog from {@see de.tum.in.i22.sentinel.android.app.package_getter.AppPickerDialog} defined in this fragment
     */
    private void showDialog() {
        packageDialog.show();
    }

    /**
     * Opens up a file explorer via an intent and lets the user choose files from the local file system.
     * Here the user is limited to .txt files as the source, sinks, and taint wrapper definitions are contained
     * within these type of files.
     * @param requestCode Is determined by which button was pressed in the view fragment and will be
     *                    sent and handled by the onActivityResult.
     */
    public void getFile(int requestCode) {
        Intent intent = new Intent(getActivity(), FileChooser.class);
        intent.putExtra(Constants.EXTENSION, Constants.INPUT_TXT); // The putExtra is used in FileChooser to stop invalid file types from being selected
        startActivityForResult(intent, requestCode);
    }

    /**
     * If an intent was sent to {@see de.tum.in.i22.sentinel.android.app.file_explorer.FileChooser}
     * the result is handled by this method. Also updates the interface to display the path of the
     * File that is passed by the intent.
     * @param requestCode Determined by which component in the View that is trying to send data
     * @param resultCode Integer value based on operation success, cancellation, or pre-defined activity results
     * @param data The intent data that was returned from the launched activity
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Matches the requestCode from which button was pressed, and updates the correct textView with
        // the absolute path retrieved from data
        if (requestCode == PICK_APPLICATION_REQUEST){
            if (resultCode == getActivity().RESULT_OK){
                setApplicationPath(data.getStringExtra(Constants.ABSOLUTE_PATH));
                dismissDialog();
            }
        } else if (requestCode == PICK_SINKS_REQUEST){
            if (resultCode == getActivity().RESULT_OK){
                setSinksPath(data.getStringExtra(Constants.ABSOLUTE_PATH));
            }
        } else if (requestCode == PICK_SOURCE_REQUEST){
            if (resultCode == getActivity().RESULT_OK){
                setSourcePath(data.getStringExtra(Constants.ABSOLUTE_PATH));
            }
        } else if (requestCode == PICK_TAINT_REQUEST){
            if (resultCode == getActivity().RESULT_OK){
                setTaintPath(data.getStringExtra(Constants.ABSOLUTE_PATH));
            }
        }
    }

    /**
     * Displays a given String in a TextView
     * @param applicationPath The String value that is to be set. If null: ""
     */
    public void setApplicationPath(String applicationPath) {
        if (applicationPath == null) {
            appInputText.setText("");
        } else {
            appInputText.setText(String.valueOf(applicationPath));
        }
        this.applicationPath = applicationPath;
    }

    /**
     * Displays a given String in a TextView
     * @param sinksPath The String value that is to be set. If null: ""
     */
    public void setSinksPath(String sinksPath) {
        if (sinksPath == null) {
            sinksInputText.setText("");
        } else {
            sinksInputText.setText(String.valueOf(sinksPath));
        }
        this.sinksPath = sinksPath;
    }

    /**
     * Displays a given String in a TextView
     * @param sourcePath The String value that is to be set. If null: ""
     */
    public void setSourcePath(String sourcePath) {
        if (sourcePath == null) {
            sourceInputText.setText("");
        } else {
            sourceInputText.setText(String.valueOf(sourcePath));
        }
        this.sourcePath = sourcePath;
    }

    /**
     * Displays a given String in a TextView
     * @param taintPath The String value that is to be set. If null: ""
     */
    public void setTaintPath(String taintPath) {
        if (taintPath == null) {
            taintInputText.setText("");
        } else {
            taintInputText.setText(String.valueOf(taintPath));
        }
        this.taintPath = taintPath;
    }

    /**
     * If the view is destroyed it saves paths of selected files in shared preferences
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();

        String app = applicationPath, sinks = sinksPath, sources = sourcePath, taint = taintPath;

        SharedPreferences sp = getActivity().getSharedPreferences(Constants.SENTINEL, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Constants.SP_PATH_APP, app);
        editor.putString(Constants.SP_PATH_SINKS, sinks);
        editor.putString(Constants.SP_PATH_SOURCES, sources);
        editor.putString(Constants.SP_PATH_TAINT, taint);
        editor.apply();
    }

    /**
     * Implemented interface method from AppPickerDialog so the path to chosen APK file can be passed to this fragment
     */
    @Override
    public void onClick() {
        Intent intent = new Intent(getActivity(), FileChooser.class);
        intent.putExtra(Constants.EXTENSION, Constants.INPUT_APK);
        startActivityForResult(intent, PICK_APPLICATION_REQUEST);
    }

    /**
     * Creates a Bitmap object from a Drawable object
     * @param drawable The drawable resource that is to be converted
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * Creates a File object from a Drawable object
     * @param d The drawable resource that is to be converted
     * @return
     */
    private File createFileFromDrawable(Drawable d) {
        Bitmap b = drawableToBitmap(d);
        FileOutputStream out = null;
        try {
            File bitmapFile = new File(getActivity().getFilesDir(), Constants.BITMAP);
            out = new FileOutputStream(bitmapFile);
            b.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            return bitmapFile;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class PickApplicationListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            packageDialog = new AppPickerDialog(getActivity(), new AppPickerDialog.OnPackageChosen() {
                @Override
                public void onPackageSet(PackageGetter.Package selectedPackage) {
                    setApplicationPath(selectedPackage.getPath());
                    InstrumentFragment.this.selectedPackage = selectedPackage;
                    dismissDialog();
                }
            }, InstrumentFragment.this);
            showDialog();
        }
    }

}
