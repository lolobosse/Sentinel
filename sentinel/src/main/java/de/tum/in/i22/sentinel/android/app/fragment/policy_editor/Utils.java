package de.tum.in.i22.sentinel.android.app.fragment.policy_editor;

import android.content.Context;

import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import de.tum.in.i22.sentinel.android.app.R;
import de.tum.in.i22.sentinel.android.app.fragment.InstrumentFragment;
import de.tum.in.www22.enforcementlanguage.PolicyType;

/**
 * Created by laurentmeyer on 23/12/15.
 */
public class Utils {

    public static String createAttributeString(String key, String value) {
        return key + "=\"" + value + "\" ";
    }

    public static PolicyType getPolicyFromRaw(Context c, int rawId) {
        IBindingFactory bfact = null;
        try {
            bfact = BindingDirectory.getFactory(PolicyType.class);
            IUnmarshallingContext uctx = bfact.createUnmarshallingContext();
            InputStream in = c.getResources().openRawResource(rawId);
            return (PolicyType) uctx.unmarshalDocument(in, null);
        } catch (JiBXException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void initDefaultFiles(Context context) {
        try {
            String[] names = {InstrumentFragment.SINKS, InstrumentFragment.SOURCES, InstrumentFragment.TAINT};
            int[] toLookFor = {R.raw.sinks, R.raw.sources, R.raw.taint};
            for (int i = 0; i < toLookFor.length; i++) {
                int filePath = toLookFor[i];
                InputStream in = context.getResources().openRawResource(filePath);
                FileOutputStream out = new FileOutputStream(new File(context.getFilesDir(), names[i]));
                byte[] buff = new byte[1024];
                int read = 0;
                while ((read = in.read(buff)) > 0) {
                    out.write(buff, 0, read);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
