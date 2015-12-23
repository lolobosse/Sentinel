package de.tum.in.i22.sentinel.android.app.fragment.policy_editor;

/**
 * Created by laurentmeyer on 23/12/15.
 */
public class TimeStep extends XMLElement{

    int amount;
    String unit;

    private String amountKey = "amount";
    private String unitKey = "unit";

    TimeStep(){
        isContainer = false;
        elementXMLName = "timestep";
    }

    @Override
    public String toString() {
        return super.toString().replace("%attrs", createAttributeString());
    }

    @Override
    String createAttributeString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Utils.createAttributeString(unitKey, unit));
        sb.append(Utils.createAttributeString(amountKey, String.valueOf(amount)));
        return sb.toString();
    }

    @Override
    String createValueString() {
        return "";
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
