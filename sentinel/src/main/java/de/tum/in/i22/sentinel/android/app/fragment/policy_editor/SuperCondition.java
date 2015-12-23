package de.tum.in.i22.sentinel.android.app.fragment.policy_editor;

/**
 * Created by laurentmeyer on 23/12/15.
 */
public abstract class SuperCondition extends XMLElement {

    boolean isNot;

    public SuperCondition() {
        elementXMLName = "condition";
        isContainer = true;
    }

    @Override
    public String toString() {
        if (!isNot) {
            return "<" + elementXMLName + " " + createAttributeString() + ">" + createValueString() + "</" + elementXMLName + ">";
        } else {
            return "<" + elementXMLName + " " + createAttributeString() + ">" + "<not>" + createValueString() + "</not>" + "</" + elementXMLName + ">";
        }
    }

    public boolean isNot() {
        return isNot;
    }

    public void setIsNot(boolean isNot) {
        this.isNot = isNot;
    }
}
