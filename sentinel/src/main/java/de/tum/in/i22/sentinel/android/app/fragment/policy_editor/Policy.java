package de.tum.in.i22.sentinel.android.app.fragment.policy_editor;

/**
 * Created by laurentmeyer on 23/12/15.
 */
public class Policy extends XMLElement {

    private final String XMLDecorator = " xmlns=\"http://www22.in.tum.de/enforcementLanguage\"\n" +
            "    xmlns:tns=\"http://www22.in.tum.de/enforcementLanguage\" \n" +
            "    xmlns:a=\"http://www22.in.tum.de/action\"\n" +
            "    xmlns:e=\"http://www22.in.tum.de/event\" \n" +
            "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ";

    boolean deployed;
    String name;
    PreventiveMechanism mechanism;
    boolean standalone;

    public Policy() {
        isContainer = true;
        elementXMLName = "policy";
    }

    @Override
    public String toString() {
        return "<?xml version='1.0' standalone='" + (standalone ? "yes" : "no") + "'?>\n" + super.toString();
    }

    @Override
    String createAttributeString() {
        return XMLDecorator + Utils.createAttributeString("name", name);
    }

    @Override
    String createValueString() {
        if (mechanism != null) {
            return mechanism.toString();
        }
        return "";
    }


    public boolean isDeployed() {
        return deployed;
    }

    public void setDeployed(boolean deployed) {
        this.deployed = deployed;
    }

    public PreventiveMechanism getMechanism() {
        return mechanism;
    }

    public void setMechanism(PreventiveMechanism mechanism) {
        this.mechanism = mechanism;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isStandalone() {
        return standalone;
    }

    public void setStandalone(boolean standalone) {
        this.standalone = standalone;
    }

}
