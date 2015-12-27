package de.tum.in.i22.sentinel.android.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import de.tum.in.i22.sentinel.android.app.R;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.Policy;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.parser.PolicyParser;

/**
 * Created by laurentmeyer on 23/12/15.
 */
public class PolicyEditorFragment extends Fragment{

    Policy p;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.policy_editor_fragment, container, false);
        ExpandableListView elv = (ExpandableListView) view.findViewById(R.id.expLv);
        try {
            elv.setAdapter(new CustomAdapter(PolicyParser.parsePolicyFromResId(getActivity(), R.raw.policy_appsms_duration4)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    private class CustomAdapter extends BaseExpandableListAdapter{

        Policy p;
        CustomAdapter(Policy p){
            this.p = p;
        }

        @Override
        public int getGroupCount() {
            return 5;
        }

        @Override
        public int getChildrenCount(int i) {
            // TODO: Make it policy dependent
            return 1;
        }

        @Override
        public Object getGroup(int i) {
            switch (i){
                case 0:
                    return p.getMechanism().getDescription();
                case 1:
                    return p.getMechanism().getTimestep();
                case 2:
                    return p.getMechanism().getTriggers();
                case 3:
                    return p.getMechanism().getConditions();
                case 4:
                    return p.getMechanism().getAuthorizationAction();
                default:
                    return null;
            }
        }

        @Override
        public Object getChild(int i, int i1) {
            return getGroup(i);
        }

        @Override
        public long getGroupId(int i) {
            return getGroup(i).hashCode();
        }

        @Override
        public long getChildId(int i, int i1) {
            return getGroup(i).hashCode();
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
            TextView tv = new TextView(getActivity());
            switch (i){
                case 0:
                    tv.setText("Description");
                    break;
                case 1:
                    tv.setText("Timestep");
                    break;
                case 2:
                    tv.setText("Trigger");
                    break;
                case 3:
                    tv.setText("Condition");
                    break;
                case 4:
                    tv.setText("Authorization");
                    break;
            }
            return tv;
        }

        @Override
        public View getChildView(int i, int i1, boolean b, View convertView, ViewGroup viewGroup) {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View v;
            if (convertView == null) {
                switch (i) {
                    case 0:
                        return inflater.inflate(R.layout.description_layout, null);
                    case 1:
                        return inflater.inflate(R.layout.timestep_layout, null);
                    case 2:
                        return inflater.inflate(R.layout.trigger_layout, null);
                    case 3:
                        return inflater.inflate(R.layout.condition_layout, null);
                    case 4:
                        return inflater.inflate(R.layout.authorization_layout, null);
                    default:
                        return null;
                }
            }
            else{
                return convertView;
            }
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return false;
        }
    }
}
