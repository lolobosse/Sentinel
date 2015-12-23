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
        StringBuilder sb = new StringBuilder();
        for (Within w : withins) {
            sb.append(w + "\n");
        }
        return sb.toString();
    }
}
