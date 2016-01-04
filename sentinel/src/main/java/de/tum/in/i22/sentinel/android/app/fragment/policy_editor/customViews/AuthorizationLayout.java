package de.tum.in.i22.sentinel.android.app.fragment.policy_editor.customViews;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.tum.in.i22.sentinel.android.app.R;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.interfaces.PolicyChanger;
import de.tum.in.www22.enforcementlanguage.PolicyType;

/**
 * Created by laurentmeyer on 27/12/15.
 */
public class AuthorizationLayout extends RelativeLayout {

    Context c;
    PolicyType p;
    PolicyChanger pc;

    public AuthorizationLayout(Context context, PolicyType p, PolicyChanger pc) {
        super(context);
        this.c = context;
        this.p = p;
        this.pc = pc;
        init();
    }

    private void init() {
        inflate(c, R.layout.authorization_layout, this);
        TextView tv = (TextView) findViewById(R.id.authName);
        tv.setText(p.getChoices().get(0).getPreventiveMechanism().getAuthorizationActions().get(0).getName());
    }
}
