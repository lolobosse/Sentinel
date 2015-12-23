package de.tum.in.i22.sentinel.android.app.fragment.policy_editor;

/**
 * Created by laurentmeyer on 23/12/15.
 */
public class Policy {

    boolean deployed;
    String name;
    PreventiveMechanism mechanism;
    boolean standalone;

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
