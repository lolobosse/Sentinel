package de.tum.in.i22.sentinel.android.app.fragment.policy_editor.customViews;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import de.tum.in.i22.sentinel.android.app.R;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.Policy;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.interfaces.PolicyChanger;

/**
 * Created by laurentmeyer on 27/12/15.
 */
public class TimeStepLayout extends RelativeLayout {

    Context c;
    Policy p;
    PolicyChanger pc;
    EditText et;
    Spinner s;

    public TimeStepLayout(Context context, Policy p, PolicyChanger pc) {
        super(context);
        c = context;
        this.p = p;
        this.pc = pc;
        init();
    }

    private void init() {
        inflate(c, R.layout.timestep_layout, this);
        et = (EditText) findViewById(R.id.amountET);
        s = (Spinner) findViewById(R.id.unitSpinner);
        FocusListener fl = new FocusListener();
        et.setOnFocusChangeListener(fl);
        s.setOnFocusChangeListener(fl);


        String [] allPossibleValues = getResources().getStringArray(R.array.spinnerItems);
        String selected = p.getMechanism().getTimestep().getUnit();
        int pos = 0;

        for (int i = 0; i<allPossibleValues.length; i++){
            if (allPossibleValues[i].equalsIgnoreCase(selected)){
                pos = i;
            }
        }
        s.setSelection(pos);
        // Not to call a wrapper toString
        et.setText(p.getMechanism().getTimestep().getAmount()+ "");
    }

    private class FocusListener implements OnFocusChangeListener{

        @Override
        public void onFocusChange(View view, boolean b) {
            // If it is the spinner or the editText and the focus is not on one of this element anymore
            if ((view.getId() == et.getId() || view.getId() == s.getId()) && !b){
                p.getMechanism().getTimestep().setAmount(Integer.parseInt(et.getText().toString()));
                p.getMechanism().getTimestep().setUnit(s.getSelectedItem().toString());
                pc.onPolicyChange();
            }
        }
    }
}
