package de.tum.in.i22.sentinel.android.app.fragment.policy_editor.customViews;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import java.util.HashMap;

import de.tum.in.i22.sentinel.android.app.R;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.ParamMatch;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.Trigger;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.interfaces.PolicyChanger;

/**
 * Created by laurentmeyer on 27/12/15.
 */
public class TriggerView extends RelativeLayout {

    Context c;
    Trigger t;
    PolicyChanger pc;

    public TriggerView(Context c, Trigger t, PolicyChanger pc) {
        super(c);
        this.c = c;
        this.t = t;
        this.pc = pc;
        init();
    }

    private void init() {
        inflate(c, R.layout.trigger_layout, this);
        final CheckBox c = (CheckBox) findViewById(R.id.checkboxTry);
        final Spinner s = (Spinner) findViewById(R.id.actionChooser);
        final Button b = (Button) findViewById(R.id.parametersButton);
        int pos = 0;
        if (!s.getAdapter().isEmpty()){
            for (int i = 0; i<s.getAdapter().getCount(); i++){
                if (((ActionChooser.Event)s.getAdapter().getItem(i)).name.equalsIgnoreCase(t.getAction())){
                    pos = i;
                }
            }
        }
        s.setSelection(pos);
        c.setChecked(t.isTryEvent());
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // We cannot do any param match if there is no param to analyse
                b.setEnabled(!((ActionChooser.Event) s.getAdapter().getItem(i)).data.isEmpty());
                b.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        HashMap<String, String> map = new HashMap<>();
                        for (ParamMatch param : t.getParamMatches()) {
                            map.put(param.getName() + ":", param.getValue());
                        }
                        ParamMatchDialog pmd = new ParamMatchDialog(getContext(), map);
                        pmd.getDialog().show();
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                t.setAction(((ActionChooser.Event) (s.getSelectedItem())).name);
                pc.onPolicyChange();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                t.setTryEvent(c.isChecked());
                pc.onPolicyChange();
            }
        });
    }
}
