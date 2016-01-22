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
     * Method used currently
     * TODO: Docu and refactor of the variable names
     *
     * @param pathToSources
     * @param pathToSinks
     * @param pathToTaintWrapper
     * @param apk
     * @param callback
     */
    public void sendFiles(Context c, File pathToSources, File pathToSinks, File pathToTaintWrapper, File apk, AsyncHttpClient.StringCallback callback, File logo, String appName, String packageName) {
        // logo, appName, packageName
        String serverAddress = "192.168.0.111";
        AsyncHttpPost post = new AsyncHttpPost("http://" + serverAddress + ":8080/instrument");
        MultipartFormDataBody body = new MultipartFormDataBody();
        body.addFilePart("apkFile", apk);
        body.addFilePart("sourceFile", pathToSources);
        body.addFilePart("sinkFile",pathToSinks);
        body.addFilePart("easyTaintWrapperSource", pathToTaintWrapper);
        if (logo != null){
            body.addFilePart("logo",logo);
        }
        if (appName != null){
            body.addStringPart("appName", appName);
        }
        if (packageName != null){
            body.addStringPart("packageName", packageName);
        }
        post.setBody(body);
        AsyncHttpClient.getDefaultInstance().executeString(post, callback);
    }
}