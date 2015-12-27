package de.tum.in.i22.sentinel.android.app.fragment.policy_editor.customViews;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.commons.lang3.text.WordUtils;
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

import de.tum.in.i22.sentinel.android.app.R;

/**
 * Created by laurentmeyer on 26/12/15.
 */
public class ActionChooser extends Spinner {

    private static final int BASED_ON = R.raw.event_information;

    private Context c;

    ArrayList<Event> events;

    OnItemSelectedListener listener;

    public ActionChooser(Context context) {
        super(context);
        this.c = context;
        init();
    }

    public ActionChooser(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.c = context;
        init();
    }

    private void init() {
        try {
            InputStream s = c.getResources().openRawResource(BASED_ON);
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
                        Event e = new Event();
                        e.methodSignature = event.getAttributes().getNamedItem("methodSignature").getNodeValue();
                        e.name = getNodeByTag(event, "eventname").getAttributes().getNamedItem("name").getNodeValue();
                        e.isBefore = getNodeByTag(event, "instrumentationpos").getFirstChild().getNodeValue().equals("before") ? true : false;
                        if (getNodeByTag(event, "data") == null) {
                            e.data = new ArrayList<>();
                        } else {
                            ArrayList<Param> params = new ArrayList<>();
                            NodeList allParams = getNodeByTag(event, "data").getChildNodes();
                            for (int j = 0; j < allParams.getLength(); j++) {
                                Node param = allParams.item(j);
                                if (param.getNodeName().equals("param")) {
                                    Param p = new Param();
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
            }
            Log.d("ActionChooser", "Set adapter");
            Log.d("ActionChooser", "events.size():" + events.size());
            BaseAdapter a = new CustomAdapter();
            a.notifyDataSetChanged();
            setAdapter(a);
            OnItemSelectedListener l = new OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.d("ActionChooser", "i:" + i);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    Log.d("ActionChooser", "nothing");
                }
            };
            setOnItemSelectedListener(l);
        }catch (Exception e){

        }
    }

    @Override
    public void setSelection(int position) {
        super.setSelection(position);
        Log.d("ActionChooser", "set selection");
    }

    private Node getNodeByTag(Node superNode, String tag) {
        for (int i = 0; i < superNode.getChildNodes().getLength(); i++) {
            if (superNode.getChildNodes().item(i).getNodeName().equals(tag)) {
                return superNode.getChildNodes().item(i);
            }
        }
        return null;
    }

    private class Event {

        String methodSignature;
        String name;
        boolean isBefore;
        ArrayList<Param> data;
    }

    private class Param {
        int pos;
        String name;
    }

    private class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return events.size();
        }

        @Override
        public Object getItem(int i) {
            return events.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View row = inflate(c, R.layout.row_action_chooser, null);
            TextView tv = (TextView) row.findViewById(R.id.title);
            TextView methodName = (TextView) row.findViewById(R.id.javaM);
            TextView cb = (TextView) row.findViewById(R.id.checkbox);
            LinearLayout ll = (LinearLayout) row.findViewById(R.id.dataContainer);
            tv.setText(WordUtils.capitalize(events.get(i).name));
            methodName.setText(events.get(i).methodSignature);
            cb.setText(events.get(i).isBefore?"true":"false");
            for (Param p : events.get(i).data) {
                TextView pt = (TextView) row.findViewById(R.id.parameterTitle);
                pt.setVisibility(VISIBLE);
                TextView ptv = new TextView(c);
                ptv.setText(p.name + " (pos: " + p.pos + ")");
                ptv.setTextSize(10);
                ll.addView(ptv);
            }
            return row;
        }
    }

}
