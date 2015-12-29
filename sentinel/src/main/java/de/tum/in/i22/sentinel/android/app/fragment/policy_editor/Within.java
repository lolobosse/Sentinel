package de.tum.in.i22.sentinel.android.app.fragment.policy_editor;

/**
 * Created by laurentmeyer on 23/12/15.
 */
public class Within extends EventMatchConditionContainer{

    int amount;
    private String amountKey = "amount";

    String unit;
    private String unitKey = "unit";

    public Within(){
        super();
        elementXMLName = "within";
    }

    @Override
    String createAttributeString() {
        CustomStringBuilder b = new CustomStringBuilder();
        b.append(Utils.createAttributeString(amountKey, String.valueOf(amount)))
                .append(Utils.createAttributeString(unitKey, unit));
        return b.toString();
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getAmountKey() {
        return amountKey;
    }

    public void setAmountKey(String amountKey) {
        this.amountKey = amountKey;
    }

    public String getUnitKey() {
        return unitKey;
    }

    public void setUnitKey(String unitKey) {
        this.unitKey = unitKey;
    }
}
