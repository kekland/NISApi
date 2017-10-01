package com.kekland.enis.NIS.Requests;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Gulnar on 29.09.2017.
 */

public class Requester {
    public interface JSONObjectListener {
        void onSuccess(JSONObject response);
        void onFailure(String message);
    }

    public static void Request(AsyncHttpClient client, final String Link, final RequestParams Params, boolean updateCookies,
                          final JSONObjectListener listener) {
        if(updateCookies) {
            NISApiMisc.UpdateCookies(client, new NISApiMisc.CookieUpdateListener() {
                @Override
                public void onSuccess(AsyncHttpClient client) {
                    Post(client, Link, Params, listener);
                }

                @Override
                public void onFailure(String message) {
                    listener.onFailure(message);
                }
            });
        }
        else {
            Post(client, Link, Params, listener);
        }

    }

    public static void RequestWithReferer(AsyncHttpClient client, final String Link, final RequestParams Params,
                               String referer,
                               final JSONObjectListener listener) {
        client.addHeader("Referer", referer);
        Post(client, Link, Params, listener);
    }

    private static void Post(AsyncHttpClient client, String Link, RequestParams Params,
                             final JSONObjectListener listener) {
        client.post(Link, Params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.has("success")) {
                        if (!response.getBoolean("success")) {
                            listener.onFailure(response.getString("ErrorMessage"));
                        }
                    }
                }
                catch(Exception e) {
                    listener.onFailure(e.getMessage());
                }
                listener.onSuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                listener.onFailure(responseString);
            }
        });
    }
}
