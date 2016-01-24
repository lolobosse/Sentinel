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
public class PostInstrumentFragment extends Fragment implements PackageGetter.Callback {

    ListView listView;
    RowPackageAdapter adapter;
    LayoutInflater inflater;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.post_instrument_fragment, container, false);
        this.inflater = inflater;
        listView = (ListView) view.findViewById(R.id.post_instrument_listView);

        Thread t = new Thread(){
            @Override
            public void run() {
                PackageGetter.getPackages(PostInstrumentFragment.this, getActivity());
            }
        };

        t.start();
        return view;
    }

    @Override
    public void onError(Exception e) {
        throw new RuntimeException(e);
    }

    @Override
    public void onSuccess(final List<PackageGetter.Package> packages) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                adapter = new RowPackageAdapter(getActivity(), packages);
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

    private class RowPackageAdapter extends BaseAdapter{

        private Context context;
        private List<PackageGetter.Package> data;

        public RowPackageAdapter(Context c, List<PackageGetter.Package> packages){
            context = c;
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

        @Override
        public View getView(int position, View v, ViewGroup parent) {
            v = inflater.inflate(R.layout.post_app_list_row, null);
            TextView rowItemTitle = (TextView) v.findViewById(R.id.rowTextTitle);
            TextView rowItemPath = (TextView) v.findViewById(R.id.rowTextPath);
            ImageView rowItemIcon = (ImageView) v.findViewById(R.id.rowIcon);
            CheckBox rowCheckBox = (CheckBox) v.findViewById(R.id.rowInstrumentedBox);

            PackageGetter.Package packageItem = (PackageGetter.Package)getItem(position);

            // Retrieves the sharedPreferences of "sentinel" and gets the boolean value stored in
            // packageName from the above packageItem
            SharedPreferences sp = getActivity().getSharedPreferences(Constants.SENTINEL, Context.MODE_PRIVATE);
            boolean isInstrumented = sp.getBoolean(packageItem.getPackageName(), false);

            // Condition: If the package is instrumented the checkbox is checked
            if (isInstrumented){
                rowCheckBox.setChecked(true);
            }
            // Sets name, path, and icon
            rowItemTitle.setText(((PackageGetter.Package)getItem(position)).getName());
            rowItemPath.setText(((PackageGetter.Package)getItem(position)).getPath());
            rowItemIcon.setImageDrawable(((PackageGetter.Package) getItem(position)).getPackagePicture());

            // Returns the row
            return v;
        }
    }

}
