package de.tum.in.i22.sentinel.android.app.fragment.policy_editor.customViews;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;

import de.tum.in.i22.sentinel.android.app.R;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.interfaces.DialogSet;

/**
 * Created by laurentmeyer on 27/12/15.
 */
public class ParamMatchDialog {

    HashMap<String, String> map;
    Context c;
    AlertDialog.Builder b;
    DialogSet ds;
    HashMap<TextView, TextView> textViews;

    public ParamMatchDialog(Context context, HashMap<String, String> mapToBeSet, DialogSet ds) {
        map = mapToBeSet;
        c = context;
        this.ds = ds;
        init();
    }

    private void init() {
        b = new AlertDialog.Builder(c);
        b.setTitle("Param Match");
        LinearLayout linearLayout = new LinearLayout(c);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        LayoutInflater li = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        textViews = new HashMap<>();
        for (String key : map.keySet()) {
            LinearLayout ll = (LinearLayout) li.inflate(R.layout.key_value_layout_tv, null);
            TextView tv = (TextView) ll.findViewById(R.id.key);
            tv.setText(key);
            TextView tv2 = (TextView) ll.findViewById(R.id.value);
            if (map.get(key) != null && map.get(key) != "")
                tv2.setText(map.get(key));
            textViews.put(tv, tv2);
            linearLayout.addView(ll);
        }
        b.setView(linearLayout);
        b.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                HashMap<String, String> results = new HashMap<>();
                for (TextView t : textViews.keySet()){
                    results.put(t.getText().toString(), textViews.get(t).getText().toString());
                }
                ds.parametersDefined(results);
            }
        });
        b.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d("ParamMatchDialog", "Cancel clicked");
            }
        });
    }

    public Dialog getDialog() {
        return b.create();
    }


}
