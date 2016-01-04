package de.tum.in.i22.sentinel.android.app.fragment.policy_editor.customViews;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import de.tum.in.i22.sentinel.android.app.R;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.interfaces.PolicyChanger;
import de.tum.in.www22.enforcementlanguage.ConditionType;
import de.tum.in.www22.enforcementlanguage.PolicyType;

/**
 * Created by laurentmeyer on 27/12/15.
 */
public class ConditionContainerLayout extends LinearLayout {

    private final PolicyType p;
    private final PolicyChanger policyChanger;
    Context c;


    public ConditionContainerLayout(Context context, PolicyType p, PolicyChanger pc) {
        super(context);
        c = context;
        this.p = p;
        this.policyChanger = pc;
        init();
    }

    private void init() {
        ViewGroup vg = (ViewGroup) inflate(getContext(), R.layout.condition_container_layout, null);
        final LinearLayout ll = (LinearLayout) vg.findViewById(R.id.conditionContainer);
        setOrientation(VERTICAL);
        ConditionType c = p.getChoices().get(0).getPreventiveMechanism().getCondition();
        ll.addView(new ConditionLayout(this.c, p, policyChanger));
        addView(vg);
    }


}
