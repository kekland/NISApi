package com.kekland.enis.NIS.Requests;

import android.util.Log;

import com.kekland.enis.NIS.NISChild;
import com.kekland.enis.NIS.NISRole;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


/**
 * Created by Gulnar on 29.09.2017.
 */

public class NISApiAccount {
    public static void Login(String PIN, String Password, String School, AsyncHttpClient client,
                             final LoginListener listener) {
        listener.onStart();
        RequestParams params = new RequestParams();
        params.put("txtUsername", PIN);
        params.put("txtPassword", Password);

        Requester.Request(client, School + "/Account/Login", params, false,
                new Requester.JSONObjectListener() {
            @Override
            public void onSuccess(JSONObject response) {
                listener.onSuccess();
            }

            @Override
            public void onFailure(String message) {
                listener.onFailure(message);
            }
        });
    }

    public interface LoginListener {
        void onStart();
        void onSuccess();
        void onFailure(String message);
    }

    public static void GetRoles(String School, AsyncHttpClient client,
                                final GetRolesListener listener) {
        Requester.Request(client, School + "/Account/GetRoles", null, false,
                new Requester.JSONObjectListener() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        try {
                            NISRole SelectedRole = GetRolesFromResponse(response);

                            if(SelectedRole == NISRole.None) {
                                listener.onFailure("We were not able to find suitable role");
                                return;
                            }

                            listener.onSuccess(SelectedRole);
                        }
                        catch(Exception e) {
                            listener.onFailure(e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(String message) {
                        listener.onFailure(message);
                    }
                });
    }

    public static NISRole GetRolesFromResponse(JSONObject response) throws JSONException {
         JSONArray RolesJSON = response.getJSONArray("listRole");

         NISRole SelectedRole = NISRole.None;
         for (int index = 0; index < RolesJSON.length(); index++) {
             String Role = RolesJSON.getJSONObject(index).getString("value");

             if (Role.equals("Student")) {
                 SelectedRole = NISRole.Student;
                 break;
             } else if (Role.equals("Parent")) {
                 SelectedRole = NISRole.Parent;
                 break;
             }
         }
         return SelectedRole;
    }

    public interface GetRolesListener {
        void onSuccess(NISRole selectedRole);
        void onFailure(String message);
    }

    public static void LoginWithRole(String PIN, String Password, String School, AsyncHttpClient client,
                                     NISRole Role, final LoginWithRoleListener listener) {
        RequestParams params = new RequestParams();
        params.put("role", Role.toString());
        params.put("password", Password);

        Requester.Request(client, School + "Account/LoginWithRole", params, false, new Requester.JSONObjectListener() {
            @Override
            public void onSuccess(JSONObject response) {
                listener.onSuccess();
            }

            @Override
            public void onFailure(String message) {
                listener.onFailure(message);
            }
        });
    }

    public interface LoginWithRoleListener {
        void onSuccess();
        void onFailure(String message);
    }

    public static void ChangePassword(AsyncHttpClient client,
                                      String School,
                                      final String OldPassword, final String NewPassword,
                                      final ChangePasswordListener listener) {
        listener.onStart();
        RequestParams req = new RequestParams();

        req.put("currentPassword", OldPassword);
        req.put("newPassword", NewPassword);
        req.put("confirmPassword", NewPassword);
        req.put("CPWindowPassOldPassword", OldPassword);
        req.put("CPWindowPassNew1", NewPassword);
        req.put("NewPasswordConfirm", NewPassword);

        Requester.Request(client, School + "/Account/ChangeUserPassword", req, true, new Requester.JSONObjectListener() {
            @Override
            public void onSuccess(JSONObject response) {
                //TODO : Change password in firebase
                listener.onSuccess();
            }

            @Override
            public void onFailure(String message) {
                listener.onFailure(message);
            }
        });
    }

    public interface ChangePasswordListener {
        void onStart();
        void onSuccess();
        void onFailure(String message);
    }

    public static void GetChildren(final String School, final AsyncHttpClient client, final GetChildrenListener listener) {
        final List<NISChild> Children = new ArrayList<>();
        Requester.Request(client, School + "/ImkoDiary/Klasses", null, false, new Requester.JSONObjectListener() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONArray classesData = response.getJSONArray("data");
                    final int size = classesData.length() - 1;
                    for (int index = 0; index < classesData.length(); index++) {
                        final JSONObject classData = classesData.getJSONObject(index);
                        String classID = classData.getString("Id");
                        String className = classData.getString("Name");
                        final int finalIndex = index;
                        GetStudentsByClassId(School, className, classID, client, new GetChildrenListener() {
                            @Override
                            public void onSuccess(List<NISChild> children) {
                                Children.addAll(children);
                                if(finalIndex == size) {
                                    listener.onSuccess(Children);
                                }
                            }

                            @Override
                            public void onFailure(String message) {
                                listener.onFailure(message);
                            }
                        });
                    }
                }
                catch(Exception e) {
                    listener.onFailure(e.getMessage());
                }
            }

            @Override
            public void onFailure(String message) {
                listener.onFailure(message);
            }
        });
    }

    public static void GetStudentsByClassId(final String School, final String className, final String classID,
                                            final AsyncHttpClient client,
                                            final GetChildrenListener listener) throws JSONException {
        RequestParams params = new RequestParams();
        params.put("klassId", classID);
        params.put("query", "");

        Requester.Request(client, School + "/ImkoDiary/Students", params, false, new Requester.JSONObjectListener() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    List<NISChild> Children = GetChildrenFromResponse(response, className, classID);
                    listener.onSuccess(Children);
                }
                catch(Exception e) {
                    listener.onFailure(e.getMessage());
                }
            }

            @Override
            public void onFailure(String message) {
                listener.onFailure(message);
            }
        });
    }

    public static List<NISChild> GetChildrenFromResponse(JSONObject response,
                                                  String className, String classID) throws JSONException {
        List<NISChild> Children = new ArrayList<>();
        JSONArray studentsData = response.getJSONArray("data");
        for (int index = 0; index < studentsData.length(); index++) {
            JSONObject studentData = studentsData.getJSONObject(index);
            Children.add(new NISChild(studentData.getString("Name"),
                    studentData.getString("Id"),
                    className,
                    classID));

        }
        return Children;
    }

    public interface GetChildrenListener {
        void onSuccess(List<NISChild> children);
        void onFailure(String message);
    }
}
