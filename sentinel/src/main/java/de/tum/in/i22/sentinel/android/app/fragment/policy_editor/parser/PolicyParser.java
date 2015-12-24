package de.tum.in.i22.sentinel.android.app.fragment.policy_editor.parser;

import android.content.Context;
import android.support.annotation.NonNull;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import de.tum.in.i22.sentinel.android.app.R;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.Description;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.EventMatch;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.EventMatchCondition;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.ParamMatch;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.Policy;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.PreventiveMechanism;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.RepLim;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.RepLimCondition;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.SuperCondition;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.TimeStep;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.Trigger;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.Within;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.WithinCondition;

/**
 * Created by laurentmeyer on 23/12/15.
 */
public class PolicyParser {

    public static void c(Context c) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder b = factory.newDocumentBuilder();
            InputStream s = c.getResources().openRawResource(R.raw.policy_appsms_duration4);
            BufferedReader reader = new BufferedReader(new InputStreamReader(s));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            parsePolicy(sb.toString());
        } catch (Exception e) {
        }
    }

    public static Policy parsePolicyFromFile(File f) {
        return null;
    }

    public static Policy parsePolicyFromString(String s) {
        return null;
    }

    public static Policy parsePolicyFromResId(Context c, int resId) {
        return null;
    }

    private static Policy parsePolicy(String s) {
        try {
            Document d = loadXMLFromString(s);
            // TODO: Extract the keys
            Element root =  d.getDocumentElement();
            if (isTheMainElementAPolicy(d)){
                Policy p = new Policy();
                NodeList rootChildren = root.getChildNodes();
                ArrayList<PreventiveMechanism> mechanisms = new ArrayList<>();
                for (int i = 0; i< rootChildren.getLength(); i++){
                    Node child = rootChildren.item(i);
                    if (isPreventiveMechanism(child)){
                        PreventiveMechanism mechanism = new PreventiveMechanism();
                        setMechanismName(child, mechanism);
                        setDescription(child, mechanism);
                        setTimeStep(child, mechanism);
                        setTriggers(child, mechanism);
                        setConditions(child, mechanism);
                        mechanisms.add(mechanism);
                    }

                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
        return null;
    }

    private static void setConditions(Node child, PreventiveMechanism mechanism) {
        ArrayList<SuperCondition> conditions = new ArrayList<>();
        for (int i = 0; i<child.getChildNodes().getLength(); i++) {
            Node subnode = child.getChildNodes().item(i);
            if (subnode.getNodeName().equals("condition")) {
                // The type could change over the iterations, that should not be a problem
                SuperCondition c = new RepLimCondition();
                defineConditionRecursively(subnode, c);
                conditions.add(c);
            }
        }
        mechanism.setConditions(conditions);
    }

    private static void defineConditionRecursively(Node subnode, SuperCondition c) {
        for (int j = 0; j < subnode.getChildNodes().getLength(); j++) {
            Node subNodeDeep2 = subnode.getChildNodes().item(j);
            if (subNodeDeep2.getNodeName().equals("not")) {
                // It is a not, we need to go deeper
                c.setIsNot(true);
                for (int k = 0; k<subNodeDeep2.getChildNodes().getLength(); k++){
                    Node item = subNodeDeep2.getChildNodes().item(k);
                    if (isRepLim(item) || isWithin(item)){
                        createWithinOrRepLim(c, item);
                    }
                }
            } else if (isRepLim(subNodeDeep2) || isWithin(subNodeDeep2)) {
                createWithinOrRepLim(c, subNodeDeep2);
            } else if (subNodeDeep2.getNodeName().equals("eventMatch")) {
                boolean cIsNot = c.isNot();
                c = new EventMatchCondition();
                c.setIsNot(cIsNot);
                ((EventMatchCondition)c).setMatches(createEventMatches(subNodeDeep2));
            }
        }
    }

    @NonNull
    private static void createWithinOrRepLim(SuperCondition c, Node subNodeDeep2) {
        // It is a repLim or a within, we need to go deeper but first let's reset the internal type
        if (isRepLim(subNodeDeep2)){
            // Was already a repLim, don't need to change the type, but we need to add the RepLim objects
            ArrayList<RepLim> alreadyThere = ((RepLimCondition)c).getRepLims() == null ? new ArrayList<RepLim>() : ((RepLimCondition)c).getRepLims();
            RepLim r = new RepLim();
            r.setUpperLimit(Integer.parseInt(((Element) subNodeDeep2).getAttribute("upperLimit")));
            r.setAmount(Integer.parseInt(((Element) subNodeDeep2).getAttribute("amount")));
            r.setUnit(((Element) subNodeDeep2).getAttribute("unit"));
            r.setLowerLimit(Integer.parseInt(((Element) subNodeDeep2).getAttribute("lowerLimit")));
            alreadyThere.add(r);
            r.setMatches(createEventMatches(subNodeDeep2));
            ((RepLimCondition) c).setRepLims(alreadyThere);
        }
        else{
            // If it was a not, we need to keep it
            boolean cIsNot = c.isNot();
            c = new WithinCondition();
            c.setIsNot(cIsNot);
            ArrayList<Within> alreadyThere = ((WithinCondition)c).getWithins() == null ? new ArrayList<Within>() : ((WithinCondition)c).getWithins();
            Within w = new Within();
            w.setAmount(Integer.parseInt(((Element) subNodeDeep2).getAttribute("amount")));
            w.setUnit(((Element) subNodeDeep2).getAttribute("unit"));
            alreadyThere.add(w);
            w.setMatches(createEventMatches(subNodeDeep2));
            ((WithinCondition) c).setWithins(alreadyThere);
        }
    }

    private static ArrayList<EventMatch> createEventMatches(Node subNodeDeep2) {
        // Create the eventMatches
        ArrayList<EventMatch> eventMatches = new ArrayList<>();
        for (int k = 0; k<subNodeDeep2.getChildNodes().getLength(); k++){
            Node subNodeDeep3 = subNodeDeep2.getChildNodes().item(k);
            if (subNodeDeep3.getNodeName().equals("eventMatch")){
                EventMatch e = new EventMatch();
                e.setAction(((Element) subNodeDeep3).getAttribute("action"));
                e.setTryEvent(Boolean.parseBoolean(((Element) subNodeDeep3).getAttribute("tryEvent")));
                e.setParamMatches(getParamMatches(subNodeDeep3.getChildNodes()));
                eventMatches.add(e);
            }
        }
        return eventMatches;
    }

    private static boolean isWithin(Node subNodeDeep2) {
        return subNodeDeep2.getNodeName().equals("within");
    }

    private static boolean isRepLim(Node subNodeDeep2) {
        return subNodeDeep2.getNodeName().equals("repLim");
    }

    private static void setTriggers(Node child, PreventiveMechanism mechanism) {
        ArrayList<Trigger> triggers = new ArrayList<>();
        for (int i = 0; i<child.getChildNodes().getLength(); i++){
            Node subItem = child.getChildNodes().item(i);
            if (subItem.getNodeName().equals("trigger")) {
                Trigger t = new Trigger();
                t.setAction(((Element) subItem).getAttribute("action"));
                t.setTryEvent(Boolean.parseBoolean(((Element) subItem).getAttribute("tryEvent")));
                NodeList triggerChildren = subItem.getChildNodes();
                ArrayList<ParamMatch> paramMatches = getParamMatches(triggerChildren);
                t.setParamMatches(paramMatches);
                triggers.add(t);
            }
        }
        if (!triggers.isEmpty()){
            mechanism.setTriggers(triggers);
        }
    }

    @NonNull
    private static ArrayList<ParamMatch> getParamMatches(NodeList triggerChildren) {
        ArrayList<ParamMatch> paramMatches = new ArrayList<>();
        for (int j = 0; j< triggerChildren.getLength(); j++){
            Node paramMatch = triggerChildren.item(j);
            if (paramMatch.getNodeName().equals("paramMatch")){
                ParamMatch m = new ParamMatch();
                m.setName(((Element) paramMatch).getAttribute("name"));
                m.setValue(((Element) paramMatch).getAttribute("value"));
                paramMatches.add(m);
            }
        }
        return paramMatches;
    }

    private static void setTimeStep(Node child, PreventiveMechanism mechanism) {
        for (int i = 0; i<child.getChildNodes().getLength(); i++) {
            Node subItem = child.getChildNodes().item(i);
            if (subItem.getNodeName().equals("timestep")) {
                TimeStep step = new TimeStep();
                step.setAmount(Integer.parseInt(((Element) subItem).getAttribute("amount")));
                step.setUnit(((Element) subItem).getAttribute("unit"));
                mechanism.setTimestep(step);
            }
        }
    }

    private static void setDescription(Node child, PreventiveMechanism mecanism){
        for (int i = 0; i<child.getChildNodes().getLength(); i++){
            Node subItem = child.getChildNodes().item(i);
            if (subItem.getNodeName().equals("description")){
                Description d = new Description();
                d.setDescription(subItem.getFirstChild().getNodeValue());
                mecanism.setDescription(d);
            }
        }
    }

    private static boolean isPreventiveMechanism(Node child) {
        return child.getNodeName().equals("preventiveMechanism");
    }

    private static void setMechanismName(Node child, PreventiveMechanism mechanism) {
        if (((Element) child).hasAttribute("name")){
            mechanism.setName(((Element) child).getAttribute("name"));
        }
    }

    private static boolean isTheMainElementAPolicy(Document d) {
        return d.getDocumentElement().getNodeName().equals("policy");
    }

    public static Document loadXMLFromString(String xml) throws Exception
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        return builder.parse(is);
    }
}
