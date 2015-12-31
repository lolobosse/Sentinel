package de.tum.in.i22.sentinel.android.app.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import de.tum.in.i22.sentinel.android.app.R;
import de.tum.in.i22.sentinel.android.app.package_getter.AppPickerDialog;

/**
 * Created by laurentmeyer on 23/12/15.
 */
public class InstrumentFragment extends Fragment{

    public final String SENTINEL = "sentinel";
    static final int PICK_APP_REQUEST = 1;
    private String dataPath = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.instrument_fragment, container, false);

        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.instrumentationLinearLayout);

        // Test button for instrumented apps counter
        Button button = new Button(getActivity());
        linearLayout.addView(button);
        button.setText("Counter++");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getActivity().getSharedPreferences(SENTINEL, 0);
                SharedPreferences.Editor editor = sp.edit();
                int retrievedAmount = sp.getInt("instrumentedApplications", 0);
                int newAmount = retrievedAmount + 1;
                editor.putInt("instrumentedApplications", newAmount);
                editor.commit();
            }
        });
        // End test

        Button pickApplicationB = (Button)view.findViewById(R.id.applicationButton);
        EditText applicationPath = (EditText)view.findViewById(R.id.applicationInput);
        pickApplicationB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                applicationChooser("application/vnd.android.*");
                AppPickerDialog d = new AppPickerDialog(getActivity());
                d.show();

            }
        });
        applicationPath.setText(dataPath);
        return view;
    }

    private void applicationChooser(String fileType){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(fileType);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        Intent sIntent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
        sIntent.putExtra("CONTENT_TYPE", fileType);
        sIntent.addCategory(Intent.CATEGORY_DEFAULT);

        Intent chooseIntent;
        if (getActivity().getPackageManager().resolveActivity(sIntent, 0) != null){
            chooseIntent = Intent.createChooser(sIntent, "Open file");
            chooseIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {
                    intent
            });
        } else {
            chooseIntent = Intent.createChooser(intent, "Open file");
        }

        try {
            startActivityForResult(chooseIntent, PICK_APP_REQUEST);
        } catch (android.content.ActivityNotFoundException ex){
            Toast.makeText(getActivity().getApplicationContext(), "No Suitable File Manager Found", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == PICK_APP_REQUEST){
            if (data != null) {
                String path = data.getDataString();
                Log.d("InstrumentFragment", "First " + path);
                dataPath = path;
            }
            super.onActivityResult(requestCode, resultCode, data);
            Log.d("InstrumentFragment", "Second");

        }
    }

}
