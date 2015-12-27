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

/**
 * Created by laurentmeyer on 27/12/15.
 */
public class ParamMatchDialog {

    HashMap<String, String> map;
    Context c;
    AlertDialog.Builder b;

    public ParamMatchDialog(Context context, HashMap<String, String> mapToBeSet) {
        map = mapToBeSet;
        c = context;
        init();
    }

    private void init() {
        b = new AlertDialog.Builder(c);
        b.setTitle("Param Match");
        LinearLayout linearLayout = new LinearLayout(c);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        LayoutInflater li = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (String key : map.keySet()) {
            LinearLayout ll = (LinearLayout) li.inflate(R.layout.row_param_match_dialog_layout, linearLayout);
            TextView tv = (TextView) ll.findViewById(R.id.key);
            tv.setText(key);
            TextView tv2 = (TextView) ll.findViewById(R.id.value);
            if (map.get(key) != null && map.get(key) != "")
                tv2.setText(map.get(key));
        }
        b.setView(linearLayout);
        b.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d("ParamMatchDialog", "Ok clicked");
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
