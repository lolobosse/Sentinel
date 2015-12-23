package de.tum.in.i22.sentinel.android.app.fragment.policy_editor.parser;

import android.content.Context;

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
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.ParamMatch;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.Policy;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.PreventiveMechanism;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.TimeStep;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.Trigger;

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
//            Document document = b.parse(s);
//            Element e = document.getDocumentElement();
//            Log.d("PolicyParser", e.getNodeName());
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
                        mechanisms.add(mechanism);
                    }

                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
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
                ArrayList<ParamMatch> paramMatches = new ArrayList<>();
                for (int j = 0; j< triggerChildren.getLength(); j++){
                    Node paramMatch = triggerChildren.item(j);
                    ParamMatch m = new ParamMatch();
                    m.setName(((Element) paramMatch).getAttribute("name"));
                    m.setValue(((Element) paramMatch).getAttribute("value"));
                    paramMatches.add(m);
                }
                t.setParamMatches(paramMatches);
                triggers.add(t);
            }
        }
        if (!triggers.isEmpty()){
            mechanism.setTriggers(triggers);
        }
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
