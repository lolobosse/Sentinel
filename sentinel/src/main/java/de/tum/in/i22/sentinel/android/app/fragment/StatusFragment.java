package de.tum.in.i22.sentinel.android.app.fragment;

import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.tum.in.i22.sentinel.android.app.R;

/**
 * Created by laurentmeyer on 23/12/15.
 */
public class StatusFragment extends Fragment{

    public final String SENTINEL = "sentinel";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.status_fragment, container, false);

        // Displays the amount of packages installed on the device
        TextView applicationCounter = (TextView) view.findViewById(R.id.statusLeftFrameNmr);
        int numberOfApps = installedApplications().size();
        applicationCounter.setText("" + numberOfApps);

        // Displays the amount of instrumented applications on the device
        TextView instrumentedCounter = (TextView) view.findViewById(R.id.statusRightFrameNmr);
        int numberOfInstrumentedApps = instrumentedApplications();
        instrumentedCounter.setText("" + numberOfInstrumentedApps);

        return view;
    }

    /**
     *
     * Returns a list of all packages installed on the device
     */
    public List installedApplications(){
        PackageManager pm = getActivity().getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        return packages;
    }

    /**
     *
     * Returns the amount of instrumented applications
     */
    public int instrumentedApplications(){
        SharedPreferences sp = getActivity().getSharedPreferences(SENTINEL, 0);
        int retrievedAmount = sp.getInt("instrumentedApplications", 0);
        return retrievedAmount;
    }

}
