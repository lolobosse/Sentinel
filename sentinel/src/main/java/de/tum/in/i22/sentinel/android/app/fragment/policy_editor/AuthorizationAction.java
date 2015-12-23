package de.tum.in.i22.sentinel.android.app.fragment.policy_editor;

/**
 * Created by laurentmeyer on 23/12/15.
 */
public class AuthorizationAction extends XMLElement {

    String name;
    String nameKey = "name";

    Behavior b;

    public AuthorizationAction() {
        isContainer = true;
        elementXMLName = "authorizationAction";
    }

    @Override
    String createAttributeString() {
        return Utils.createAttributeString(nameKey, name);
    }

    @Override
    String createValueString() {
        return new Behavior().toString();
    }
}
