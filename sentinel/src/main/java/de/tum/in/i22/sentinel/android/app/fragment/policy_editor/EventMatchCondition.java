package de.tum.in.i22.sentinel.android.app.fragment.policy_editor;

import java.util.ArrayList;

/**
 * Created by laurentmeyer on 23/12/15.
 */
public class EventMatchCondition extends SuperCondition {

    ArrayList<EventMatch> matches;

    @Override
    String createAttributeString() {
        return "";
    }

    @Override
    String createValueString() {
        StringBuilder sb = new StringBuilder();
        for (EventMatch match : matches){
            sb.append(match+"\n");
        }
        return sb.toString();
    }

    public ArrayList<EventMatch> getMatches() {
        return matches;
    }

    public void setMatches(ArrayList<EventMatch> matches) {
        this.matches = matches;
    }
}
