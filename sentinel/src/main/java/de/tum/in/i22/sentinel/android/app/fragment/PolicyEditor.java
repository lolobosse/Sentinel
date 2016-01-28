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

import ae.com.sun.xml.bind.v2.runtime.reflect.opt.Const;
import de.tum.in.i22.sentinel.android.app.Constants;
import de.tum.in.i22.sentinel.android.app.R;
import de.tum.in.i22.sentinel.android.app.file_explorer.FileChooser;

/**
 * Created by laurentmeyer on 21/01/16.
 */
public class PolicyEditor extends Fragment {

    private int filePickingRequest = 0;

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

    public void startNewActivity(Context context, String packageName) {
        Intent intent = createBaseIntent(context, packageName);
        context.startActivity(intent);
    }

    public void startNewActivity(Context context, String packageName, Uri filePath) {
        Intent intent = createBaseIntent(context, packageName);
        intent.setData(filePath);
        intent.setAction(Intent.ACTION_EDIT);
        context.startActivity(intent);
    }

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == filePickingRequest && resultCode == Activity.RESULT_OK) {
            String absolutePath = data.getStringExtra(Constants.ABSOLUTE_PATH);
            Uri toSend = Uri.parse(new File(absolutePath).toString());
            startNewActivity(getActivity(), Constants.AXEL_PACKAGE_NAME, toSend);
        }
    }
}
