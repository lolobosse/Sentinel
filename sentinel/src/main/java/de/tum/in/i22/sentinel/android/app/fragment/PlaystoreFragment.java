package de.tum.in.i22.sentinel.android.app.fragment;

import android.content.Context;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.tum.in.i22.sentinel.android.app.R;
import de.tum.in.i22.sentinel.android.app.package_getter.PackageGetter;

/**
 * Created by Moderbord on 2016-01-13.
 */

public class PlaystoreFragment extends Fragment implements PackageGetter.Callback {

    GridView gridView;
    List<PackageGetter.Package> packages;
    ImageAdapter adapter;
    OnPackageChosen callback;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.playstore_layout, container, false);
        gridView = (GridView) view.findViewById(R.id.pictureGrid);

        packages = new ArrayList<>();

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

    }

    @Override
    public void onSuccess(final List<PackageGetter.Package> packages) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                PlaystoreFragment.this.packages = packages;
                adapter = new ImageAdapter(getActivity());
                adapter.notifyDataSetChanged();
                gridView.setAdapter(adapter);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Toast.makeText(getActivity(), i, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    public interface OnPackageChosen {
        void onPackageSet(PackageGetter.Package selectedPackage);
    }

    private class ImageAdapter extends BaseAdapter{
        private Context context;

        public ImageAdapter(Context c){
            context = c;
        }

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return packages.get(position % packages.size());
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(context);
                imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 8, 8, 8);
            } else {
                imageView = (ImageView) convertView;
            }

            imageView.setImageDrawable(((PackageGetter.Package) getItem(position)).getPackagePicture());
            return imageView;
        }
    }

}
