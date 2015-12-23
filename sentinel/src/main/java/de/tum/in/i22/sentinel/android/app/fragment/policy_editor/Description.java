package de.tum.in.i22.sentinel.android.app.fragment.policy_editor;

/**
 * Created by laurentmeyer on 23/12/15.
 */
public class Description extends XMLElement {

    String description;

    public Description() {
        elementXMLName = "description";
        isContainer = false;
    }

    @Override
    String createAttributeString() {
        return "";
    }

    @Override
    String createValueString() {
        return description;
    }
}
