package de.tum.in.i22.sentinel.android.app.fragment.policy_editor;

/**
 * Created by laurentmeyer on 23/12/15.
 */
public abstract class XMLElement {

    protected String elementXMLName;
    protected boolean isContainer;

    /**
     * Basically it just calls the attribute method in order that, if properly overridden, everything works :)
     * @return The toString of the element for saving it in XML
     */
    @Override
    public String toString() {
        return !isContainer ? "<" + elementXMLName + " " + createAttributeString() + "/>" : "<" + elementXMLName + " " + createAttributeString() + ">" + createValueString() + "</" + elementXMLName + ">";
    }

    abstract String createAttributeString();

    abstract String createValueString();
}
