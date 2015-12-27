package de.tum.in.i22.sentinel.android.app.fragment.policy_editor.customViews;

import android.content.Context;
import android.widget.LinearLayout;

import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.Policy;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.Trigger;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.interfaces.PolicyChanger;

/**
 * Created by laurentmeyer on 27/12/15.
 */
public class TriggerContainer extends LinearLayout {

    PolicyChanger pc;
    Context c;
    Policy p;

    public TriggerContainer(Context context, Policy policy, PolicyChanger pc) {
        super(context);
        c = context;
        p = policy;
        this.pc = pc;
        init();
    }

    private void init() {
        for (Trigger t : p.getMechanism().getTriggers()){
            addView(new TriggerView(c, t, pc));
        }
    }
}
