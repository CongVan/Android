package com.example.musicforlife;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class YoutubeHelper {
    public static final String API_KEY = "AIzaSyACtzmdHydUC5STYTFynjzVmVtMvrGAhtI";
    private static final int MAX_LENGTH = 10;
    private static final String ORDER_RESULT = "relevance";
    private static final String SEARCH_DEFAULT = " lyric";
    private static String mUrlSearch = "https://www.googleapis.com/youtube/v3/search?part=snippet&key=" + API_KEY + "&maxResults=" + MAX_LENGTH + "&order=" + ORDER_RESULT;
    private static Context mContext;
    private static YoutubeHelper mYoutubeHelper;
    private static final String TAG = "YoutubeHelper";

    public static YoutubeHelper getInstance(Context context) {
        if (mContext == null) {
            mContext = context;
            mYoutubeHelper = new YoutubeHelper();
        }
        return mYoutubeHelper;
    }

    public void searchVideoLyric(String songName, Response.Listener<JSONObject> onResponse, Response.ErrorListener onErrorResponse) {
        RequestQueue queue = Volley.newRequestQueue(mContext);
        mUrlSearch += "&q=" + songName + SEARCH_DEFAULT;
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, mUrlSearch, null, onResponse, onErrorResponse);
        queue.add(objectRequest);
    }

}
