package de.tum.in.i22.sentinel.android.app.serverTest;

import android.test.AndroidTestCase;
import android.util.Log;

import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpResponse;

import java.io.File;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import de.tum.in.i22.sentinel.android.app.Constants;
import de.tum.in.i22.sentinel.android.app.R;
import de.tum.in.i22.sentinel.android.app.Utils;
import de.tum.in.i22.sentinel.android.app.backend.APKReceiver;
import de.tum.in.i22.sentinel.android.app.backend.APKSender;
import de.tum.in.i22.sentinel.android.app.package_getter.Hash;

/**
 * Created by laurentmeyer on 30/01/16.
 */
public class UploadTest extends AndroidTestCase {

    String instrumentedFilename = "instrumented.apk";
    String nonInstrumentedFilename = "non_instrumented.apk";

    private static final long TIMEOUT = 200000L;



    @Override
    protected void setUp() throws Exception {
        super.setUp();
        // Put the raw file in the internal files
        Utils.writeToFile(instrumentedFilename, R.raw.instrumented, getContext(), null);
        Utils.writeToFile(nonInstrumentedFilename, R.raw.not_instrumented, getContext(), null);
    }

    public void testGlobalSuccessfulWorkflow() throws Exception {
        final Semaphore semaphore = new Semaphore(0);
        File pathToSources = new File(getContext().getFilesDir(), Constants.SOURCES);
        File pathToSinks = new File(getContext().getFilesDir(), Constants.SINKS);
        File pathToTaintWrapper = new File(getContext().getFilesDir(), Constants.TAINT);
        File apk = new File(getContext().getFilesDir(), nonInstrumentedFilename);
        final String hash = Hash.createHashForFile(apk);
        APKSender.getInstance().sendFiles(pathToSources, pathToSinks, pathToTaintWrapper, apk, new AsyncHttpClient.StringCallback() {
            @Override
            public void onCompleted(Exception e, AsyncHttpResponse asyncHttpResponse, String s) {
                Log.d("UploadTest", "asyncHttpResponse:" + asyncHttpResponse);
                assertEquals("The server didn't return 202", 202, asyncHttpResponse.code());
                semaphore.release();
            }
        }, null, null, null);
        assertTrue("Timeout on upload", semaphore.tryAcquire(TIMEOUT, TimeUnit.MILLISECONDS));

        Thread.sleep(15000);
        // We could maybe have used the same Semaphore, but we find it clearer with two.
        final Semaphore semaphoreSuccessful = new Semaphore(0);
        APKReceiver.getInstance().getFile(hash, new AsyncHttpClient.FileCallback() {
            @Override
            public void onCompleted(Exception e, AsyncHttpResponse asyncHttpResponse, File file) {
                assertEquals("The server didn't return 200", 200, asyncHttpResponse.code());
                String hashInstrumented = Hash.createHashForFile(file);
                assertNotSame("The app is the same, hasn't been instrumented", hash, hashInstrumented);
                semaphoreSuccessful.release();
            }
        });
        assertTrue("Timeout on app retrieving", semaphoreSuccessful.tryAcquire(TIMEOUT, TimeUnit.MILLISECONDS));

    }


    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        // Delete the raw file which have been copied
        File instrumented = new File(getContext().getFilesDir(), instrumentedFilename);
        File notInstrumented = new File(getContext().getFilesDir(), nonInstrumentedFilename);
        instrumented.delete();
        notInstrumented.delete();
    }
}
