package de.tum.in.i22.sentinel.android.app.backend;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpGet;

import java.io.File;

import de.tum.in.i22.sentinel.android.app.Constants;
import de.tum.in.i22.sentinel.android.app.fragment.SettingsFragment;

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
     *
     * @return Instance of the APKReceiver
     */
    public static APKReceiver getInstance() {
        if (instance == null) {
            instance = new APKReceiver();
        }
        return instance;
    }

    /**
     * This method is needed because we need to get the hash from the json list we get.
     * BUT THIS IS UGLY
     * @param downloadUrl: the download url from the list
     * @param callback: what has to be done when the file is downloaded {@see com.koushikdutta.async.http.AsyncHttpClient.FileCallback}
     */
    public void getFileFromDownloadUrl(String downloadUrl, AsyncHttpClient.FileCallback callback, Context c) {
        String[] segments = downloadUrl.split("/");
        String last = segments[segments.length-1];
        getFile(last, callback, c);
    }

    /**
     * Very simple method to retrieve the apk from the server, sending the wished hash
     *
     * @param hash:     the hash of the wished APK, you can just hash your APK file with {@see de.tum.in.i22.sentinel.android.app.package_getter.Hash#createHashForFile(java.io.File)}
     * @param callback: the callback fot the network call, it should use the {@see de.tum.in.i22.sentinel.android.app.backend.APKReceiver#installApk(android.content.Context, java.lang.String)}
     */

    public void getFile(String hash, AsyncHttpClient.FileCallback callback, Context c) {
        File defaultRepo = new File(Environment.getExternalStorageDirectory() + "/instrumentedApk/");
        File userSpecifiedRepo = (SettingsFragment.savedAPKFolder != null) ? new File(SettingsFragment.savedAPKFolder) : defaultRepo;
        File repo = (SettingsFragment.saveAPK) ? userSpecifiedRepo : defaultRepo;
        repo.mkdirs();
        File output = new File(repo, hash + ".apk");
        String filename = output.getAbsolutePath();
        Log.d("APKReceiver", Constants.getServerAddress(c) + Constants.SERVER_INSTRUMENTATION_ENDPOINT + "/" + hash);
        AsyncHttpClient.getDefaultInstance().executeFile(new AsyncHttpGet(Constants.getServerAddress(c) + Constants.SERVER_INSTRUMENTATION_ENDPOINT + "/" + hash), filename, callback);
    }


    /**
     * This method installs the APK on the system
     *
     * @param fragment:    #Fragment is needed to start the intent
     * @param path: Path to APK
     * @see <a href="http://www.stackoverflow.com/a/4969421/2545832">This thread</a>
     */
    public void installApk(Fragment fragment, String path) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(path)), Constants.APK_TYPE);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // without this flag android returned a intent error!
        fragment.startActivityForResult(intent, REQUEST_INSTALLATION);
    }

    /**
     * This methods uninstall an APK by prompting the user a dialog to confirm
     *
     * @param fragment:           #Fragment needed to start an Activity
     * @param packageName: The package to be removed
     * @see <a href="http://stackoverflow.com/a/21854473/2545832">This thread</a>
     */
    public void uninstallApk(final Fragment fragment, String packageName) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse(packageName));
        if (!fragment.getActivity().getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).isEmpty()) {
            fragment.startActivityForResult(intent, REQUEST_UNINSTALLATION);
        } else {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(fragment.getActivity(), "Please uninstall the application manually, we cannot do it without being root", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    /**
     * This method installs the APK on the system
     *
     * @param activity:    #Fragment is needed to start the intent
     * @param path: Path to APK
     * @see <a href="http://www.stackoverflow.com/a/4969421/2545832">This thread</a>
     */
    public void installApk(Activity activity, String path) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(path)), Constants.APK_TYPE);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // without this flag android returned a intent error!
        activity.startActivityForResult(intent, REQUEST_INSTALLATION);
    }

    /**
     * This methods uninstall an APK by prompting the user a dialog to confirm
     *
     * @param activity:           #Fragment needed to start an Activity
     * @param packageName: The package to be removed
     * @see <a href="http://stackoverflow.com/a/21854473/2545832">This thread</a>
     */
    public void uninstallApk(final Activity activity, String packageName) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse(packageName));
        if (!activity.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).isEmpty()) {
            activity.startActivityForResult(intent, REQUEST_UNINSTALLATION);
        } else {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, "Please uninstall the application manually, we cannot do it without being root", Toast.LENGTH_LONG).show();
                }
            });
        }
        activity.startActivityForResult(intent, REQUEST_UNINSTALLATION);
    }
}
