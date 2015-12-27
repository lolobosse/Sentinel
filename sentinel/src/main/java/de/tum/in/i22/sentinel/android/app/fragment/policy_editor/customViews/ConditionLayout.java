package de.tum.in.i22.sentinel.android.app.fragment.policy_editor.customViews;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import de.tum.in.i22.sentinel.android.app.R;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.EventMatchCondition;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.Policy;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.RepLimCondition;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.SuperCondition;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.WithinCondition;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.interfaces.PolicyChanger;

/**
 * Created by laurentmeyer on 27/12/15.
 */
public class ConditionLayout extends RelativeLayout {

    SuperCondition sc;
    PolicyChanger pc;
    Policy p;

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
        if (sc instanceof EventMatchCondition){
            spinner.setSelection(0);
        }
        else if (sc instanceof RepLimCondition){
            spinner.setSelection(1);
        }
        else if (sc instanceof WithinCondition){
            spinner.setSelection(2);
        }
    }


}
