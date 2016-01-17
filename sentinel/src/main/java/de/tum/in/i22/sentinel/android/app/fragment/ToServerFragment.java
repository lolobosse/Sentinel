package de.tum.in.i22.sentinel.android.app.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import de.tum.in.i22.sentinel.android.app.backend.APKSender;

/**
 * Created by Moderbord on 2016-01-06.
 */

public class ToServerFragment extends Fragment {

    TextView summary;
    File sourceFile, sinkFile, taintFile, apkFile;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        String apk = args.getString(InstrumentFragment.APK);
        String source = args.getString(InstrumentFragment.SOURCES);
        String sink = args.getString(InstrumentFragment.SINKS);
        String taintWrapper = args.getString(InstrumentFragment.TAINT);
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
        // TODO: Write a text there with paths.

        Button b = (Button) view.findViewById(R.id.launchServer);
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
                        } else {
                            throw new RuntimeException(e);
                        }
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
