package de.tum.in.i22.sentinel.android.app.backend;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpGet;
import com.koushikdutta.async.http.AsyncHttpResponse;

import java.io.File;

import de.tum.in.i22.sentinel.android.app.Constants;

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

    /**
     * Very simple method to retrieve the apk from the server, sending the wished hash
     * @param hash: the hash of the wished APK, you can just hash your APK file with {@see de.tum.in.i22.sentinel.android.app.package_getter.Hash#createHashForFile(java.io.File)}
     * @param callback
     */

    public void getFile(String hash, AsyncHttpClient.FileCallback callback){
        String serverAddress = Constants.SERVER_ADDRESS;
        File repo = new File(Environment.getExternalStorageDirectory()+"/instrumentedApk/");
        repo.mkdirs();
        File output = new File(repo, hash+".apk");
        String filename = output.getAbsolutePath();
        AsyncHttpClient.getDefaultInstance().executeFile(new AsyncHttpGet("http://"+serverAddress+"/instrument/"+ hash), filename, callback);
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
}
