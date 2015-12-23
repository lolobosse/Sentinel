package de.tum.in.i22.sentinel.android.app.fragment.policy_editor;

import java.util.ArrayList;

/**
 * Created by laurentmeyer on 23/12/15.
 */
public class RepLim extends EventMatchConditionContainer{

    String amount, unit;
    int lowerLimit, upperLimit;

    String amountKey = "amount";
    String unitKey = "unit";
    String lowerLimitKey = "lowerLimit";
    String upperLimitKey = "upperLimit";

    RepLim(){
        super();
        elementXMLName = "within";
    }

    @Override
    String createAttributeString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Utils.createAttributeString(amountKey, amount))
                .append(Utils.createAttributeString(unitKey, unit))
                .append(Utils.createAttributeString(lowerLimitKey, String.valueOf(lowerLimit)))
                .append(Utils.createAttributeString(upperLimitKey, String.valueOf(upperLimit)));
        return sb.toString();
    }
}
