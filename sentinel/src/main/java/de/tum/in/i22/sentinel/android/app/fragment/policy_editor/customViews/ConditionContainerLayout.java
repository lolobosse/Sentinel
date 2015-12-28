package de.tum.in.i22.sentinel.android.app.fragment.policy_editor.customViews;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.concurrent.locks.Condition;

import de.tum.in.i22.sentinel.android.app.R;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.EventMatchCondition;
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
        ViewGroup vg = (ViewGroup) inflate(getContext(), R.layout.condition_container_layout, null);
        final LinearLayout ll = (LinearLayout) vg.findViewById(R.id.conditionContainer);
        setOrientation(VERTICAL);
        for (SuperCondition c : p.getMechanism().getConditions()){
            ll.addView(new ConditionLayout(this.c, p, policyChanger, c));
        }
        Button b = (Button) vg.findViewById(R.id.addCondition);
        b.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                inflate(ConditionContainerLayout.this.getContext(), R.layout.horizontal_bar, ll);
                ll.addView(new ConditionLayout(ConditionContainerLayout.this.getContext(), p, policyChanger, new EventMatchCondition()));
            }
        });
        addView(vg);
    }


}
