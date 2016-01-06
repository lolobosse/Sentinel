package de.tum.in.i22.sentinel.android.app.package_getter;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.tum.in.i22.sentinel.android.app.R;

/**
 * Created by laurentmeyer on 31/12/15.
 */
public class AppPickerDialog extends Dialog implements PackageGetter.Callback {

    ListView lv;
    TextView tv;
    Button b;
    ProgressBar pb;

    List<PackageGetter.Package> packages;
    AllApkAdapter adapter;

    OnPackageChosen callback;

    public AppPickerDialog(Context context, OnPackageChosen callback) {
        super(context);
        this.callback = callback;
        init();
    }

    private void init(){
        packages = new ArrayList<>();
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.app_picker_dialog_layout, null);

        lv = (ListView) v.findViewById(R.id.allAppList);
        tv = (TextView)v.findViewById(R.id.pickFromFile);
        b = (Button) v.findViewById(R.id.pickButton);
        pb = (ProgressBar) v.findViewById(R.id.progress);

        adapter = new AllApkAdapter();
        lv.setAdapter(adapter);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Your code to pick here
            }
        });
        b.setVisibility(View.INVISIBLE);
        tv.setVisibility(View.INVISIBLE);
        lv.setVisibility(View.INVISIBLE);
        pb.setVisibility(View.VISIBLE);
        setContentView(v);
        Thread t = new Thread(){
            @Override
            public void run() {
                PackageGetter.getPackages(AppPickerDialog.this);
            }
        };
        t.start();
    }

    @Override
    public void onError(Exception e) {
        dismiss();
    }

    @Override
    public void onSuccess(final List<PackageGetter.Package> packages) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                AppPickerDialog.this.packages = packages;
                adapter.notifyDataSetChanged();
                lv.setVisibility(View.VISIBLE);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if (callback != null){
                            callback.onPackageSet(packages.get(i));
                        }
                    }
                });
                tv.setVisibility(View.VISIBLE);
                b.setVisibility(View.VISIBLE);
                pb.setVisibility(View.GONE);
            }
        });
    }

    private class AllApkAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return packages.size();
        }

        @Override
        public Object getItem(int i) {
            return packages.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            // TODO: Limit to third party
            TextView t = new TextView(getContext());
            t.setText(getItem(i).toString());
            return t;
        }
    }

    public interface OnPackageChosen{
        void onPackageSet(PackageGetter.Package selectedPackage);
    }
}
