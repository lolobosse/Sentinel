package de.tum.in.i22.sentinel.android.app.fragment.policy_editor.customViews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import de.tum.in.i22.sentinel.android.app.R;

/**
 * Created by laurentmeyer on 26/12/15.
 */
public class TypeConditionChooser extends Spinner{

    Context c;
    ArrayList<AdapterObject> objects;

    public TypeConditionChooser(Context context) {
        super(context);
        this.c = context;
        init();
    }

    public TypeConditionChooser(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.c = context;
        init();
    }

    private void init() {
        objects = new ArrayList<>();
        AdapterObject ao1 = new AdapterObject();
        ao1.setTitle("None");
        ao1.setExplanation("No within nor repLim");
        objects.add(ao1);
        AdapterObject ao2 = new AdapterObject();
        ao2.setTitle("RepLim");
        ao2.setExplanation("It limits the number of repetitions of the same action");
        objects.add(ao2);
        AdapterObject ao3 = new AdapterObject();
        ao3.setTitle("Within");
        ao3.setExplanation("It limits the number of same actions in a given timeframe");
        objects.add(ao3);
        setAdapter(new TypeConditionAdapter());

    }

    private class TypeConditionAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return objects.size();
        }

        @Override
        public Object getItem(int i) {
            return objects.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null){
                view = inflate(c, R.layout.type_condition_layout, null);
            }
            TextView tv1 = (TextView) view.findViewById(R.id.typeTitle);
            TextView tv2 = (TextView) view.findViewById(R.id.typeSubtitle);
            tv1.setText(((AdapterObject)getItem(i)).getTitle());
            tv2.setText(((AdapterObject)getItem(i)).getExplanation());
            return view;
        }
    }

    private class AdapterObject{

        String title;
        String explanation;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getExplanation() {
            return explanation;
        }

        public void setExplanation(String explanation) {
            this.explanation = explanation;
        }
    }
}
