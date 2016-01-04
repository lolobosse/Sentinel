package de.tum.in.i22.sentinel.android.app.fragment.policy_editor.customViews;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.HashMap;

import de.tum.in.i22.sentinel.android.app.R;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.interfaces.PolicyChanger;
import de.tum.in.www22.enforcementlanguage.ConditionType;
import de.tum.in.www22.enforcementlanguage.PolicyType;
import de.tum.in.www22.enforcementlanguage.RepLimType;
import de.tum.in.www22.enforcementlanguage.WithinType;

/**
 * Created by laurentmeyer on 27/12/15.
 */
public class ConditionLayout extends RelativeLayout {

    ConditionType sc;
    PolicyChanger pc;
    PolicyType p;

    LinearLayout subConditionAttrs;

    public ConditionLayout(Context c, PolicyType p, PolicyChanger policyChanger, ConditionType superCondition) {
        super(c);
        this.pc = policyChanger;
        this.sc = superCondition;
        this.p = p;
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.condition_layout, this);
        final Spinner spinner = (Spinner) findViewById(R.id.spinner_type_action);
        CheckBox not = (CheckBox) findViewById(R.id.notCheckbox);
        not.setChecked(sc.getConditionType().ifNot());
        if (isNothing()) {
            spinner.setSelection(0);
        } else if (isRepLim()) {
            spinner.setSelection(1);
        } else if (isWithin()) {
            spinner.setSelection(2);
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        // If it change the type of object
                        if (!isNothing()) {
                            sc.getConditionType().clearOperatorsSelect();
                            sc.getConditionType().setRepLim(null);
                            sc.getConditionType().setWithin(null);
                        }
                        break;
                    case 1:
                        if (isRepLim()) {
                            sc.getConditionType().clearOperatorsSelect();
                            sc.getConditionType().setRepLim(new RepLimType());
                        }
                        break;
                    case 2:
                        if (isWithin()) {
                            sc.getConditionType().clearOperatorsSelect();
                            sc.getConditionType().setWithin(new WithinType());
                        }
                        break;
                }
                createSubAttrs();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        subConditionAttrs = (LinearLayout) findViewById(R.id.subConditionAttrs);
        createSubAttrs();


    }

    private boolean isWithin() {
        return !sc.getConditionType().ifWithin() || (!(sc.getConditionType().getNot().getNotType() == null) && !sc.getConditionType().getNot().getNotType().ifRepLim());
    }

    private boolean isRepLim() {
        return !sc.getConditionType().ifRepLim() || (!(sc.getConditionType().getNot().getNotType() == null) && !sc.getConditionType().getNot().getNotType().ifRepLim());
    }

    private boolean isNothing() {
        return !sc.getConditionType().ifRepLim() && !sc.getConditionType().ifWithin() &&
                (!(sc.getConditionType().getNot() == null) && (!(sc.getConditionType().getNot().getNotType().ifRepLim())
                        && !(sc.getConditionType().getNot().getNotType().ifWithin())));
    }

    private void createSubAttrs() {
        subConditionAttrs.removeAllViews();
        HashMap<String, String> map = new HashMap<>();
        if (sc.getConditionType().ifRepLim()) {
            // TODO: Adapt it in relation of the emplacement of the condition
            map.put("Amount", String.valueOf(sc.getConditionType().getRepLim().getTimeAmountAttributeGroup().getAmount()));
            map.put("Unit", String.valueOf(sc.getConditionType().getRepLim().getTimeAmountAttributeGroup().getUnit().name()));
            map.put("Lower Limit", String.valueOf(sc.getConditionType().getRepLim().getLowerLimit()));
            map.put("Upper Limit", String.valueOf(sc.getConditionType().getRepLim().getUpperLimit()));
        } else if (sc.getConditionType().ifWithin()) {
            map.put("Amount", String.valueOf(sc.getConditionType().getWithin().getTimeAmountAttributeGroup().getAmount()));
            map.put("Unit", String.valueOf(sc.getConditionType().getWithin().getTimeAmountAttributeGroup().getUnit().name()));
        }

        for (String key : map.keySet()) {
            if (!key.equals("unit")) {
                View v = inflate(getContext(), R.layout.key_value_layout_tv, null);
                TextView tv = (TextView) v.findViewById(R.id.key);
                tv.setText(key);
                TextView tv2 = (TextView) v.findViewById(R.id.value);
                tv2.setText(map.get(key));
                subConditionAttrs.addView(v);
            } else {
                View v = inflate(getContext(), R.layout.key_spinner_layout, null);
                TextView tv = (TextView) v.findViewById(R.id.key);
                tv.setText(key);
                int pos = 0;
                Spinner s = (Spinner) v.findViewById(R.id.unitSpinner);
                for (int i = 0; i < s.getAdapter().getCount(); i++) {
                    if (s.getAdapter().getItem(i).equals(map.get(key))) {
                        pos = i;
                    }
                }
                s.setSelection(pos);
                subConditionAttrs.addView(v);
            }
        }
    }


}
