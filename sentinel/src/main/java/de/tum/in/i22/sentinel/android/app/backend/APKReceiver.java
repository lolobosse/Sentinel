package de.tum.in.i22.sentinel.android.app.backend;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.Fragment;

import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpGet;

import java.io.File;

import de.tum.in.i22.sentinel.android.app.Constants;

/**
 * Created by laurentmeyer on 18/01/16.
 * Class which handles the post-instrumentation process
 */
public class APKReceiver {

    // Do not move them, this constant are making more sense here
    public static final int REQUEST_INSTALLATION = 0;
    public static final int REQUEST_UNINSTALLATION = 1;

    private static APKReceiver instance = null;

    /**
     * Returns an instance of the APKReceiver, it is a singleton
     * @return Instance of the APKReceiver
     */
    public static APKReceiver getInstance() {
        if (instance == null) {
            instance = new APKReceiver();
        }
        return instance;
    }


    public void getFileFromDownloadUrl(String downloadUrl, AsyncHttpClient.FileCallback callback){
        // TODO: Could be better
        getFile(downloadUrl.replace("http://lapbroyg58.informatik.tu-muenchen.de:443/instrument/", ""), callback);
    }

    /**
     * Very simple method to retrieve the apk from the server, sending the wished hash
     * @param hash: the hash of the wished APK, you can just hash your APK file with {@see de.tum.in.i22.sentinel.android.app.package_getter.Hash#createHashForFile(java.io.File)}
     * @param callback: the callback fot the network call, it should use the {@see de.tum.in.i22.sentinel.android.app.backend.APKReceiver#installApk(android.content.Context, java.lang.String)}
     */

    public void getFile(String hash, AsyncHttpClient.FileCallback callback){
        File repo = new File(Environment.getExternalStorageDirectory()+"/instrumentedApk/");
        repo.mkdirs();
        File output = new File(repo, hash+".apk");
        String filename = output.getAbsolutePath();
        AsyncHttpClient.getDefaultInstance().executeFile(new AsyncHttpGet(Constants.SERVER_ADDRESS+Constants.SERVER_INSTRUMENTATION_ENDPOINT + "/" + hash), filename, callback);
    }


    /**
     * This method installs the APK on the system
     * @see <a href="http://www.stackoverflow.com/a/4969421/2545832">This thread</a>
     * @param c: #Fragment is needed to start the intent
     * @param path: Path to APK
     */
    public void installApk(Fragment c, String path){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(path)), Constants.APK_TYPE);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // without this flag android returned a intent error!
        c.startActivityForResult(intent, REQUEST_INSTALLATION);
    }

    /**
     * This methods uninstall an APK by prompting the user a dialog to confirm
     * @see <a href="http://stackoverflow.com/a/21854473/2545832">This thread</a>
     * @param c: #Fragment needed to start an Activity
     * @param packageName: The package to be removed
     */
    public void uninstallApk(Fragment c, String packageName) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse(packageName));
        c.startActivityForResult(intent, REQUEST_UNINSTALLATION);
    }

    /**
     * This method installs the APK on the system
     * @see <a href="http://www.stackoverflow.com/a/4969421/2545832">This thread</a>
     * @param c: #Fragment is needed to start the intent
     * @param path: Path to APK
     */
    public void installApk(Activity c, String path){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(path)), Constants.APK_TYPE);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // without this flag android returned a intent error!
        c.startActivityForResult(intent, REQUEST_INSTALLATION);
    }

    /**
     * This methods uninstall an APK by prompting the user a dialog to confirm
     * @see <a href="http://stackoverflow.com/a/21854473/2545832">This thread</a>
     * @param c: #Fragment needed to start an Activity
     * @param packageName: The package to be removed
     */
    public void uninstallApk(Activity c, String packageName) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse(packageName));
        c.startActivityForResult(intent, REQUEST_UNINSTALLATION);
    }
}
