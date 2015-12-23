package de.tum.in.i22.sentinel.android.app.fragment.policy_editor;

import java.util.ArrayList;

/**
 * Created by laurentmeyer on 23/12/15.
 */
public abstract class EventMatchConditionContainer extends XMLElement {

    ArrayList<EventMatch> matches;

    EventMatchConditionContainer(){
        isContainer = true;
    }


    @Override
    String createValueString() {
        StringBuilder sb = new StringBuilder();
        for (EventMatch match : matches){
            sb.append(match+"\n");
        }
        return sb.toString();
    }
}
