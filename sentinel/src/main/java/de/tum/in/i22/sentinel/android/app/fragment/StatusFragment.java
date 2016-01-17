package de.tum.in.i22.sentinel.android.app.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

import de.tum.in.i22.sentinel.android.app.R;
import de.tum.in.i22.uc.pdp.android.ServiceBoundListener;
import de.tum.in.i22.uc.pdp.android.pdpService;

/**
 * Created by laurentmeyer on 23/12/15.
 */
public class StatusFragment extends Fragment{

    public final String SENTINEL = "sentinel", INSTRUMENTED_APPLICATIONS = "instrumentedApplications";

    private static de.tum.in.i22.uc.pdp.android.RemoteServiceConnection deployPolicyConnection = new de.tum.in.i22.uc.pdp.android.RemoteServiceConnection();

    private File currentPolicyFile = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.status_fragment, container, false);

        Switch pdpServiceSwitch = (Switch) view.findViewById(R.id.pdpSwitch);
        pdpServiceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    if (isChecked) {
                        // Activate the PDP
                        Intent start = new Intent("de.tum.in.i22.uc.pdp.android.pdpService");
                        getActivity().startService(start);
                        if (!deployPolicyConnection.isBound()) {
                            Intent intent = new Intent();
                            intent.setClassName("de.tum.in.i22.uc.pdp.android",
                                    "de.tum.in.i22.uc.pdp.android.pdpService");
                            intent.setAction(pdpService.ACTION_PDP_SETPOLICY);
                            getActivity().bindService(intent, deployPolicyConnection,
                                    Context.BIND_AUTO_CREATE);
                            deployPolicyConnection
                                    .addOnServiceBoundListener(new ServiceBoundListener() {

                                        @Override
                                        public void serviceBound(
                                                de.tum.in.i22.uc.pdp.android.RemoteServiceConnection connection) {
                                            Log.i("LOLO IS TESTING", "serviceBound");
                                            deployPolicy(currentPolicyFile);

                                        }
                                    });
                        }
                        else {
                            Log.d("MainViewFragment", "PDP is bound");
                        }
                    } else {
                        Log.d("MainViewFragment", "Lol");
                    }
                } else {
                    // pdpService off
                }
            }
        });

        // Displays the amount of packages installed on the device
        TextView applicationCounter = (TextView) view.findViewById(R.id.statusLeftFrameNmr);
        int numberOfApps = installedApplications().size();
        applicationCounter.setText(String.valueOf(numberOfApps));

        // Displays the amount of instrumented applications on the device
        TextView instrumentedCounter = (TextView) view.findViewById(R.id.statusRightFrameNmr);
        int numberOfInstrumentedApps = instrumentedApplications();
        instrumentedCounter.setText(String.valueOf(numberOfInstrumentedApps));

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
        int retrievedAmount = sp.getInt(INSTRUMENTED_APPLICATIONS, 0);
        return retrievedAmount;
    }

    private void deployPolicy(File pathToPolicy) {
        Log.d("LOLO IS TESTING", "deployPolicy method");
        try {
            // Read in the policy file
            BufferedReader rdr = new BufferedReader(
                    new FileReader(pathToPolicy));
            String line = "";
            String data = "";
            while ((line = rdr.readLine()) != null)
                data += line + "\n";
            rdr.close();
            Log.d("LOLO IS TESTING", "Policy file read.");

            // Deploy the policy
            Bundle event = new Bundle();
            event.putString("policy", data);

            Message m = Message.obtain();
            m.setData(event);
            Log.d("LOLO IS TESTING", "sending deployment message");
            deployPolicyConnection.getMessenger().send(m);
            Log.d("LOLO IS TESTING", "deployment message sent");

            Toast.makeText(getActivity(), "Policy deployed",
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.e("LOLO IS TESTING", "Exception during deployment" + e);
            Log.e("LOLO IS TESTING", e.getMessage());
            e.printStackTrace();
        }
    }

}
