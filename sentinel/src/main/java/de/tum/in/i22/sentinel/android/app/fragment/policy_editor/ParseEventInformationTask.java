package de.tum.in.i22.sentinel.android.app.fragment.policy_editor;

import android.content.Context;
import android.os.AsyncTask;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.customViews.ActionChooser;

/**
 * Created by laurentmeyer on 28/12/15.
 */
public class ParseEventInformationTask extends AsyncTask<Void, Void, Void>{

    Context context;
    int basedOn;

    public ParseEventInformationTask(Context c, int based_on){
        this.context = c;
        this.basedOn = based_on;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            ArrayList<ActionChooser.Event> events;
            InputStream s = context.getResources().openRawResource(this.basedOn);
            BufferedReader reader = new BufferedReader(new InputStreamReader(s));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(sb.toString()));
            Document d = builder.parse(is);
            events = new ArrayList<>();
            if (d.getFirstChild() != null && d.getFirstChild().getNodeName().equals("events")) {
                for (int i = 0; i < d.getFirstChild().getChildNodes().getLength(); i++) {
                    Node event = d.getFirstChild().getChildNodes().item(i);
                    if (event.getAttributes() != null) {
                        ActionChooser.Event e = new ActionChooser.Event();
                        e.methodSignature = event.getAttributes().getNamedItem("methodSignature").getNodeValue();
                        e.name = getNodeByTag(event, "eventname").getAttributes().getNamedItem("name").getNodeValue();
                        e.isBefore = getNodeByTag(event, "instrumentationpos").getFirstChild().getNodeValue().equals("before") ? true : false;
                        if (getNodeByTag(event, "data") == null) {
                            e.data = new ArrayList<>();
                        } else {
                            ArrayList<ActionChooser.Param> params = new ArrayList<>();
                            NodeList allParams = getNodeByTag(event, "data").getChildNodes();
                            for (int j = 0; j < allParams.getLength(); j++) {
                                Node param = allParams.item(j);
                                if (param.getNodeName().equals("param")) {
                                    ActionChooser.Param p = new ActionChooser.Param();
                                    p.name = param.getAttributes().getNamedItem("name").getNodeValue();
                                    p.pos = Integer.parseInt(param.getAttributes().getNamedItem("pos").getNodeValue());
                                    params.add(p);
                                }
                            }
                            e.data = params;
                        }
                        events.add(e);
                    }
                }
                ResultGetter.getInstance().setResult(events);
            }
        }catch(Exception e){
            ResultGetter.getInstance().setResult(null);
        }
        return null;
    }


    private Node getNodeByTag(Node superNode, String tag) {
        for (int i = 0; i < superNode.getChildNodes().getLength(); i++) {
            if (superNode.getChildNodes().item(i).getNodeName().equals(tag)) {
                return superNode.getChildNodes().item(i);
            }
        }
        return null;
    }

    /**
     * It will be unsafe because we assume the device goes faster to parse the XML than to arrive to the given view, should never fail, still unsafe.
     */
    public static class ResultGetter{

        private static ResultGetter instance;

        private ArrayList<ActionChooser.Event> results;

        public static ResultGetter getInstance(){
            if (instance == null){
                instance = new ResultGetter();
            }
            return instance;
        }

        private void setResult(ArrayList<ActionChooser.Event> events){
            results = events;
        }

        public ArrayList<ActionChooser.Event> getResults(){
            return results;
        }
    }
}
