package de.tum.in.i22.sentinel.android.app.fragment;


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

import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpResponse;

import java.io.File;

import de.tum.in.i22.sentinel.android.app.R;
import de.tum.in.i22.sentinel.android.app.backend.APKReceiver;
import de.tum.in.i22.sentinel.android.app.backend.APKSender;
import de.tum.in.i22.sentinel.android.app.package_getter.Hash;

/**
 * Created by Moderbord on 2016-01-06.
 */

public class ToServerFragment extends Fragment {

    File sourceFile, sinkFile, taintFile, apkFile;

    String hash;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        String apk = args.getString(InstrumentFragment.APK);
        String source = args.getString(InstrumentFragment.SOURCES);
        String sink = args.getString(InstrumentFragment.SINKS);
        String taintWrapper = args.getString(InstrumentFragment.TAINT);

        // TODO Lolo if a sink/source/wrapper hasn't been specified in previous view, these keys return an "empty" string. Should they not return null instead?
        apkFile = getFile(apk, InstrumentFragment.APK);
        sourceFile = getFile(source, InstrumentFragment.SOURCES);
        sinkFile = getFile(sink, InstrumentFragment.SINKS);
        taintFile = getFile(taintWrapper, InstrumentFragment.TAINT);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.to_server_fragment, container, false);
        TextView summaryText = (TextView) view.findViewById(R.id.summary);

        // Displays a summary of previously selected files to the user
        Spanned displayText = Html.fromHtml("<b>APK: </b>" + apkFile + "<br><b>Sinks: </b>"
                + sinkFile + "<br><b>Sources: </b>" + sourceFile + "<br><b>Taint Wrapper: </b>" + taintFile);
        summaryText.setText(displayText);

        // TODO Refactor that
        Button b = (Button) view.findViewById(R.id.launchServer);
        final Button getAPK = (Button) view.findViewById(R.id.getAPK);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // sendFiles(File pathToSources, File pathToSinks, File pathToTaintWrapper, File apk, AsyncHttpClient.StringCallback callback) {
                APKSender.getInstance().sendFiles(sourceFile, sinkFile, taintFile, apkFile, new AsyncHttpClient.StringCallback() {
                    @Override
                    public void onCompleted(Exception e, AsyncHttpResponse source, String result) {
                        if (e == null) {
                            Log.d("ToServerFragment", "Success, bitch");
                            Log.d("ToServerFragment", result);
                            hash = Hash.createHashForFile(apkFile);
                            Log.d("ToServerFragment", hash);
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    getAPK.setVisibility(View.VISIBLE);
                                }
                            });
                        } else {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        });
        getAPK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                APKReceiver.getInstance().getFile(hash, new AsyncHttpClient.FileCallback() {
                    @Override
                    public void onCompleted(Exception e, AsyncHttpResponse source, File result) {
                        if (e != null) {
                            Log.d("ToServerFragment", "LAAA BITE A DUDUUUULE");
                        } else {
                            Exception b = e;
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onProgress(AsyncHttpResponse response, long downloaded, long total) {
                        Log.d("ToServerFragment", "downloaded:" + downloaded);
                        Log.d("ToServerFragment", "downloaded/total:" + (downloaded / total));
                    }
                });
            }
        });
        return view;
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
