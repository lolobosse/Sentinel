package de.tum.in.i22.sentinel.android.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import de.tum.in.i22.sentinel.android.app.R;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.Utils;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.customViews.PolicyEditorLayout;

/**
 * Created by laurentmeyer on 23/12/15.
 */
public class PolicyEditorFragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            PolicyEditorLayout layout = new PolicyEditorLayout(getActivity(), Utils.getPolicyFromRaw(getActivity(), R.raw.policy_appsms_duration4));
            ScrollView sv = new ScrollView(getActivity());
            sv.setLayoutParams(new ScrollView.LayoutParams(ScrollView.LayoutParams.MATCH_PARENT, ScrollView.LayoutParams.MATCH_PARENT));
            sv.addView(layout);
            return sv;
        } catch (Exception e) {
            return null;
        }
    }
}
