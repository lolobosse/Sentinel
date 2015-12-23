package de.tum.in.i22.sentinel.android.app.fragment.policy_editor;

/**
 * Created by laurentmeyer on 23/12/15.
 */
public class ParamMatch extends XMLElement {

    String name;
    private String nameKey = "name";
    String value;
    private String valueKey = "value";

    ParamMatch(){
        isContainer = false;
        elementXMLName = "paramMatch";
    }

    @Override
    String createAttributeString() {
        CustomStringBuilder b = new CustomStringBuilder();
        b.append(Utils.createAttributeString(nameKey, name));
        b.append(Utils.createAttributeString(valueKey, value));
        return b.toString();
    }

    @Override
    String createValueString() {
        return "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
