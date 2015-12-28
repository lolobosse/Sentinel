package de.tum.in.i22.sentinel.android.app.fragment.policy_editor;

/**
 * Created by laurentmeyer on 23/12/15.
 */
public class RepLim extends EventMatchConditionContainer {

    int amount;
    String unit;
    int lowerLimit, upperLimit;

    private String amountKey = "amount";
    private String unitKey = "unit";
    private String lowerLimitKey = "lowerLimit";
    private String upperLimitKey = "upperLimit";

    public RepLim() {
        super();
        elementXMLName = "repLim";
    }

    @Override
    String createAttributeString() {
        CustomStringBuilder sb = new CustomStringBuilder();
        sb.append(Utils.createAttributeString(amountKey, String.valueOf(amount)))
                .append(Utils.createAttributeString(unitKey, unit))
                .append(Utils.createAttributeString(lowerLimitKey, String.valueOf(lowerLimit)))
                .append(Utils.createAttributeString(upperLimitKey, String.valueOf(upperLimit)));
        return sb.toString();
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

    public int getLowerLimit() {
        return lowerLimit;
    }

    public void setLowerLimit(int lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    public int getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(int upperLimit) {
        this.upperLimit = upperLimit;
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

    public String getLowerLimitKey() {
        return lowerLimitKey;
    }

    public void setLowerLimitKey(String lowerLimitKey) {
        this.lowerLimitKey = lowerLimitKey;
    }

    public String getUpperLimitKey() {
        return upperLimitKey;
    }

    public void setUpperLimitKey(String upperLimitKey) {
        this.upperLimitKey = upperLimitKey;
    }
}
