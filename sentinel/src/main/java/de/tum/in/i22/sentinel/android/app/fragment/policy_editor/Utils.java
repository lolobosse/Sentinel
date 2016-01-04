package de.tum.in.i22.sentinel.android.app.fragment.policy_editor;

import android.content.Context;

import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;

import java.io.InputStream;

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
}
