package de.tum.in.i22.sentinel.android.app.backend;

import android.content.Context;
import android.os.Environment;

import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpGet;
import com.koushikdutta.async.http.AsyncHttpResponse;

import java.io.File;

/**
 * Created by laurentmeyer on 18/01/16.
 */
public class APKReceiver {

    private static APKReceiver instance = null;

    public static APKReceiver getInstance() {
        if (instance == null) {
            instance = new APKReceiver();
        }
        return instance;
    }

    public void getFile(String hash, AsyncHttpClient.FileCallback callback){
        String serverAddress = "192.168.0.111:8080";
        File repo = new File(Environment.getExternalStorageDirectory()+"/instrumentedApk/");
        repo.mkdirs();
        File output = new File(repo, hash+".apk");
        String filename = output.getAbsolutePath();
        AsyncHttpClient.getDefaultInstance().executeFile(new AsyncHttpGet("http://"+serverAddress+"/instrument/"+ hash), filename, callback);
    }
}
