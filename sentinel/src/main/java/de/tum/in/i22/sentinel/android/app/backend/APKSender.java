package de.tum.in.i22.sentinel.android.app.backend;
/**
 * Created by laurentmeyer on 15/12/15.
 */

import android.content.Context;

import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpPost;
import com.koushikdutta.async.http.body.MultipartFormDataBody;

import java.io.File;

import de.tum.in.i22.sentinel.android.app.Constants;

/**
 * Class which is responsible for sending the APK to the server, it doesn't deal with the PlayStore at all
 */
public class APKSender {

    private static APKSender instance = null;

    /**
     * Returns an instance of the APKSender, singleton, same pattern as {@see de.tum.in.i22.sentinel.android.app.backend.APKReceiver}
     * @return Instance of APKSender
     */
    public static APKSender getInstance() {
        if (instance == null) {
            instance = new APKSender();
        }
        return instance;
    }

    /**
     * Send the files to the server as a multipart upload
     *
     * The difference between meta and no meta is due to the fact that the server has two endpoints
     * (because of a RAML-Code-Generation Limitation) -> one endpoint is used when the clients send
     * metadata with the package (appName, packageName, logo) and the other when they just send
     * the sources, sinks, taint and apk.
     *
     * @param pathToSources         : the file containing the sources (NOT NULL)
     * @param pathToSinks           : the file containing the sinks (NOT NULL)
     * @param pathToTaintWrapper    : the file containing the taintWrapper (NOT NULL)
     * @param apk                   : the apk File to be instrumented, obviously NOT NULL
     * @param callback              : the callback happening after the network has been executed (successful or not)
     * @param logo                  : the logo of the app (OPTIONAL)
     * @param appName               : the name of the app to instrument (as a String) and also OPTIONAL
     * @param packageName           : the package name of the app to instrument and also OPTIONAL
     */
    public void sendFiles(File pathToSources, File pathToSinks, File pathToTaintWrapper, File apk, AsyncHttpClient.StringCallback callback, File logo, String appName, String packageName, Context c) {
        AsyncHttpPost post;
        MultipartFormDataBody body = new MultipartFormDataBody();
        if (appName != null && logo != null && packageName != null){
            post = new AsyncHttpPost(Constants.getServerAddress(c) + Constants.SERVER_INSTRUMENTATION_ENDPOINT_META);
            body.addFilePart(Constants.SERVER_LOGO_FILE, logo);
            body.addStringPart(Constants.SERVER_APP_NAME, appName);
            body.addStringPart(Constants.SERVER_PACKAGE_NAME, packageName);
        }
        else {
            post = new AsyncHttpPost(Constants.getServerAddress(c) + Constants.SERVER_INSTRUMENTATION_ENDPOINT_NO_META);
        }
        body.addFilePart(Constants.SERVER_APK_FILE, apk);
        body.addFilePart(Constants.SERVER_SOURCE_FILE, pathToSources);
        body.addFilePart(Constants.SERVER_SINK_FILE, pathToSinks);
        body.addFilePart(Constants.SERVER_TAINT_WRAPPER, pathToTaintWrapper);
        body.addStringPart(Constants.SERVER_PUBLIC_FLAG, "true");
        post.setBody(body);
        AsyncHttpClient.getDefaultInstance().executeString(post, callback);
    }
}