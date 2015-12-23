package de.tum.in.i22.sentinel.android.app.fragment.policy_editor;

import java.util.ArrayList;

/**
 * Created by laurentmeyer on 23/12/15.
 */
public class Trigger extends XMLElement{

    String action;
    private String actionKey = "action";

    boolean tryEvent;
    private String tryEventKey = "tryEvent";

    ArrayList<ParamMatch> paramMatches;

    public Trigger() {
        isContainer = true;
        elementXMLName = "trigger";
    }

    @Override
    String createAttributeString() {
        CustomStringBuilder builder = new CustomStringBuilder();
        builder.append(Utils.createAttributeString(actionKey, action))
                .append(Utils.createAttributeString(tryEventKey, String.valueOf(tryEvent)));
        return builder.toString();
    }

    @Override
    String createValueString() {
        CustomStringBuilder b = new CustomStringBuilder();
        for (ParamMatch match : paramMatches){
            b.append(match+"\n");
        }
        return b.toString();
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public boolean isTryEvent() {
        return tryEvent;
    }

    public void setTryEvent(boolean tryEvent) {
        this.tryEvent = tryEvent;
    }

    public ArrayList<ParamMatch> getParamMatches() {
        return paramMatches;
    }

    public void setParamMatches(ArrayList<ParamMatch> paramMatches) {
        this.paramMatches = paramMatches;
    }
}
