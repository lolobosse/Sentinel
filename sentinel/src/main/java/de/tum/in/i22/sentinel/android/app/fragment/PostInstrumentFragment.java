package de.tum.in.i22.sentinel.android.app.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import de.tum.in.i22.sentinel.android.app.R;
import de.tum.in.i22.sentinel.android.app.package_getter.PackageGetter;
import de.tum.in.i22.sentinel.android.app.playstore.PlayStoreFocusable;

/**
 * Created by Moderbord on 2016-01-18.
 */
public class PostInstrumentFragment extends Fragment implements PackageGetter.Callback {

    ListView listView;
    HughAdapter adapter;
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
                adapter = new HughAdapter(getActivity(), packages);
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

    private class HughAdapter extends BaseAdapter{

        private Context context;
        private List<PackageGetter.Package> data;

        public HughAdapter(Context c, List<PackageGetter.Package> packages){
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
            v = inflater.inflate(R.layout.app_list_row, null);

            PackageGetter.Package packageItem = (PackageGetter.Package)getItem(position);

            if (packageItem.isInstrumented()){
                // TODO Put code here to only display instrumented applications in the listView
            }

            TextView rowItemTitle = (TextView) v.findViewById(R.id.rowTextTitle);
            TextView rowItemPath = (TextView) v.findViewById(R.id.rowTextPath);
            ImageView rowItemIcon = (ImageView) v.findViewById(R.id.rowIcon);

            rowItemTitle.setText(((PackageGetter.Package)getItem(position)).getName());
            rowItemPath.setText(((PackageGetter.Package)getItem(position)).getPath());
            rowItemIcon.setImageDrawable(((PackageGetter.Package) getItem(position)).getPackagePicture());

            return v;
        }
    }

}
