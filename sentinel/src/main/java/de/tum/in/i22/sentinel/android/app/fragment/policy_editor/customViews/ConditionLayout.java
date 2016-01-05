package de.tum.in.i22.sentinel.android.app.fragment.policy_editor.customViews;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.tum.in.i22.sentinel.android.app.R;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.interfaces.PolicyChanger;
import de.tum.in.www22.enforcementlanguage.ConditionType;
import de.tum.in.www22.enforcementlanguage.Operators;
import de.tum.in.www22.enforcementlanguage.PolicyType;

/**
 * Created by laurentmeyer on 27/12/15.
 */
public class ConditionLayout extends LinearLayout {

    PolicyType p;
    PolicyChanger pc;


    public ConditionLayout(Context context, PolicyType p, PolicyChanger pc) {
        super(context);
        this.p = p;
        this.pc = pc;
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        ConditionType condition = p.getChoices().get(0).getPreventiveMechanism().getCondition();
        Operators o = condition.getConditionType();
        ArrayList<NamedMap> named = defineCondition(o);
        // Because of the recursion the element are not in order.
        Collections.reverse(named);
        for(NamedMap m : named){
            LinearLayout ll = (LinearLayout) inflate(getContext(), R.layout.condition_level_layout, null);
            TextView tv = (TextView) ll.findViewById(R.id.nameOfCondition);
            tv.setText(m.getName());
            addView(ll);
            for (String key : m.keySet()) {
                ViewGroup vg = (ViewGroup) inflate(this.getContext(), R.layout.key_value_layout_tv, null);
                TextView keyTV = (TextView) vg.findViewById(R.id.key);
                TextView valueTV = (TextView) vg.findViewById(R.id.value);
                keyTV.setText(key);
                valueTV.setText(m.get(key));
                addView(vg);
            }
        }
    }

    // Need to extract it from the method because the method is recursive
    ArrayList<NamedMap> maps = new ArrayList<>();

    private ArrayList<NamedMap> defineCondition(Object o) {
        try {
            Class c = o.getClass();
            Log.d("ConditionLayout", "c:" + c);
            Pattern p = Pattern.compile("(class de.tum.in.www22.enforcementlanguage.)([\\D]+Type)");
            for (Method m : c.getMethods()) {
                Matcher matcher = p.matcher(m.getReturnType().toString());
                String type = null;
                if (matcher.matches()) {
                    type = matcher.group(2);
                }
                if (type != null) {
                    Object typeFromGet = m.invoke(o);
                    if (typeFromGet != null) {
                        // We need to go a layer down
                        Class intermediateLevel = typeFromGet.getClass();
                        Object nextLevel = null;
                        for (Method m1 : intermediateLevel.getMethods()) {
                            if (m1.getReturnType().getSimpleName().equals("Operators")) {
                                nextLevel = m1.invoke(typeFromGet);
                            }
                        }
                        // Then next layer
                        if (nextLevel != null)
                            defineCondition(nextLevel);
                        // We reached the last layer so we can start to analyse the other getter and setter
                        String regexToExtractParamLabel = "(get)([\\D]+)";
                        Pattern p1 = Pattern.compile(regexToExtractParamLabel);
                        NamedMap map = new NamedMap();
                        for (Method m2 : intermediateLevel.getMethods()) {
                            map.setName(type.replace("Operator", "").replace("Type", ""));
                            if (!m2.getName().matches(regexToExtractParamLabel)) continue;
                            // To get the BlaBla of 'getBlaBla'
                            Matcher matcher1 = p1.matcher(m2.getName());
                            String label = matcher1.matches() ? matcher1.group(2) : null;
                            if (label == null) continue;
                            if (m2.getReturnType().equals(boolean.class)) {
                                map.put(label, String.valueOf(m2.invoke(typeFromGet)));
                            }
                            if (m2.getReturnType().equals(long.class)) {
                                map.put(label, String.valueOf(m2.invoke(typeFromGet)));
                            }
                            if (m2.getReturnType().equals(int.class)) {
                                map.put(label, String.valueOf(m2.invoke(typeFromGet)));
                            }
                            if (m2.getReturnType().equals(String.class)) {
                                map.put(label, String.valueOf(m2.invoke(typeFromGet)));
                            }
                        }
                        maps.add(map);
                    }
                    Log.d("ConditionLayout", "typeFromGet:" + typeFromGet);
                }
            }
        } catch (Exception e) {
            Log.d("ConditionLayout", "e:" + e);
        }
        return maps;
    }

    private class NamedMap extends HashMap<String, String>{
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        private String name;
    }

}
