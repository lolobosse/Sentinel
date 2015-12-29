package de.tum.in.i22.sentinel.android.app.fragment.policy_editor.customViews;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.HashMap;

import de.tum.in.i22.sentinel.android.app.R;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.EventMatchCondition;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.Policy;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.RepLim;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.RepLimCondition;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.SuperCondition;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.Within;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.WithinCondition;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.interfaces.PolicyChanger;

/**
 * Created by laurentmeyer on 27/12/15.
 */
public class ConditionLayout extends RelativeLayout {

    SuperCondition sc;
    PolicyChanger pc;
    Policy p;

    LinearLayout subConditionAttrs;

    public ConditionLayout(Context c, Policy p, PolicyChanger policyChanger, SuperCondition superCondition) {
        super(c);
        this.pc = policyChanger;
        this.sc = superCondition;
        this.p = p;
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.condition_layout, this);
        Spinner spinner = (Spinner) findViewById(R.id.spinner_type_action);
        CheckBox not = (CheckBox) findViewById(R.id.notCheckbox);
        not.setChecked(sc.isNot());
        if (sc instanceof EventMatchCondition) {
            spinner.setSelection(0);
        } else if (sc instanceof RepLimCondition) {
            spinner.setSelection(1);
        } else if (sc instanceof WithinCondition) {
            spinner.setSelection(2);
        }
        subConditionAttrs = (LinearLayout) findViewById(R.id.subConditionAttrs);
        HashMap<String, String> map = new HashMap<>();
        if (sc instanceof RepLimCondition) {
            RepLim repLim = ((RepLimCondition) sc).getRepLims().get(0);
            map.put(repLim.getAmountKey(), String.valueOf(repLim.getAmount()));
            map.put(repLim.getUnitKey(), repLim.getUnit());
            map.put(repLim.getLowerLimitKey(), String.valueOf(repLim.getLowerLimit()));
            map.put(repLim.getUpperLimitKey(), String.valueOf(repLim.getUpperLimit()));
        } else if (sc instanceof WithinCondition) {
            Within w = ((WithinCondition) sc).getWithins().get(0);
            map.put(w.getAmountKey(), String.valueOf(w.getAmount()));
            map.put(w.getUnitKey(), w.getUnit());
        }

        for (String key : map.keySet()) {
            if (!key.equals("unit")) {
                View v = inflate(getContext(), R.layout.key_value_layout_tv, null);
                TextView tv = (TextView) v.findViewById(R.id.key);
                tv.setText(key);
                TextView tv2 = (TextView) v.findViewById(R.id.value);
                tv2.setText(map.get(key));
                subConditionAttrs.addView(v);
            }
            else{
                View v = inflate(getContext(), R.layout.key_spinner_layout, null);
                TextView tv = (TextView) v.findViewById(R.id.key);
                tv.setText(key);
                int pos = 0;
                Spinner s = (Spinner) v.findViewById(R.id.unitSpinner);
                for (int i = 0; i<s.getAdapter().getCount(); i++){
                    if (s.getAdapter().getItem(i).equals(map.get(key))){
                        pos = i;
                    }
                }
                s.setSelection(pos);
                subConditionAttrs.addView(v);
            }
        }


    }


}
