package de.tum.in.i22.sentinel.android.app.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.tum.in.i22.sentinel.android.app.Constants;
import de.tum.in.i22.sentinel.android.app.R;
import de.tum.in.i22.sentinel.android.app.backend.VolleyClient;
import de.tum.in.i22.sentinel.android.app.playstore.PlayStoreDetail;

/**
 * Created by Moderbord on 2016-01-13.
 */

public class PlaystoreFragment extends Fragment{

    GridView gridView;
    GridAdapter adapter;
    LayoutInflater inflater;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.playstore_layout, container, false);
        this.inflater = inflater;
        gridView = (GridView) view.findViewById(R.id.pictureGrid);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Temp variables for testing
                ServerPackageInformation packageItem = (ServerPackageInformation) adapterView.getItemAtPosition(i);

                String logo = packageItem.logoUrl;

                Intent intent = new Intent(getActivity(), PlayStoreDetail.class);
                intent.putExtra(Constants.PACKAGE_TEXT_PLAY_STORE_DETAIL, packageItem.appName);
                intent.putExtra(Constants.PACKAGE_IMAGE_PLAY_STORE_DETAIL, logo);
                startActivity(intent);
            }
        });

        VolleyClient.getInstance(getActivity()).requestAllMetaData(new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                List<ServerPackageInformation> serverList = new ArrayList<>();
                try {
                    JSONArray metadata = response.getJSONArray("metadata");
                    for (int i = 0; i < metadata.length(); i++) {
                        JSONObject jo = metadata.getJSONObject(i);
                        String hash = jo.getString("hash");
                        String logo = jo.getString("logoUrl");
                        logo = logo.replace("http://lapbroyg58.informatik.tu-muenchen.de:80/", "http://lapbroyg58.informatik.tu-muenchen.de:443/");
                        logo = logo.concat(".png");
                        Log.d("PlaystoreFragment", logo);
                        String appName = jo.getString("appName");
                        String packageName = jo.getString("packageName");
                        double size = jo.getDouble("sizeInBytes");
                        ServerPackageInformation newPackage = new ServerPackageInformation(hash, logo, appName, packageName, size);
                        serverList.add(newPackage);
                    }
                    adapter = new GridAdapter(getActivity(), serverList);
                    gridView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("PlaystoreFragment", "error:" + error);
            }
        });

        return view;
    }

    private class GridAdapter extends BaseAdapter{
        private Context context;
        private List<ServerPackageInformation> data;

        public GridAdapter(Context c, List<ServerPackageInformation> packages){
            context = c;
            data = packages;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
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
            Picasso.with(getActivity()).load(((ServerPackageInformation) getItem(position)).logoUrl);

            packageName.setText(((ServerPackageInformation) getItem(position)).appName);

            return v;
        }
    }

    private class ServerPackageInformation{
        String hash, logoUrl, appName, packageName;
        double size;

        public ServerPackageInformation(String hash, String logoUrl, String appName, String packageName, double size) {
            this.hash = hash;
            this.logoUrl = logoUrl;
            this.appName = appName;
            this.packageName = packageName;
            this.size = size;
        }
    }

}

