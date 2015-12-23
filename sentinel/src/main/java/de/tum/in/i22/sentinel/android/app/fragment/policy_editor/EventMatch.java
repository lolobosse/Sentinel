package de.tum.in.i22.sentinel.android.app.fragment.policy_editor;

import java.util.ArrayList;

/**
 * Created by laurentmeyer on 23/12/15.
 */
public class EventMatch extends XMLElement{

    String action;
    boolean tryEvent;

    ArrayList<ParamMatch> paramMatches;

    String actionKey = "action";
    String tryEventKey = "tryEvent";

    public EventMatch() {
        elementXMLName = "eventMatch";
        isContainer = true;
    }

    @Override
    String createAttributeString() {
        StringBuilder builder = new StringBuilder();
        builder.append(Utils.createAttributeString(actionKey, action))
                .append(Utils.createAttributeString(tryEventKey, String.valueOf(tryEvent)));
        return builder.toString();
    }

    @Override
    String createValueString() {
        StringBuilder b = new StringBuilder();
        for (ParamMatch match : paramMatches){
            b.append(paramMatches+"\n");
        }
        return b.toString();
    }
}
