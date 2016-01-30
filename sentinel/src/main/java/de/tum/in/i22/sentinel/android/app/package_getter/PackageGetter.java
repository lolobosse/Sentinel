package de.tum.in.i22.sentinel.android.app.package_getter;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by laurentmeyer on 15/12/15.
 */
public class PackageGetter {

    /**
     * You get with this command every single third party app of the device.
     * Convenience method for #getThirdPartyPackages
     * @param callback: callback object to be called when the system is done with the execution of the command.
     */
    public static void getPackages(Callback callback, Context c) {
        /*
        Google doc is saying that for `list package`
        -f: See their associated file.
        -d: Filter to only show disabled packages.
        -e: Filter to only show enabled packages.
        -s: Filter to only show system packages.
        -3: Filter to only show third party packages.
        -i: See the installer for the packages.
        -u: Also include uninstalled packages.
        --user <USER_ID>: The user space to query.
         */

        ArrayList<Package> allPackages = getThirdPartyPackages(c, callback);
        if (callback != null) {
            callback.onSuccess(allPackages);
        }
    }

    /**
     * Used to retrieve the 3rd party apps of the device which are the only ones which can be accessed
     * without being root.
     * @param c: context to get the icon
     * @param callback: the callback is only used here in case of error; the #getPackages manage the
     *                success.
     * @return
     */
    private static ArrayList<Package> getThirdPartyPackages(Context c, Callback callback) {
        String command = "pm list packages -f -3";
        Process process;
        try {
            ArrayList<Package> packages = new ArrayList<>();
            process = Runtime.getRuntime().exec(command);
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            String cLine;
            while ((cLine = bufferedReader.readLine()) != null) {
                Package p = createPackageObjectFromString(cLine, c);
                if (p != null) {
                    packages.add(p);
                }
            }
            return packages;
        } catch (IOException e) {
            if (callback != null) {
                callback.onError(e);
            }
            return null;
        }
    }

    /**
     * Matches the regex and create a package from the results.
     * @param fromDevice: what the device returned us
     * @param c: the context, needed for the icon
     * @return
     */
    private static Package createPackageObjectFromString(String fromDevice, Context c) {
        Pattern p = Pattern.compile(buildPackageAndPathRegex());
        Matcher m = p.matcher(fromDevice);
        if (m.matches()) {
            Package createPackage = new Package(c, false, false, m.group(4), m.group(2));
            return createPackage;
        }
        return null;
    }

    /**
     * Here is the heart of the logic, all is based on the parsing of what the system returns us as
     * found packages.
     * @return
     */
    private static String buildPackageAndPathRegex() {
        /*
        "package" is always there, then it's the path of the app to be, potentially, extracted.
        then comes the package name.
         */
        return "(package:)([a-zA-Z\\/.\\-0-9\\_]*)(=)([A-Za-z\\.\\d\\_]*)";
    }

    /**
     * Implemented on the other side, needed to execute different behaviour in case of error or success.
     */
    public interface Callback {
        void onError(Exception e);
        void onSuccess(List<Package> packages);
    }

    /**
     * Very simple class representing an app on the Android system.
     * Can be compared at #ApplicationInfo
     */
    public static class Package {

        boolean isThirdParty;
        String packageName;
        boolean isInstrumented;
        String path;
        String name;
        Drawable packagePicture;

        public Package(Context c, boolean isInstrumented, boolean isThirdParty, String packageName, String path) {
            this.isInstrumented = isInstrumented;
            this.isThirdParty = isThirdParty;
            this.packageName = packageName;
            this.path = path;
            // We try to get the icon from the package manager, that's why we needed the context.
            try {
                this.packagePicture = c.getPackageManager().getApplicationIcon(this.packageName);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            this.name = this.getProperlyFormattedPackageName(packageName, c);
        }

        public boolean isInstrumented() {
            return isInstrumented;
        }

        public void setIsInstrumented(boolean isInstrumented) {
            this.isInstrumented = isInstrumented;
        }

        public boolean isThirdParty() {
            return isThirdParty;
        }

        public void setIsThirdParty(boolean isThirdParty) {
            this.isThirdParty = isThirdParty;
        }

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        /**
         * Equals is purely based on the packageName.
         * @param o: object to be compared to
         * @return is the package name the same as the one of the object being compared to.
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Package aPackage = (Package) o;

            return !(packageName != null ? !packageName.equals(aPackage.packageName) : aPackage.packageName != null);

        }

        @Override
        public int hashCode() {
            return packageName != null ? packageName.hashCode() : 0;
        }

        @Override
        public String toString() {
            return name;
        }

        public Drawable getPackagePicture() {
            return packagePicture;
        }

        public void setPackagePicture(Drawable packagePicture) {
            this.packagePicture = packagePicture;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        /**
         * To retrieve the commercial name of the apps being displayed
         * @param packageName: packageName of the seached app
         * @param c: the context is useful to call the package manager
         * @return: the commercial name of the app.
         */
        private String getProperlyFormattedPackageName(String packageName, Context c) {
            PackageManager pm = c.getPackageManager();
            ApplicationInfo ai;
            try {
                ai = pm.getApplicationInfo(packageName, 0);
            } catch (PackageManager.NameNotFoundException e) {
                ai = null;
            }
            return (String) (ai != null ? pm.getApplicationLabel(ai) : "(Unknown)");
        }
    }

}
