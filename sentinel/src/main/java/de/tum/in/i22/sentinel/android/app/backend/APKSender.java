package de.tum.in.i22.sentinel.android.app.backend;
/**
 * Created by laurentmeyer on 15/12/15.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;

import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpPost;
import com.koushikdutta.async.http.body.MultipartFormDataBody;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import de.tum.in.i22.sentinel.android.app.Constants;
import de.tum.in.i22.sentinel.android.app.package_getter.PackageGetter;

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
     * @param pathToSources         : the file containing the sources (NOT NULL)
     * @param pathToSinks           : the file containing the sinks (NOT NULL)
     * @param pathToTaintWrapper    : the file containing the taintWrapper (NOT NULL)
     * @param apk                   : the apk File to be instrumented, obviously NOT NULL
     * @param callback              : the callback happening after the network has been executed (successful or not)
     * @param logo                  : the logo of the app (OPTIONAL)
     * @param appName               : the name of the app to instrument (as a String) and also OPTIONAL
     * @param packageName           : the package name of the app to instrument and also OPTIONAL
     */
    public void sendFiles(File pathToSources, File pathToSinks, File pathToTaintWrapper, File apk, AsyncHttpClient.StringCallback callback, File logo, String appName, String packageName) {
        AsyncHttpPost post = new AsyncHttpPost(Constants.SERVER_ADDRESS + Constants.SERVER_INSTRUMENTATION_ENDPOINT);
        MultipartFormDataBody body = new MultipartFormDataBody();
        body.addFilePart(Constants.SERVER_APK_FILE, apk);
        body.addFilePart(Constants.SERVER_SOURCE_FILE, pathToSources);
        body.addFilePart(Constants.SERVER_SINK_FILE, pathToSinks);
        body.addFilePart(Constants.SERVER_TAINT_WRAPPER, pathToTaintWrapper);
        if (logo != null){
            body.addFilePart(Constants.SERVER_LOGO_FILE, logo);
        }
        if (appName != null){
            body.addStringPart(Constants.SERVER_APP_NAME, appName);
        }
        if (packageName != null){
            body.addStringPart(Constants.SERVER_PACKAGE_NAME, packageName);
        }
        // TODO Have a UI and a parameter for that
        body.addStringPart(Constants.SERVER_PUBLIC_FLAG, "true");
        post.setBody(body);
        AsyncHttpClient.getDefaultInstance().executeString(post, callback);
    }
}