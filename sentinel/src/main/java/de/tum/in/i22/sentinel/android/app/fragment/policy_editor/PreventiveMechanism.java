package de.tum.in.i22.sentinel.android.app.fragment.policy_editor;

import android.support.annotation.NonNull;

import java.util.ArrayList;

/**
 * Created by laurentmeyer on 23/12/15.
 */
public class PreventiveMechanism extends XMLElement {

    String name;
    TimeStep timestep;
    Description description;
    ArrayList<Trigger> triggers;
    ArrayList<SuperCondition> conditions;
    AuthorizationAction authorizationAction;

    public PreventiveMechanism() {
        isContainer = true;
        elementXMLName = "preventiveMechanism";
    }

    @Override
    String createAttributeString() {
        return Utils.createAttributeString("name", name);
    }

    @Override
    String createValueString() {
        CustomStringBuilder builder = new CustomStringBuilder();
        builder.append(description);
        builder.append("\n");
        builder.append(timestep);
        builder.append("\n");
        if (triggers!=null) {
            for (Trigger t : triggers) {
                builder.append(t + "\n");
            }
        }
        if (conditions != null) {
            for (SuperCondition c : conditions) {
                builder.append(c + "\n");
            }
        }
        builder.append(authorizationAction)
                .append("\n");
        return builder.toString();
    }

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }


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

    public ArrayList<SuperCondition> getConditions() {
        return conditions;
    }

    public void setConditions(ArrayList<SuperCondition> conditions) {
        this.conditions = conditions;
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
