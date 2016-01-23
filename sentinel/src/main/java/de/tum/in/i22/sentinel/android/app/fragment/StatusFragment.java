package de.tum.in.i22.sentinel.android.app.fragment;

import android.app.Activity;
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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

import de.tum.in.i22.sentinel.android.app.Constants;
import de.tum.in.i22.sentinel.android.app.R;
import de.tum.in.i22.sentinel.android.app.file_explorer.FileChooser;
import de.tum.in.i22.uc.pdp.android.ServiceBoundListener;
import de.tum.in.i22.uc.pdp.android.pdpService;

/**
 * Created by laurentmeyer on 23/12/15.
 */
public class StatusFragment extends Fragment{

    private static de.tum.in.i22.uc.pdp.android.RemoteServiceConnection deployPolicyConnection = new de.tum.in.i22.uc.pdp.android.RemoteServiceConnection();

    private File currentPolicyFile = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.status_fragment, container, false);

        final Switch pdpServiceSwitch = (Switch) view.findViewById(R.id.pdpSwitch);
        Button deployPolicy = (Button) view.findViewById(R.id.deployPolicy);
        deployPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pdpServiceSwitch.setChecked(true);
                Intent intent = new Intent(getActivity(), FileChooser.class);
                intent.putExtra(Constants.EXTENSION, Constants.INPUT_XML);
                startActivityForResult(intent, 0);
            }
        });
        pdpServiceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    if (isChecked) {
                        // Activate the PDP
                        // WARNING: This line won't work on 5.0 devices because Google enforces explicit intent!
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
        SharedPreferences sp = getActivity().getSharedPreferences(Constants.SENTINEL, 0);
        int retrievedAmount = sp.getInt(Constants.INSTRUMENTED_APPLICATIONS, 0);
        return retrievedAmount;
    }

    private void deployPolicy(File pathToPolicy) {
        try {
            // Read in the policy file
            BufferedReader rdr = new BufferedReader(
                    new FileReader(pathToPolicy));
            String line = "";
            String data = "";
            while ((line = rdr.readLine()) != null)
                data += line + "\n";
            rdr.close();

            // Deploy the policy
            Bundle event = new Bundle();
            event.putString("policy", data);

            Message m = Message.obtain();
            m.setData(event);
            deployPolicyConnection.getMessenger().send(m);

            Toast.makeText(getActivity(), "Policy deployed",
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == 0){
            String pathToPolicy = data.getStringExtra(Constants.ABSOLUTE_PATH);
            deployPolicy(new File(pathToPolicy));
        }
    }
}
