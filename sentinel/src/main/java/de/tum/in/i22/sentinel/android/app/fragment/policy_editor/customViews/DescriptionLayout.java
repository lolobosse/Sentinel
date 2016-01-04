package de.tum.in.i22.sentinel.android.app.fragment.policy_editor.customViews;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.tum.in.i22.sentinel.android.app.R;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.interfaces.PolicyChanger;
import de.tum.in.www22.enforcementlanguage.PolicyType;
import de.tum.in.www22.enforcementlanguage.PreventiveMechanismType;

/**
 * Created by laurentmeyer on 27/12/15.
 */
public class DescriptionLayout extends RelativeLayout{

    Context c;
    PolicyType p;
    PolicyChanger pc;

    public DescriptionLayout(Context context, PolicyType p, PolicyChanger pc) {
        super(context);
        this.c = context;
        this.p = p;
        this.pc = pc;
        init();
    }

    private void init() {
        inflate(c, R.layout.description_layout, this);
        final TextView tv = (TextView) findViewById(R.id.descriptionET);
        final PreventiveMechanismType preventiveMechanism = p.getChoices().get(0).getPreventiveMechanism();
        tv.setOnFocusChangeListener(new OnFocusChangeListener() {
            /**
             * We save on focus change to avoid saving for every character
             * @param viewFocused
             * @param b
             */
            @Override
            public void onFocusChange(View viewFocused, boolean b) {
                if (viewFocused.getId() == tv.getId() && !b){
                    preventiveMechanism.setDescription(tv.getText().toString());
                    Log.d("DescriptionLayout", "On Policy change is called with this description " + preventiveMechanism.getDescription());
                    pc.onPolicyChange();
                }
            }
        });

        // Init of the text
        if (preventiveMechanism != null && preventiveMechanism.getDescription() != null) {
            tv.setText(preventiveMechanism.getDescription());
        }

    }


}
