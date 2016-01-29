package de.tum.in.i22.sentinel.android.app.backend;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by laurentmeyer on 24/01/16.
 * This class is used to communicated with the JSON interface of the play store on the server
 */
public class VolleyClient {

    // Basic Volley Request Queue
    private RequestQueue mRequestQueue;

    // Singleton pattern
    private static VolleyClient instance;

    public static VolleyClient getInstance(Context c){
        if (instance == null){
            instance = new VolleyClient(c);
        }
        return instance;
    }

    /**
     * Creates a volley client with all default parameters: we do not need some custom cache or
     * optimized HTTP Stack yet.
     * @param c: The {@see android.content.Context} to pass to {@see com.android.volley.toolbox.Volley}
     */
    private VolleyClient(Context c){
        // Default
        mRequestQueue = Volley.newRequestQueue(c);
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    // TODO: Extract constants
    /**
     * Creates the request which will populate the PlayStore view.
     * @param listener: Called if success {@see com.android.volley.Response.Listener}
     * @param errorListener: Called if error {@see com.android.volley.Response.ErrorListener}
     */
    public void requestAllMetaData(Response.Listener listener, Response.ErrorListener errorListener){
        JsonObjectRequest request = new JsonObjectRequest("http://lapbroyg58.informatik.tu-muenchen.de:443/metadata/instrumented", listener, errorListener);
        mRequestQueue.add(request);
    }
}
