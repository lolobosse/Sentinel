package de.tum.in.i22.sentinel.android.app.fragment.policy_editor.customViews;

import android.content.Context;
import android.widget.LinearLayout;

import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.interfaces.PolicyChanger;
import de.tum.in.www22.enforcementlanguage.PolicyType;

/**
 * Created by laurentmeyer on 27/12/15.
 */
public class TriggerContainer extends LinearLayout {

    PolicyChanger pc;
    Context c;
    PolicyType p;

    public TriggerContainer(Context context, PolicyType policy, PolicyChanger pc) {
        super(context);
        c = context;
        p = policy;
        this.pc = pc;
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        addView(new TriggerView(c, p.getChoices().get(0).getPreventiveMechanism().getTrigger(), pc));
    }
}
