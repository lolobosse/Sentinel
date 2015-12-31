package de.tum.in.i22.sentinel.android.app.package_getter;
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

    public static class Package {

        boolean isThirdParty;
        String packageName;
        boolean isInstrumented;
        String path;

        @Override
        public String toString() {
            return "Package{" +
                    "packageName='" + packageName + '\'' +
                    ", path='" + path + '\'' + '}';
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

        public Package(boolean isInstrumented, boolean isThirdParty, String packageName, String path) {
            this.isInstrumented = isInstrumented;
            this.isThirdParty = isThirdParty;
            this.packageName = packageName;
            this.path = path;
        }

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
    }

    /**
     * You get with this command every single app of the device.
     *
     * @param callback:   callback object to be called when the system is done with the execution of the command.
     */
    public static void getPackages(Callback callback) {
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

        ArrayList<Package> allPackages = getAllPackages(callback);
        addTheThirdPartyInfo(allPackages, callback);
        // checkIfInstrumented(allPackages, callback);
        if (callback != null) {
            callback.onSuccess(allPackages);
        }
    }

    private static void addTheThirdPartyInfo(ArrayList<Package> allPackages, Callback callback) {
        String command = "pm list packages -f -3";
        Process process;
        try {
            ArrayList<Package> packages = new ArrayList<>();
            process = Runtime.getRuntime().exec(command);
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            String cLine;
            while ((cLine = bufferedReader.readLine()) != null) {
                Package p = createPackageObjectFromString(cLine);
                if (p != null) {
                    // TODO: Get the icons
                    // Drawable icon = getContext().getPackageManager().getApplicationIcon(pkg);
                    packages.add(p);
                }
            }
            if (!packages.isEmpty()) {
                for (Package p : packages) {
                    if (allPackages.contains(p)) {
                        allPackages.get(allPackages.indexOf(p)).setIsThirdParty(true);
                    }
                }
            }
        } catch (IOException e) {
            if (callback != null) {
                callback.onError(e);
            }
        }
    }

    private static ArrayList<Package> getAllPackages(Callback callback) {
        String command = "pm list packages -f";
        Process process;
        try {
            ArrayList<Package> packages = new ArrayList<>();
            process = Runtime.getRuntime().exec(command);
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            String cLine;
            while ((cLine = bufferedReader.readLine()) != null) {
                Package p = createPackageObjectFromString(cLine);
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

    private static Package createPackageObjectFromString(String fromDevice) {
        Pattern p = Pattern.compile(buildPackageAndPathRegex());
        Matcher m = p.matcher(fromDevice);
        if (m.matches()) {
            Package createPackage = new Package(false, false, m.group(4), m.group(2));
            return createPackage;
        }
        return null;
    }

    private static String buildPackageAndPathRegex() {
        /*
        "package" is always there, then it's the path of the app to be, potentially, extracted.
        then comes the package name.
         */
        return "(package:)([a-zA-Z/.\\-0-9]*)(=)([A-Za-z.]*)";
    }


    public interface Callback {
        void onError(Exception e);
        void onSuccess(List<Package> packages);
    }

}
