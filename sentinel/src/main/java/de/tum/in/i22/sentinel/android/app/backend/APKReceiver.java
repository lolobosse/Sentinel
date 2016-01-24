package de.tum.in.i22.sentinel.android.app.backend;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpGet;

import java.io.File;

import de.tum.in.i22.sentinel.android.app.Constants;

/**
 * Created by laurentmeyer on 18/01/16.
 * Class which handles the post-instrumentation process
 */
public class APKReceiver {

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
     * @param c: #Context is needed to start the intent
     * @param path: Path to APK
     */
    public void installApk(Context c, String path){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(path)), Constants.APK_TYPE);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // without this flag android returned a intent error!
        c.startActivity(intent);
    }

    /**
     * This methods uninstall an APK by prompting the user a dialog to confirm
     * @see <a href="http://stackoverflow.com/a/21854473/2545832">This thread</a>
     * @param c: {@see android.content.Context} needed to start an Activity
     * @param packageName: The package to be removed
     */
    public void uninstallApk(Context c, String packageName) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse(packageName));
        c.startActivity(intent);
    }
}
