package de.tum.in.i22.sentinel.android.app.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpResponse;

import java.io.File;

import de.tum.in.i22.sentinel.android.app.Constants;
import de.tum.in.i22.sentinel.android.app.R;
import de.tum.in.i22.sentinel.android.app.Utils;
import de.tum.in.i22.sentinel.android.app.backend.APKReceiver;
import de.tum.in.i22.sentinel.android.app.backend.APKSender;
import de.tum.in.i22.sentinel.android.app.backend.APKUtils;
import de.tum.in.i22.sentinel.android.app.package_getter.Hash;

/**
 * Created by Moderbord on 2016-01-06.
 */

public class ToServerFragment extends Fragment {

    TextView summary;
    File sourceFile, sinkFile, taintFile, apkFile, logo;
    String appName, packageName;
    String hash;

    TextView notReady;

    String resultPath;

    /**
     * Main idea of this method is to retrieve the path of the different file we want to send to the server
     * @param savedInstanceState: not used
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        String apk = args.getString(Constants.APK);
        String source = args.getString(Constants.SOURCES);
        String sink = args.getString(Constants.SINKS);
        String taintWrapper = args.getString(Constants.TAINT);
        String logoPath = args.getString(Constants.LOGO);
        packageName = args.getString(Constants.PACKAGE_NAME);
        appName = args.getString(Constants.APP_NAME);
        apkFile = getFile(apk, Constants.APK);
        sourceFile = getFile(source, Constants.SOURCES);
        sinkFile = getFile(sink, Constants.SINKS);
        taintFile = getFile(taintWrapper, Constants.TAINT);
        // Ternary assigning null or the File corresponding to the path to the #logo variable
        logo = logoPath == null ? null : new File(logoPath);
    }

    /**
     * We create a summary view for the user in order that he checks a last time what he is about
     * to send to the server.
     * To do that we build a small text with a #StringBuilder and a #Spanned
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return the view to be displayed
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.to_server_fragment, container, false);
        TextView summaryText = (TextView) view.findViewById(R.id.summary);
        notReady = (TextView) view.findViewById(R.id.notReady);

        // Displays a summary of previously selected files to the user
        StringBuilder sb = new StringBuilder();
        String sinkPath = getActivity().getFilesDir() + File.separator + Constants.SINKS;
        String sourcePath = getActivity().getFilesDir() + File.separator + Constants.SOURCES;
        String taintPath = getActivity().getFilesDir() + File.separator + Constants.TAINT;
        sb.append("<b>APK: </b>")
                .append(apkFile)
                .append("<br><b>Sinks: </b>")
                .append(sinkFile.getAbsolutePath().equals(sinkPath) ? "Default" : sinkFile.getAbsolutePath())
                .append("<br><b>Sources: </b>")
                .append(sourceFile.getAbsolutePath().equals(sourcePath) ? "Default" : sourceFile.getAbsolutePath())
                .append("<br><b>Taint Wrapper: </b>")
                .append(taintFile.getAbsolutePath().equals(taintPath) ? "Default" : taintFile.getAbsolutePath());
        Spanned displayText = Html.fromHtml(sb.toString());

        summaryText.setText(displayText);

        Button toServerButton = (Button) view.findViewById(R.id.launchServer);
        final Button getAPK = (Button) view.findViewById(R.id.getAPK);
        toServerButton.setOnClickListener(new View.OnClickListener() {

            /**
             * That's where we send the data to the server in order that it instruments the app.
             * @param view: the button being clicked (not important for us yet)
             */
            @Override
            public void onClick(View view) {
                APKSender.getInstance().sendFiles(sourceFile, sinkFile, taintFile, apkFile, new AsyncHttpClient.StringCallback() {
                    @Override
                    public void onCompleted(Exception e, AsyncHttpResponse source, String result) {
                        if (e == null) {
                            Log.d("ToServerFragment", "Success, bitch");
                            Log.d("ToServerFragment", result);
                            hash = Hash.createHashForFile(apkFile);
                            Log.d("ToServerFragment", hash);
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                /**
                                 * Once the server returned a 202 (Accepted) we can display the button
                                 * to retrieve the APK
                                 */
                                @Override
                                public void run() {
                                    getAPK.setVisibility(View.VISIBLE);
                                }
                            });
                        } else {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    Utils.toastMaker(getActivity(), "No server connection");
                                }
                            });
                        }
                    }
                }, logo, appName, packageName, getActivity());
            }
        });
        getAPK.setOnClickListener(new View.OnClickListener() {
            /**
             * Once the button to retrieve the app is clicked, we need to download the app and try
             * to install it.
             * @param view
             */
            @Override
            public void onClick(View view) {
                APKReceiver.getInstance().getFile(hash, new AsyncHttpClient.FileCallback() {
                    @Override
                    public void onCompleted(Exception e, AsyncHttpResponse source, File result) {
                        // If the server returns a 200 status => the app has been successfully
                        // instrumented and is successfully returned
                        if (e == null && source.code() == 200) {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    notReady.setText("Your app is downloading, wait a moment please :)");
                                    notReady.setVisibility(View.VISIBLE);
                                }
                            });
                            // If an app with the same package name is already installed on the device, we try to uninstall it first
                            if (APKUtils.isInstalled(getActivity(), packageName)) {
                                resultPath = result.getAbsolutePath();
                                APKReceiver.getInstance().uninstallApk(ToServerFragment.this, packageName);
                            } else {
                                APKReceiver.getInstance().installApk(ToServerFragment.this, result.getAbsolutePath());
                            }
                        } else {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    notReady.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    }
                }, getActivity());
            }
        });
        return view;
    }

    /**
     * Called when the user come back from the different install/uninstall
     * @param requestCode: the code the #APKUtils sent with intent to the system
     * @param resultCode: if it was successful
     * @param data: not used
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == APKReceiver.REQUEST_UNINSTALLATION) {
            if (resultCode == Activity.RESULT_OK) {
                APKReceiver.getInstance().installApk(this, resultPath);
            } else {
                Toast.makeText(getActivity(), "Something went wrong during the uninstall", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == APKReceiver.REQUEST_INSTALLATION) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(getActivity(), "Yepee, app installed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Something went wrong during the install", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private File getFile(String path, String key) {
        if (!TextUtils.isEmpty(path)) {
            return new File(path);
        } else {
            String defaultFilePath = getActivity().getFilesDir() + "/" + key;
            return new File(defaultFilePath);
        }
    }
}
