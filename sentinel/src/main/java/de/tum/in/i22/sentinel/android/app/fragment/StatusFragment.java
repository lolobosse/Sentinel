package de.tum.in.i22.sentinel.android.app.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
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
import de.tum.in.i22.sentinel.android.app.backend.APKUtils;
import de.tum.in.i22.sentinel.android.app.file_explorer.FileChooser;
import de.tum.in.i22.sentinel.android.app.package_getter.PackageGetter;
import de.tum.in.i22.uc.pdp.android.ServiceBoundListener;
import de.tum.in.i22.uc.pdp.android.pdpService;

/**
 * Created by laurentmeyer on 23/12/15.
 */
public class StatusFragment extends Fragment implements PackageGetter.Callback{

    private static de.tum.in.i22.uc.pdp.android.RemoteServiceConnection deployPolicyConnection = new de.tum.in.i22.uc.pdp.android.RemoteServiceConnection();

    private File currentPolicyFile = null;
    private int numberOfApps = 0;

    /**
     * Very ugly and not satisfying method which starts an instance of the PDP as a Service.
     * The code is coming from the original project (appPDPj) and wasn't even refactored.
     * It works as is and we still have issue to bind it with policies as you can see in the report
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.status_fragment, container, false);
        getActivity().setTitle("Status");

        final Switch pdpServiceSwitch = (Switch) view.findViewById(R.id.pdpSwitch);
        Button deployPolicy = (Button) view.findViewById(R.id.deployPolicy);
        deployPolicy.setOnClickListener(new View.OnClickListener() {

            /**
             * Coming directly from PDP
             * @param view
             */
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
                if (isChecked) {
                    // First prevent the program from crashing by checking the build version of the system
                    if (Build.VERSION.SDK_INT> 19){
                        Toast.makeText(getActivity(), "Doesn't work on Android >= 5.0", Toast.LENGTH_LONG).show();
                        return;
                    }
                    // Activate the PDP
                    // WARNING: This line won't work on 5.0 devices because Google enforces explicit intent!
                    Intent start = new Intent("de.tum.in.i22.uc.pdp.android.pdpService");
                    getActivity().startService(start);
                    if (!deployPolicyConnection.isBound()) {
                        Intent intent = new Intent();
                        intent.setClassName("de.tum.in.i22.sentinel.android.app",
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
                    } else {
                        Log.d("MainViewFragment", "PDP is bound");
                    }
                } else {
                    Log.d("MainViewFragment", "Lol");
                }
            }
        });

        // Displays the amount of packages installed on the device
        TextView applicationCounter = (TextView) view.findViewById(R.id.statusLeftFrameNmr);
        PackageGetter.getPackages(StatusFragment.this, getActivity());
        applicationCounter.setText(String.valueOf(numberOfApps));

        // Displays the amount of instrumented applications on the device
        TextView instrumentedCounter = (TextView) view.findViewById(R.id.statusRightFrameNmr);
        int numberOfInstrumentedApps = APKUtils.getNumberOfInstrumentedApp(getActivity());
        instrumentedCounter.setText(String.valueOf(numberOfInstrumentedApps));

        return view;
    }

    /**
     * This methods comes also from the original appPDPj as-is
     * @param pathToPolicy: which policy do we wanna deploy
     */
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

    /**
     * Called when the user chose the his policy, supposed to deploy it
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == 0) {
            String pathToPolicy = data.getStringExtra(Constants.ABSOLUTE_PATH);
            deployPolicy(new File(pathToPolicy));
        }
    }

    @Override
    public void onError(Exception e) {

    }

    @Override
    public void onSuccess(List<PackageGetter.Package> packages) {
        numberOfApps = packages.size();
    }
}
