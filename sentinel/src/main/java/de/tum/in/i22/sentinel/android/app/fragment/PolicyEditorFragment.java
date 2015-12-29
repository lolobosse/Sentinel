package de.tum.in.i22.sentinel.android.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import de.tum.in.i22.sentinel.android.app.R;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.customViews.DescriptionLayout;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.Policy;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.customViews.PolicyEditorLayout;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.customViews.TimeStepLayout;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.interfaces.PolicyChanger;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.parser.PolicyParser;

/**
 * Created by laurentmeyer on 23/12/15.
 */
public class PolicyEditorFragment extends Fragment{

    Policy p;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//        View view = inflater.inflate(R.layout.policy_editor_fragment, container, false);
//        ExpandableListView elv = (ExpandableListView) view.findViewById(R.id.expLv);
//        try {
//            elv.setAdapter(new CustomAdapter(PolicyParser.parsePolicyFromResId(getActivity(), R.raw.policy_appsms_duration4)));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return view;
        try {
            PolicyEditorLayout layout = new PolicyEditorLayout(getActivity(), PolicyParser.parsePolicyFromResId(getActivity(), R.raw.policy_appsms_duration4));
            ScrollView sv = new ScrollView(getActivity());
            sv.setLayoutParams(new ScrollView.LayoutParams(ScrollView.LayoutParams.MATCH_PARENT, ScrollView.LayoutParams.MATCH_PARENT));
            sv.addView(layout);
            return sv;
        } catch (Exception e) {
            return null;
        }
    }

    private class CustomAdapter extends BaseExpandableListAdapter implements PolicyChanger {

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
            return true;
        }

        @Override
        public View getGroupView(int i, boolean b, View convertView, ViewGroup viewGroup) {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.policy_expandable_list_view_layout, null);
                TextView tv = (TextView) convertView.findViewById(R.id.categoryTitle);
                switch (i) {
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
            }
            return convertView;
        }

        @Override
        public View getChildView(int i, int i1, boolean b, View convertView, ViewGroup viewGroup) {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            if (convertView == null) {
                switch (getChildType(i, i1)) {
                    case 0:
                        return new DescriptionLayout(getActivity(), p, this);
                    case 1:
                        return new TimeStepLayout(getActivity(), p, this);
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
        public int getChildTypeCount() {
            return getGroupCount();
        }

        @Override
        public int getChildType(int groupPosition, int childPosition) {
            return groupPosition;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return false;
        }

        @Override
        public void onPolicyChange() {
            Log.d("CustomAdapter", "Policy change called in the PolicyEditorFragment");
            notifyDataSetChanged();
        }
    }
}
