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
import com.google.gson.Gson;
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

                // We pass the whole package to the detail activity and we serialize via Gson
                ServerPackageInformation packageItem = (ServerPackageInformation) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getActivity(), PlayStoreDetail.class);
                intent.putExtra(Constants.DETAILS_TO_DISPLAY_KEY, new Gson().toJson(packageItem, ServerPackageInformation.class));
                startActivity(intent);
            }
        });

        VolleyClient.getInstance(getActivity()).requestAllMetaData(new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                List<ServerPackageInformation> serverList = new ArrayList<>();
                try {
                    JSONArray metadata = response.getJSONArray("metadata");
                    Gson g = new Gson();
                    for (int i = 0; i < metadata.length(); i++) {
                        String jo = metadata.getJSONObject(i).toString();
                        ServerPackageInformation spi = g.fromJson(jo, ServerPackageInformation.class);
                        serverList.add(spi);
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
            Picasso.with(getActivity()).load(((ServerPackageInformation) getItem(position)).logoUrl).into(packageView);

            packageName.setText(((ServerPackageInformation) getItem(position)).appName);

            return v;
        }
    }

    /**
     * We basically find that getter and setter boring and we do Python style!
     */
    public static class ServerPackageInformation{
        public String downloadUrl, logoUrl, appName, packageName, summary, description, license, appCategory, webLink, sourceCodeLink, marketVersion, sha256hash, sdkVersion, permissions, features;
        public int size;
    }

}

