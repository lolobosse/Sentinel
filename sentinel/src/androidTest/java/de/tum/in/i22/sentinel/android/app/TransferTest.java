package de.tum.in.i22.sentinel.android.app;

import android.test.AndroidTestCase;

import java.io.File;

/**
 * Created by laurentmeyer on 30/01/16.
 */
public class TransferTest extends AndroidTestCase {

    public void testArePoliciesThere() throws Exception {
        Utils.passPoliciesFromRawToFile(getContext());
        String[] array = {"Policy.xml", "Policy_App_SMS_1.xml", "Policy_App_SMS_Duration_2.xml", "Policy_App_SMS_Duration_4.xml",
                "Policy_Draft.xml", "Policy_DroidBench.xml", "Policy_No_Imei_to_12345.xml", "Policy_No_Location.xml", "Policy_Old.xml"};
        for (String policy : array){
            checkFilePresence(policy);
        }
    }

    private void checkFilePresence(String s){
        File sdcard = new File("/sdcard/SentinelPolicies/");
        File toCheck = new File(sdcard, s);
        assertTrue(toCheck.exists() && !toCheck.isDirectory());
    }
}
