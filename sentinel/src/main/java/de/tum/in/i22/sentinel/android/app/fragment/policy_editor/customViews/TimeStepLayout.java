package de.tum.in.i22.sentinel.android.app.fragment.policy_editor.customViews;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import de.tum.in.i22.sentinel.android.app.R;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.interfaces.PolicyChanger;
import de.tum.in.www22.enforcementlanguage.PolicyType;
import de.tum.in.www22.enforcementlanguage.PreventiveMechanismType;
import de.tum.in.www22.time.TimeUnitType;

/**
 * Created by laurentmeyer on 27/12/15.
 */
public class TimeStepLayout extends RelativeLayout {

    Context c;
    PolicyType p;
    PolicyChanger pc;
    EditText et;
    Spinner s;

    public TimeStepLayout(Context context, PolicyType p, PolicyChanger pc) {
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
        final PreventiveMechanismType preventiveMechanism = p.getChoices().get(0).getPreventiveMechanism();
        String selected = preventiveMechanism.getTimestep().getUnit().name();
        int pos = 0;

        for (int i = 0; i<allPossibleValues.length; i++){
            if (allPossibleValues[i].equalsIgnoreCase(selected)){
                pos = i;
            }
        }
        s.setSelection(pos);
        // Not to call a wrapper toString
        et.setText(String.valueOf(preventiveMechanism.getTimestep().getAmount()));

        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                preventiveMechanism.getTimestep().setUnit(TimeUnitType.valueOf(TimeUnitType.class, s.getSelectedItem().toString()));
                pc.onPolicyChange();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private class FocusListener implements OnFocusChangeListener{

        @Override
        public void onFocusChange(View view, boolean b) {
            // If it is the spinner or the editText and the focus is not on one of this element anymore
            if (view.getId() == et.getId() && !b){
                p.getChoices().get(0).getPreventiveMechanism().getTimestep().setAmount(Integer.parseInt(et.getText().toString()));
                pc.onPolicyChange();
            }
        }
    }
}
