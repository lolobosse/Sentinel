package de.tum.in.i22.sentinel.android.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.tum.in.i22.sentinel.android.app.R;

/**
 * Created by Moderbord on 2016-01-18.
 */
public class PostInstrumentFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.post_instrument_fragment, container, false);

        return view;
    }
}
