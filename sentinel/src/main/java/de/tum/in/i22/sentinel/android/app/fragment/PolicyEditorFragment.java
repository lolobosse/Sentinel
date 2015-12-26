package de.tum.in.i22.sentinel.android.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.ArrayList;

import de.tum.in.i22.sentinel.android.app.R;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.Policy;

/**
 * Created by laurentmeyer on 23/12/15.
 */
public class PolicyEditorFragment extends Fragment{

    Policy p;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.trigger_layout, container, false);
    }

//    private class CustomAdapter extends BaseExpandableListAdapter{
//
//        Policy p;
//
//        CustomAdapter(Policy p){
//            this.p = p;
//        }
//
//        @Override
//        public int getGroupCount() {
//            return 4;
//        }
//
//        @Override
//        public int getChildrenCount(int i) {
//            switch (i){
//                case 0:
//                    return 1;
//                case 1:
//                    return 2;
//                case 2:
//                    return 0;
//                case 3:
//                    return 1;
//                default:
//                    return 0;
//            }
//        }
//
//        @Override
//        public Object getGroup(int i) {
//            switch (i){
//                case 0:
//                    return p.getMechanism().getDescription();
//                case 1:
//                    return p.getMechanism().getTriggers();
//                case 2:
//                    return p.getMechanism().getConditions();
//                case 3:
//                    return p.getMechanism().getAuthorizationAction();
//                default:
//                    return null;
//            }
//        }
//
//        @Override
//        public Object getChild(int i, int i1) {
//            return null;
//        }
//
//        @Override
//        public long getGroupId(int i) {
//            return 0;
//        }
//
//        @Override
//        public long getChildId(int i, int i1) {
//            return 0;
//        }
//
//        @Override
//        public boolean hasStableIds() {
//            return false;
//        }
//
//        @Override
//        public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
//            return null;
//        }
//
//        @Override
//        public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
//            return null;
//        }
//
//        @Override
//        public boolean isChildSelectable(int i, int i1) {
//            return false;
//        }
//    }
}
