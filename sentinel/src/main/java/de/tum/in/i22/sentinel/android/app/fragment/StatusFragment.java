package de.tum.in.i22.sentinel.android.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.tum.in.i22.sentinel.android.app.R;

/**
 * Created by laurentmeyer on 23/12/15.
 */
public class StatusFragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.status_fragment, container, false);

        TextView textView = (TextView)view.findViewById(R.id.statusLeftFrameText);
        textView.setText("test");

        return view;




    }
}
