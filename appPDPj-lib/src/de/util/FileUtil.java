package de.util;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import de.tum.in.i22.uc.pdp.android.R;


public class FileUtil {

    public static void copyPolicyFileFromAssetsToInternalStorage(Context context, String srcFile, String targetFolder) throws IOException {
        writeToFile(srcFile, R.raw.event_information, context);
    }


    private static void writeToFile(String filename, int definitionRes, Context c) {
        try {
            InputStream in = c.getResources().openRawResource(definitionRes);
            FileOutputStream out = c.openFileOutput(filename, Context.MODE_PRIVATE);

            byte[] buff = new byte[1024];
            int read = 0;
            while ((read = in.read(buff)) > 0) {
                out.write(buff, 0, read);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }
}
