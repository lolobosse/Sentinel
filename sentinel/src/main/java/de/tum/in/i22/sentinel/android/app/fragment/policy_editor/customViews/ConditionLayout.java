package de.tum.in.i22.sentinel.android.app.fragment.policy_editor.customViews;

import android.content.Context;
import android.util.Log;
import android.widget.LinearLayout;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        Object o1 = defineTopCondition(o);
        defineCondition(o);
        Class c = o1.getClass();
        Log.d("ConditionLayout", "c:" + c);
    }

    private Object defineTopCondition(Operators o) {
        if (o.ifAlways()) {
            return o.getAlways();
        }
        if (o.ifNot()) {
            return o.getNot();
        }
        if (o.ifAnd()) {
            return o.getAnd();
        }
        if (o.ifRepLim()) {
            return o.getRepLim();
        }
        if (o.ifWithin()) {
            return o.getWithin();
        }
        if (o.ifBefore()) {
            return o.getBefore();
        }
        if (o.ifDuring()) {
            return o.getDuring();
        }
        if (o.ifEval()) {
            return o.getEval();
        }
        if (o.ifConditionParamMatch()) {
            return o.getConditionParamMatch();
        }
        if (o.ifEventMatch()) {
            return o.getEventMatch();
        }
        if (o.ifFalse()) {
            return o.getFalse();
        }
        if (o.ifImplies()) {
            return o.getImplies();
        }
        if (o.ifIsMaxIn()) {
            return o.getIsMaxIn();
        }
        if (o.ifOr()) {
            return o.getOr();
        }
        if (o.ifOccurMinEvent()) {
            return o.getOccurMinEvent();
        }
        if (o.ifRepMax()) {
            return o.getRepMax();
        }
        if (o.ifRepSince()) {
            return o.getRepSince();
        }
        if (o.ifSince()) {
            return o.getSince();
        }
        if (o.ifStateBasedFormula()) {
            return o.ifStateBasedFormula();
        }
        if (o.ifEval()) {
            return o.getEval();
        } else {
            return null;
        }
    }

    private ArrayList<HashMap<String, String>> maps = new ArrayList<>();

    private Class defineCondition(Object o) {
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
                        HashMap<String, String> map = new HashMap<>();
                        for (Method m2 : intermediateLevel.getMethods()) {
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
        return null;
    }

//    ConditionType sc;
//    PolicyChanger pc;
//    PolicyType p;
//
//    LinearLayout subConditionAttrs;
//
//    public ConditionLayout(Context c, PolicyType p, PolicyChanger policyChanger, ConditionType superCondition) {
//        super(c);
//        this.pc = policyChanger;
//        this.sc = superCondition;
//        this.p = p;
//        init();
//        sc.getConditionType().getNot().getNotType().getNot().getNotType()
//    }
//
//    private void init() {
//        inflate(getContext(), R.layout.condition_layout, this);
//        final Spinner spinner = (Spinner) findViewById(R.id.spinner_type_action);
//        CheckBox not = (CheckBox) findViewById(R.id.notCheckbox);
//        not.setChecked(sc.getConditionType().ifNot());
//        if (isNothing()) {
//            spinner.setSelection(0);
//        } else if (isRepLim()) {
//            spinner.setSelection(1);
//        } else if (isWithin()) {
//            spinner.setSelection(2);
//        }
//
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                switch (i) {
//                    case 0:
//                        // If it change the type of object
//                        if (!isNothing()) {
//                            sc.getConditionType().clearOperatorsSelect();
//                            sc.getConditionType().setRepLim(null);
//                            sc.getConditionType().setWithin(null);
//                        }
//                        break;
//                    case 1:
//                        if (isRepLim()) {
//                            sc.getConditionType().clearOperatorsSelect();
//                            sc.getConditionType().setRepLim(new RepLimType());
//                        }
//                        break;
//                    case 2:
//                        if (isWithin()) {
//                            sc.getConditionType().clearOperatorsSelect();
//                            sc.getConditionType().setWithin(new WithinType());
//                        }
//                        break;
//                }
//                createSubAttrs();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//        subConditionAttrs = (LinearLayout) findViewById(R.id.subConditionAttrs);
//        createSubAttrs();
//
//
//    }
//
//    private boolean isWithin() {
//        return !sc.getConditionType().ifWithin() || (!(sc.getConditionType().getNot().getNotType() == null) && !sc.getConditionType().getNot().getNotType().ifRepLim());
//    }
//
//    private boolean isRepLim() {
//        return !sc.getConditionType().ifRepLim() || (!(sc.getConditionType().getNot().getNotType() == null) && !sc.getConditionType().getNot().getNotType().ifRepLim());
//    }
//
//    private boolean isNothing() {
//        return !sc.getConditionType().ifRepLim() && !sc.getConditionType().ifWithin() &&
//                (!(sc.getConditionType().getNot() == null) && (!(sc.getConditionType().getNot().getNotType().ifRepLim())
//                        && !(sc.getConditionType().getNot().getNotType().ifWithin())));
//    }
//
//    private void createSubAttrs() {
//        subConditionAttrs.removeAllViews();
//        HashMap<String, String> map = new HashMap<>();
//        if (sc.getConditionType().ifRepLim()) {
//            // TODO: Adapt it in relation of the emplacement of the condition
//            map.put("Amount", String.valueOf(sc.getConditionType().getRepLim().getTimeAmountAttributeGroup().getAmount()));
//            map.put("Unit", String.valueOf(sc.getConditionType().getRepLim().getTimeAmountAttributeGroup().getUnit().name()));
//            map.put("Lower Limit", String.valueOf(sc.getConditionType().getRepLim().getLowerLimit()));
//            map.put("Upper Limit", String.valueOf(sc.getConditionType().getRepLim().getUpperLimit()));
//        } else if (sc.getConditionType().ifWithin()) {
//            map.put("Amount", String.valueOf(sc.getConditionType().getWithin().getTimeAmountAttributeGroup().getAmount()));
//            map.put("Unit", String.valueOf(sc.getConditionType().getWithin().getTimeAmountAttributeGroup().getUnit().name()));
//        }
//
//        for (String key : map.keySet()) {
//            if (!key.equals("unit")) {
//                View v = inflate(getContext(), R.layout.key_value_layout_tv, null);
//                TextView tv = (TextView) v.findViewById(R.id.key);
//                tv.setText(key);
//                TextView tv2 = (TextView) v.findViewById(R.id.value);
//                tv2.setText(map.get(key));
//                subConditionAttrs.addView(v);
//            } else {
//                View v = inflate(getContext(), R.layout.key_spinner_layout, null);
//                TextView tv = (TextView) v.findViewById(R.id.key);
//                tv.setText(key);
//                int pos = 0;
//                Spinner s = (Spinner) v.findViewById(R.id.unitSpinner);
//                for (int i = 0; i < s.getAdapter().getCount(); i++) {
//                    if (s.getAdapter().getItem(i).equals(map.get(key))) {
//                        pos = i;
//                    }
//                }
//                s.setSelection(pos);
//                subConditionAttrs.addView(v);
//            }
//        }
//    }
//

}
