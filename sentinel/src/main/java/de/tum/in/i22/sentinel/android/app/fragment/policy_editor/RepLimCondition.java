package de.tum.in.i22.sentinel.android.app.fragment.policy_editor;

import java.util.ArrayList;

/**
 * Created by laurentmeyer on 23/12/15.
 */
public class RepLimCondition extends SuperCondition {

    ArrayList<RepLim> repLims;

    @Override
    String createAttributeString() {
        return "";
    }

    @Override
    String createValueString() {
        StringBuilder builder = new StringBuilder();
        for (RepLim rl : repLims){
            builder.append(rl+"\n");
        }
        return builder.toString();
    }
}
