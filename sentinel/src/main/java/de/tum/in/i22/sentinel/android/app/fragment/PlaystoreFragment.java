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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import de.tum.in.i22.sentinel.android.app.Constants;
import de.tum.in.i22.sentinel.android.app.R;
import de.tum.in.i22.sentinel.android.app.package_getter.PackageGetter;
import de.tum.in.i22.sentinel.android.app.playstore.PlayStoreFocusable;

/**
 * Created by Moderbord on 2016-01-13.
 */

public class PlaystoreFragment extends Fragment implements PackageGetter.Callback {

    GridView gridView;
    GridAdapter adapter;
    LayoutInflater inflater;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.playstore_layout, container, false);
        this.inflater = inflater;
        gridView = (GridView) view.findViewById(R.id.pictureGrid);

        // TODO Retrieve applications from server rather than packages from local device
        Thread t = new Thread(){
            @Override
            public void run() {
                PackageGetter.getPackages(PlaystoreFragment.this, getActivity());
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
                adapter = new GridAdapter(getActivity(), packages);
                gridView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        // Temp variables for testing
                        PackageGetter.Package packageItem = (PackageGetter.Package) adapterView.getItemAtPosition(i);

                        // Converts package drawable to bitmap
                        Drawable drawable = packageItem.getPackagePicture();
                        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();

                        Intent intent = new Intent(getActivity(), PlayStoreFocusable.class);
                        intent.putExtra(Constants.PACKAGE_TEXT_FOCUSED, packageItem.getName());
                        intent.putExtra(Constants.PACKAGE_IMAGE_FOCUSED, bitmap);
                        startActivity(intent);
                    }
                });
            }
        });
    }

    private class GridAdapter extends BaseAdapter{
        private Context context;
        private List<PackageGetter.Package> data;

        public GridAdapter(Context c, List<PackageGetter.Package> packages){
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
            v = inflater.inflate(R.layout.playstore_gridview, null);

            ImageView packageView = (ImageView) v.findViewById(R.id.packageView);
            TextView packageName = (TextView) v.findViewById(R.id.packageName);

            packageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            packageView.setPadding(8, 8, 8, 8);
            packageView.setImageDrawable(((PackageGetter.Package) getItem(position)).getPackagePicture());

            packageName.setText(((PackageGetter.Package)getItem(position)).getName());

            return v;
        }
    }

}

