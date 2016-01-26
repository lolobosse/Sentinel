package de.tum.in.i22.sentinel.android.app.backend;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by laurentmeyer on 24/01/16.
 */
public class VolleyClient {

    private RequestQueue mRequestQueue;

    private static VolleyClient instance;

    public static VolleyClient getInstance(Context c){
        if (instance == null){
            instance = new VolleyClient(c);
        }
        return instance;
    }

    private VolleyClient(Context c){
        mRequestQueue = Volley.newRequestQueue(c);
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public void requestAllMetaData(Response.Listener listener, Response.ErrorListener errorListener){
        JsonObjectRequest request = new JsonObjectRequest("http://lapbroyg58.informatik.tu-muenchen.de:443/metadata/instrumented", listener, errorListener);
        mRequestQueue.add(request);
    }
}
