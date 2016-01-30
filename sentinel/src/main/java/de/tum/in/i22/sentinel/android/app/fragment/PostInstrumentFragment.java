package de.tum.in.i22.sentinel.android.app.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import de.tum.in.i22.sentinel.android.app.Constants;
import de.tum.in.i22.sentinel.android.app.R;
import de.tum.in.i22.sentinel.android.app.package_getter.PackageGetter;

/**
 * Created by Moderbord on 2016-01-18.
 */

/**
 * This fragment is used to display which installed apps have been instrumented
 */
public class PostInstrumentFragment extends Fragment implements PackageGetter.Callback {

    ListView listView;
    RowPackageAdapter adapter;
    LayoutInflater inflater;

    /**
     * Inherited method which will allow us to create the view.
     * We start a new Thread to get all packages installed on the devices
     * @param inflater: inflater is used to inflate the XML
     * @param container: container of the view
     * @param savedInstanceState: not used
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.post_instrument_fragment, container, false);
        getActivity().setTitle("Applications");
        this.inflater = inflater;
        listView = (ListView) view.findViewById(R.id.post_instrument_listView);

        Thread t = new Thread() {
            @Override
            public void run() {
                PackageGetter.getPackages(PostInstrumentFragment.this, getActivity());
            }
        };

        t.start();
        return view;
    }

    /**
     * Triggered when the parsing of the packages is not correctly done
     * Because it makes no sense to continue if an exception happens there, we crash the app.
     * @param e: what happened
     */
    @Override
    public void onError(Exception e) {
        throw new RuntimeException(e);
    }

    /**
     * The parsing was successful, let's display our installed apps!
     * @param packages: which packages have been retrieved
     */
    @Override
    public void onSuccess(final List<PackageGetter.Package> packages) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                adapter = new RowPackageAdapter(packages);
                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        PackageGetter.Package packageItem = (PackageGetter.Package) adapterView.getItemAtPosition(i);

                        Toast.makeText(getActivity(), packageItem.getName(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    /**
     * Very basic adapter to display the apps in a list view.
     */
    private class RowPackageAdapter extends BaseAdapter {

        private List<PackageGetter.Package> data;

        public RowPackageAdapter(List<PackageGetter.Package> packages) {
            data = packages;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position % data.size());
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        /**
         * We display the name of the app, the path of it, the icon and if it's instrumented or not.
         * @param position: the position of the view we wanna build
         * @param v: existing view (for memory optimization)
         * @param parent: the listView containing all these views
         * @return the inflated view
         */
        @Override
        public View getView(int position, View v, ViewGroup parent) {
            v = inflater.inflate(R.layout.post_app_list_row, null);
            TextView rowItemTitle = (TextView) v.findViewById(R.id.rowTextTitle);
            TextView rowItemPath = (TextView) v.findViewById(R.id.rowTextPath);
            ImageView rowItemIcon = (ImageView) v.findViewById(R.id.rowIcon);
            CheckBox rowCheckBox = (CheckBox) v.findViewById(R.id.rowInstrumentedBox);

            PackageGetter.Package packageItem = (PackageGetter.Package) getItem(position);

            // Retrieves the sharedPreferences of "sentinel" and gets the boolean value stored in
            // packageName from the above packageItem
            SharedPreferences sp = getActivity().getSharedPreferences(Constants.SENTINEL, Context.MODE_PRIVATE);
            boolean isInstrumented = sp.getBoolean(packageItem.getPackageName(), false);

            // Condition: If the package is instrumented the checkbox is checked
            if (isInstrumented) {
                rowCheckBox.setChecked(true);
            }
            // Sets name, path, and icon
            rowItemTitle.setText(((PackageGetter.Package) getItem(position)).getName());
            rowItemPath.setText(((PackageGetter.Package) getItem(position)).getPath());
            rowItemIcon.setImageDrawable(((PackageGetter.Package) getItem(position)).getPackagePicture());

            // Returns the row
            return v;
        }
    }

}
