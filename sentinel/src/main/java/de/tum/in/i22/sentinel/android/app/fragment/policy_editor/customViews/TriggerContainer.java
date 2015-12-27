package de.tum.in.i22.sentinel.android.app.fragment.policy_editor.customViews;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import de.tum.in.i22.sentinel.android.app.R;
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
        setOrientation(VERTICAL);
        for (Trigger t : p.getMechanism().getTriggers()){
            addView(new TriggerView(c, t, pc));
        }
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.button_add, this);
        Button b = (Button) findViewById(R.id.button);
        b.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TriggerContainer", "New Trigger condition requested");
            }
        });
    }
}
