package de.tum.in.i22.sentinel.android.app.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.File;

import de.tum.in.i22.sentinel.android.app.Constants;
import de.tum.in.i22.sentinel.android.app.R;
import de.tum.in.i22.sentinel.android.app.file_explorer.FileChooser;

/**
 * Created by laurentmeyer on 21/01/16.
 */

/**
 * This class is there to send the user to the XML Editor in order to create/modify their policies
 */
public class PolicyEditor extends Fragment {

    private int filePickingRequest = 0;

    /**
     * Creates a very simple view where the user chooses to edit or create a policy and then he gets
     * redirected to the Axel policy editor explicitly.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflated = inflater.inflate(R.layout.policy_editor_to_axel, container, false);
        getActivity().setTitle("Policy Editor");
        final Button startDialog = (Button) inflated.findViewById(R.id.whichPolicy);
        startDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PolicyEditor.this.getActivity());
                builder.setTitle("New or Existing?");
                String[] choices = {"Existing...", "New"};
                builder.setItems(choices, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            Intent intent = new Intent(PolicyEditor.this.getActivity(), FileChooser.class);
                            intent.putExtra(Constants.EXTENSION, Constants.INPUT_XML);
                            startActivityForResult(intent, filePickingRequest);
                        } else {
                            startNewActivity(PolicyEditor.this.getActivity(), Constants.AXEL_PACKAGE_NAME);
                        }
                    }
                }).show();
            }
        });
        return inflated;
    }

    /**
     * This method is called for creating a new file and uses #createBaseIntent
     *
     * @param context:     Context to start the app from
     * @param packageName: the package name of the app to be started
     */
    public void startNewActivity(Context context, String packageName) {
        Intent intent = createBaseIntent(context, packageName);
        context.startActivity(intent);
    }

    /**
     * This method is called for editing an existing file and uses #createBaseIntent
     *
     * @param context:     Context to start the app from
     * @param packageName: the package name of the app to be started
     * @param filePath:    the file path of the file we want ot edit
     */
    public void startNewActivity(Context context, String packageName, Uri filePath) {
        Intent intent = createBaseIntent(context, packageName);
        // Only set action to edit if it is not a PlayStore intent
        if (intent.getData() == null) {
            intent.setData(filePath);
            intent.setAction(Intent.ACTION_EDIT);
            context.startActivity(intent);
        }
    }

    /**
     * Method common to both #startNewActivity methods.
     * It creates an intent which will look for the XML Editor and if you do not have it,
     * will propose you to download it.
     *
     * @param context:     The context to start the app from.
     * @param packageName: the package name of the app to start
     * @return: an Intent to Google Play Store or to XML Editor
     */
    @NonNull
    private Intent createBaseIntent(Context context, String packageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent == null) {
            // Bring user to the market or let them choose an app?
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=" + packageName));
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    /**
     * When the user picks his file, we come back in this method.
     * Inherited from the android system, it allows us to retrieve the result of
     * the user action which happened outside our app.
     * @param requestCode: the integer we started the external activity with
     * @param resultCode: Was the external activity successful
     * @param data: the data the user picked
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // If everything is ok (the user picked an XML file), we send him to the axel app.
        if (requestCode == filePickingRequest && resultCode == Activity.RESULT_OK) {
            String absolutePath = data.getStringExtra(Constants.ABSOLUTE_PATH);
            Uri toSend = Uri.parse(new File(absolutePath).toString());
            startNewActivity(getActivity(), Constants.AXEL_PACKAGE_NAME, toSend);
        }
    }
}
