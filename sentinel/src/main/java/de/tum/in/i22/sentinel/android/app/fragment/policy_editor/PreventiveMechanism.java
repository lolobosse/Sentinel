package de.tum.in.i22.sentinel.android.app.fragment.policy_editor;

import java.util.ArrayList;

/**
 * Created by laurentmeyer on 23/12/15.
 */
public class PreventiveMechanism {

    String name, description;
    TimeStep timestep;
    ArrayList<Trigger> triggers;
    ArrayList<Condition> conditions;
    AuthorizationAction authorizationAction;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AuthorizationAction getAuthorizationAction() {
        return authorizationAction;
    }

    public void setAuthorizationAction(AuthorizationAction authorizationAction) {
        this.authorizationAction = authorizationAction;
    }

    public ArrayList<Condition> getConditions() {
        return conditions;
    }

    public void setConditions(ArrayList<Condition> conditions) {
        this.conditions = conditions;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TimeStep getTimestep() {
        return timestep;
    }

    public void setTimestep(TimeStep timestep) {
        this.timestep = timestep;
    }

    public ArrayList<Trigger> getTriggers() {
        return triggers;
    }

    public void setTriggers(ArrayList<Trigger> triggers) {
        this.triggers = triggers;
    }
}
