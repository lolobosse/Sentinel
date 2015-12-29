package de.tum.in.i22.sentinel.android.app.fragment.policy_editor.customViews;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.concurrent.locks.Condition;

import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.Policy;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.SuperCondition;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.interfaces.PolicyChanger;

/**
 * Created by laurentmeyer on 27/12/15.
 */
public class ConditionContainerLayout extends LinearLayout {

    private final Policy p;
    private final PolicyChanger policyChanger;
    Context c;


    public ConditionContainerLayout(Context context, Policy p, PolicyChanger pc) {
        super(context);
        c = context;
        this.p = p;
        this.policyChanger = pc;
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        for (SuperCondition c : p.getMechanism().getConditions()){
            addView(new ConditionLayout(this.c, p, policyChanger, c));
        }
    }


}
