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

import java.util.ArrayList;

import de.tum.in.i22.sentinel.android.app.R;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.ParseEventInformationTask;

/**
 * Created by laurentmeyer on 26/12/15.
 */
public class ActionChooser extends Spinner {

    private static final int BASED_ON = R.raw.event_information;

    private Context c;

    ArrayList<Event> events;

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
        events = ParseEventInformationTask.ResultGetter.getInstance().getResults();
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

    }

    @Override
    public void setSelection(int position) {
        super.setSelection(position);
        Log.d("ActionChooser", "set selection");
    }

    public static class Event {

        public String methodSignature;
        public String name;
        public boolean isBefore;
        public ArrayList<Param> data;
    }

    public static class Param {
        public int pos;
        public String name;
    }

    private class CustomAdapter extends BaseAdapter {

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
            cb.setText(events.get(i).isBefore ? "true" : "false");
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
