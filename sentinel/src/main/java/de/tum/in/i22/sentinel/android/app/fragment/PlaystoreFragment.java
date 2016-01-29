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

/**
 * This class is responsible for showing the available apps on the server that are instrumented
 * and can be downloaded and installed
 */
public class PlayStoreFragment extends Fragment {

    GridView gridView;
    GridAdapter adapter;
    LayoutInflater inflater;

    /**
     * Creates the view by inflating the layout and starting the network requests which will fill the view.
     * @param inflater: inflates the XML layout
     * @param container: the container of the view
     * @param savedInstanceState: not used
     * @return the view of this fragment
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.playstore_layout, container, false);
        getActivity().setTitle("PlayStore");
        this.inflater = inflater;
        gridView = (GridView) view.findViewById(R.id.pictureGrid);

        // If the user is interested by an app, he clicks it and we start another detail Fragment
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

        // We use our #VolleyClient and we just define some anonymous listener to be able to
        // populate the view depending on the server response
        VolleyClient.getInstance(getActivity()).requestAllMetaData(new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                List<ServerPackageInformation> serverList = new ArrayList<>();
                try {
                    JSONArray metadata = response.getJSONArray("metadata");
                    // We use the automatic parser from Google to save dev time (however we loose flexibility)
                    Gson g = new Gson();
                    for (int i = 0; i < metadata.length(); i++) {
                        String jo = metadata.getJSONObject(i).toString();
                        ServerPackageInformation spi = g.fromJson(jo, ServerPackageInformation.class);
                        serverList.add(spi);
                    }
                    // We create the adapter and fill it with all elements we just retrieved
                    adapter = new GridAdapter(serverList);
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

    /**
     * This class is the adapter of the view, it manage the appearance of the cells in the view
     */
    private class GridAdapter extends BaseAdapter {
        private List<ServerPackageInformation> data;

        public GridAdapter(List<ServerPackageInformation> packages) {
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

            // Not optimized
            v = inflater.inflate(R.layout.playstore_gridview, null);

            ImageView packageView = (ImageView) v.findViewById(R.id.packageView);
            TextView packageName = (TextView) v.findViewById(R.id.packageName);

            packageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            packageView.setPadding(8, 8, 8, 8);

            // Loads the image from the internet, following the link the server gave
            Picasso.with(getActivity()).load(((ServerPackageInformation) getItem(position)).logoUrl).into(packageView);

            packageName.setText(((ServerPackageInformation) getItem(position)).appName);

            return v;
        }
    }

    /**
     * We basically find that getter and setter boring and we do Python style!
     */
    public static class ServerPackageInformation {
        public String downloadUrl, logoUrl, appName, packageName, summary, description, license, appCategory, webLink, sourceCodeLink, marketVersion, sha256hash, sdkVersion, permissions, features;
        public int size;
    }

}

