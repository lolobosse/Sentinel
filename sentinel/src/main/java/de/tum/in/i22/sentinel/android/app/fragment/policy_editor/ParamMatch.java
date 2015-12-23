package de.tum.in.i22.sentinel.android.app.fragment.policy_editor;

/**
 * Created by laurentmeyer on 23/12/15.
 */
public class ParamMatch extends XMLElement {

    String name;
    String value;
    String nameKey = "name";
    String valueKey = "value";

    ParamMatch(){
        isContainer = false;
        elementXMLName = "paramMatch";
    }

    @Override
    String createAttributeString() {
        StringBuilder b = new StringBuilder();
        b.append(Utils.createAttributeString(nameKey, name));
        b.append(Utils.createAttributeString(valueKey, value));
        return b.toString();
    }

    @Override
    String createValueString() {
        return "";
    }
}
