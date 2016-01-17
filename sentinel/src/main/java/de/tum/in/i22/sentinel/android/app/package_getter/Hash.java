package de.tum.in.i22.sentinel.android.app.package_getter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;

/**
 * Created by laurentmeyer on 13/01/16.
 */
public class Hash {

    public static String createHashForPackage(PackageGetter.Package p){
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA-512");
            String sha512Hash = Hex.encodeHex(messageDigest.digest(IOUtils.toByteArray(new FileInputStream(new File(p.getPath()))))).toString();
            return sha512Hash;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
