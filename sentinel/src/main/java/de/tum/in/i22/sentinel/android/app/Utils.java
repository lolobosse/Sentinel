package de.tum.in.i22.sentinel.android.app;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;


/**
 * Created by laurentmeyer on 23/12/15.
 */
public class Utils {

    public static void initDefaultFiles(Context context) {
        try {
            String[] definitionType = {Constants.SINKS, Constants.SOURCES, Constants.TAINT};
            int[] definitionFiles = {R.raw.sinks, R.raw.sources, R.raw.taint};
            for (int i = 0; i < definitionFiles.length; i++) {
                int definitionRes = definitionFiles[i];
                String filename = definitionType[i];
                writeToFile(filename, definitionRes, context, null);

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void writeToFile(String filename, int definitionRes, Context c, String filePath){
        try {
            InputStream in = c.getResources().openRawResource(definitionRes);
            FileOutputStream out;
            if (filePath == null){
                out = new FileOutputStream(new File(c.getFilesDir(), filename));
            }
            else {
                // create a File object for the parent directory
                File parentDirectory = new File(filePath);
                // have the object build the directory structure, if needed.
                parentDirectory.mkdirs();
                // create a File object for the output file
                File outputFile = new File(parentDirectory, filename);
                // now attach the OutputStream to the file object, instead of a String representation
                out = new FileOutputStream(outputFile);
            }
            byte[] buff = new byte[1024];
            int read = 0;
            while ((read = in.read(buff)) > 0) {
                out.write(buff, 0, read);
            }
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * This method is only there to allow the user to use the policy which were available in the examples
     * To do so, they moved to a visible folder of the device: the folder "SentinelPolicies"
     * @param c
     */
    public static void passPoliciesFromRawToFile(Context c){
        String where = Environment.getExternalStorageDirectory().getPath() + "/SentinelPolicies/";
        writeToFile("Policy.xml", R.raw.policy, c, where);
        writeToFile("Policy_App_SMS_1.xml", R.raw.policy_appsms1, c, where);
        writeToFile("Policy_App_SMS_Duration_2.xml", R.raw.policy_appsms_duration2, c, where);
        writeToFile("Policy_App_SMS_Duration_4.xml", R.raw.policy_appsms_duration4, c, where);
        writeToFile("Policy_Draft.xml", R.raw.policy_draft, c, where);
        writeToFile("Policy_DroidBench.xml", R.raw.policy_droidbench_androidspecific_directleak1, c, where);
        writeToFile("Policy_No_Imei_to_12345.xml", R.raw.policy_no_imei_to_12345, c, where);
        writeToFile("Policy_No_Location.xml", R.raw.policy_no_location, c, where);
        writeToFile("Policy_Old.xml", R.raw.policy_old, c, where);

    }
}
