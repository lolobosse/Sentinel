package de.tum.in.i22.sentinel.android.app.backend;

/**
 * Created by laurentmeyer on 24/01/16.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

import java.util.List;

import de.tum.in.i22.sentinel.android.app.Constants;

/**
 * This class is used to check if a package has been instrumented and some other fancy stuff
 * about currently installed APKs
 */
public class APKUtils {

    /**
     * Checks if the package has been instrumented by checking the signature of the package --> is it the same as the server
     * @param packageName: package name which we need to lookup
     * @param c: Needed to get the {@see android.content.pm.PackageManager}
     * @return boolean: has the package the signature of our instrumentation server?
     */
    public static boolean isInstrumented(String packageName, Context c) {
        Signature[] sigs;
        SharedPreferences p = c.getSharedPreferences(Constants.SENTINEL, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = p.edit();
        try {
            sigs = c.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES).signatures;
            boolean isInstrumented = false;
            for (Signature sig : sigs) {
                if (sig.hashCode() == Constants.SERVER_SIGNATURE_HASH_CODE)
                    isInstrumented = true;
            }
            e.putBoolean(packageName, isInstrumented);
            e.apply();
            return isInstrumented;
        } catch (PackageManager.NameNotFoundException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * Iterate through all the packages and finds out which one has the DroidForce server signature
     * @param c: Context to be passed at {@see de.tum.in.i22.sentinel.android.app.backend.APKUtils#isInstrumented(java.lang.String, android.content.Context)}
     * @return the number of instrumented apps on the device
     */
    public static int getNumberOfInstrumentedApp(Context c) {
        List<ApplicationInfo> infos = installedApplications(c);
        int i = 0;
        for (ApplicationInfo info : infos) {
            if (isInstrumented(info.packageName, c))
                i++;
        }
        return i;
    }

    /**
     * Returns the list of the apps the {@see android.content.pm.PackageManager} knows about
     * @param c: The Context is used to get the PackageManager
     * @return all the packages, the Package Manager knows about
     */
    public static List<ApplicationInfo> installedApplications(Context c) {
        PackageManager pm = c.getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        return packages;
    }

    /**
     * Method to see if we need to uninstall an app before installing another instrumented one
     * @param c: Context
     * @param packageName: Name of the package we'd like to implement
     * @return if the package is already on the device
     */
    public static boolean isInstalled (Context c, String packageName){
        List<ApplicationInfo> infos = installedApplications(c);
        for (ApplicationInfo i : infos){
            if (i.packageName.equals(packageName)){
                return true;
            }
        }
        return false;
    }


}
