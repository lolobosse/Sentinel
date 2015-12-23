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
        CustomStringBuilder builder = new CustomStringBuilder();
        for (RepLim rl : repLims){
            builder.append(rl+"\n");
        }
        return builder.toString();
    }

    public ArrayList<RepLim> getRepLims() {
        return repLims;
    }

    public void setRepLims(ArrayList<RepLim> repLims) {
        this.repLims = repLims;
    }
}
