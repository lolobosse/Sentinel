package de.tum.in.i22.sentinel.android.app;

import android.test.AndroidTestCase;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import de.tum.in.i22.sentinel.android.app.package_getter.PackageGetter;

/**
 * Created by Moderbord on 2016-01-12.
 */

public class AppPickingProcessTest extends AndroidTestCase {

    int numberOfPackages;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        String command = "pm list packages -f";
        Process process;
        try {
            process = Runtime.getRuntime().exec(command);
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            String cLine;
            while ((cLine = bufferedReader.readLine()) != null) {
                if (cLine.contains("package"))
                    numberOfPackages++;
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public void testNumberOfPackages() throws Exception {
        PackageGetter.getPackages(new PackageGetter.Callback() {
            @Override
            public void onError(Exception e) {
                assertTrue("The getter returned an error", false);
            }

            @Override
            public void onSuccess(List<PackageGetter.Package> packages) {
                assertEquals("Number of packages is not consistent", numberOfPackages, packages.size());
            }
        }, getContext());

    }
}
