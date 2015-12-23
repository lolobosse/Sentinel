package de.tum.in.i22.sentinel.android.app.fragment.policy_editor;

/**
 * Created by laurentmeyer on 23/12/15.
 */
public class Within extends EventMatchConditionContainer{

    int amount;
    String unit;

    String unitKey = "unit";
    String amountKey = "amount";

    Within(){
        super();
        elementXMLName = "within";
    }

    @Override
    String createAttributeString() {
        StringBuilder b = new StringBuilder();
        b.append(Utils.createAttributeString(amountKey, String.valueOf(amount)))
                .append(Utils.createAttributeString(unitKey, unit));
        return b.toString();
    }
}
