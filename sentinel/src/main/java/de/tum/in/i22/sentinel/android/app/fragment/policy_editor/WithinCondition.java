package de.tum.in.i22.sentinel.android.app.fragment.policy_editor;

import java.util.ArrayList;

/**
 * Created by laurentmeyer on 23/12/15.
 */
public class WithinCondition extends SuperCondition {

    ArrayList<Within> withins;

    @Override
    String createAttributeString() {
        return "";
    }

    @Override
    String createValueString() {
        CustomStringBuilder sb = new CustomStringBuilder();
        for (Within w : withins) {
            sb.append(w + "\n");
        }
        return sb.toString();
    }

    public ArrayList<Within> getWithins() {
        return withins;
    }

    public void setWithins(ArrayList<Within> withins) {
        this.withins = withins;
    }
}