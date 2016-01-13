package de.tum.in.i22.sentinel.android.app.backend;
/**
 * Created by laurentmeyer on 15/12/15.
 */

import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpPost;
import com.koushikdutta.async.http.body.MultipartFormDataBody;

import java.io.File;
import java.util.UUID;

import de.tum.in.i22.sentinel.android.app.package_getter.PackageGetter;

public class APKSender {

    private static APKSender instance = null;

    public static APKSender getInstance() {
        if (instance == null) {
            instance = new APKSender();
        }
        return instance;
    }

    /**
     * Used to send the apk alone (mainly for test purposes), I doubt it could be needed in the real world
     *
     * @param p
     * @param callback
     */
    public void sendAPK(PackageGetter.Package p, AsyncHttpClient.StringCallback callback) {
        String serverAddress = "0.0.0.0";
        AsyncHttpPost post = new AsyncHttpPost("http://" + serverAddress + ":8080/instrument");
        MultipartFormDataBody body = new MultipartFormDataBody();
        body.addFilePart("Apk", new File(p.getPath()));
        body.addStringPart("sid", UUID.randomUUID().toString());
        post.setBody(body);
        AsyncHttpClient.getDefaultInstance().executeString(post, callback);
    }

    public void sendFiles(String pathToSources, String pathToSinks, String pathToTaintWrapper, PackageGetter.Package apk, AsyncHttpClient.StringCallback callback) {
        String serverAddress = "0.0.0.0";
        AsyncHttpPost post = new AsyncHttpPost("http://" + serverAddress + ":8080/instrument");
        MultipartFormDataBody body = new MultipartFormDataBody();
        if (checkPath(apk.getPath()))
            body.addFilePart("Apk", new File(apk.getPath()));
        // TODO: Make it real one way or another
        body.addStringPart("sid", UUID.randomUUID().toString());
        if (checkPath(pathToSources))
            body.addFilePart("Source", new File(pathToSources));
        if (checkPath(pathToSinks))
            body.addFilePart("Sinks", new File(pathToSinks));
        if (checkPath(pathToTaintWrapper))
            body.addFilePart("TaintWrapper", new File(pathToTaintWrapper));
        AsyncHttpClient.getDefaultInstance().executeString(post, callback);
    }

    private boolean checkPath(String path) {
        if (path != null) {
            return new File(path).exists();
        }
        return false;
    }
}