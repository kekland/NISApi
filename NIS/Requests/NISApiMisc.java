package com.kekland.enis.NIS.Requests;

import com.kekland.enis.NIS.NISChild;
import com.kekland.enis.NIS.NISData;
import com.kekland.enis.NIS.NISRole;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Gulnar on 29.09.2017.
 */

public class NISApiMisc {

    public static void UpdateCookies(final AsyncHttpClient client, final CookieUpdateListener listener) {
        NISApiAccount.Login(NISData.getPIN(), NISData.getPassword(), NISData.getSchool(),
                client, new NISApiAccount.LoginListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess() {
                NISApiAccount.GetRoles(NISData.getSchool(), client, new NISApiAccount.GetRolesListener() {
                    @Override
                    public void onSuccess(final NISRole selectedRole) {
                        NISData.setRole(selectedRole);

                        NISApiAccount.LoginWithRole(NISData.getPIN(), NISData.getPassword(),
                                NISData.getSchool(), client, selectedRole, new NISApiAccount.LoginWithRoleListener() {
                            @Override
                            public void onSuccess() {
                                listener.onSuccess(client);
                            }

                            @Override
                            public void onFailure(String message) {
                                listener.onFailure(message);
                            }
                        });
                    }

                    @Override
                    public void onFailure(String message) {
                        listener.onFailure(message);
                    }
                });
            }

            @Override
            public void onFailure(String message) {
                listener.onFailure(message);
            }
        });
    }

    public interface CookieUpdateListener {
        void onSuccess(AsyncHttpClient client);
        void onFailure(String message);
    }

    public static void GetPasswordStrength(AsyncHttpClient client, String Pass, final PassStrengthListener listener) {
        listener.onStart();
        RequestParams params = new RequestParams("pass", Pass);
        client.post("http://fmalm.nis.edu.kz/Almaty_Fmsh/Account/GetPassStrength",
                params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String text = new String(bytes);
                listener.onSuccess(Integer.parseInt(text));
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                if(bytes == null) {
                    bytes = new byte[0];
                }
                listener.onFailure(new String(bytes));
            }
        });
    }

    public interface PassStrengthListener {
        void onStart();
        void onSuccess(int strength);
        void onFailure(String message);
    }

    public static void GetUsers() {

    }
}
