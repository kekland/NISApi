package com.kekland.enis.NIS;

import android.content.Context;

import com.kekland.enis.NIS.Requests.NISApiAccount;
import com.kekland.enis.NIS.Requests.NISApiMisc;
import com.kekland.enis.NIS.Requests.NISApiSubjects;
import com.kekland.enis.NIS.Subject.IMKOLesson;
import com.kekland.enis.NIS.Subject.JKOLesson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;

import java.util.List;

import cz.msebera.android.httpclient.client.HttpClient;

/**
 * Created by Gulnar on 29.09.2017.
 */

public class NISApi {

    private static Context context;

    private static String DefaultPassword = "Qqwerty1!";

    private static AsyncHttpClient client;

    public static void Init(Context app) {
        context = app;

        client = new AsyncHttpClient();
        PersistentCookieStore cookieStore = new PersistentCookieStore(app);
        client.setCookieStore(cookieStore);
    }

    public static void Login(final String PIN, final String Pass, final String School,
                             final NISApiAccount.LoginListener listener) {
        NISApiAccount.Login(PIN, Pass, School, client, new NISApiAccount.LoginListener() {
            @Override
            public void onStart() {
                listener.onStart();
            }

            @Override
            public void onSuccess() {
                NISData.setPIN(PIN);
                NISData.setPassword(Pass);
                NISData.setSchool(School);

                NISApiAccount.GetRoles(School, client, new NISApiAccount.GetRolesListener() {
                    @Override
                    public void onSuccess(final NISRole selectedRole) {
                        NISData.setRole(selectedRole);

                        NISApiAccount.LoginWithRole(PIN, Pass, School, client, selectedRole, new NISApiAccount.LoginWithRoleListener() {
                            @Override
                            public void onSuccess() {
                                if(selectedRole == NISRole.Parent) {
                                    NISApiAccount.GetChildren(School, client, new NISApiAccount.GetChildrenListener() {
                                        @Override
                                        public void onSuccess(List<NISChild> children) {
                                            NISChild[] Children = new NISChild[children.size()];

                                            for(int i = 0; i < children.size(); i++) {
                                                Children[i] = children.get(i);
                                            }

                                            NISData.setChildren(Children);

                                            listener.onSuccess();
                                        }

                                        @Override
                                        public void onFailure(String message) {
                                            listener.onFailure(message);
                                        }
                                    });
                                }
                                else {
                                    listener.onSuccess();
                                }
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

    public static void CheckCredentials(final String PIN, final String Pass, final String School,
                                        final NISApiAccount.LoginListener listener) {
        NISApiAccount.Login(PIN, Pass, School, client, listener);
    }

    public static void TryToLogin(final String PIN, final String Pass, final String School,
                                  final NISApiAccount.LoginListener listener) {
        listener.onStart();
        CheckCredentials(PIN, Pass, School, new NISApiAccount.LoginListener() {
            @Override
            public void onStart() {
                listener.onStart();
            }

            @Override
            public void onSuccess() {
                Login(PIN, Pass, School, new NISApiAccount.LoginListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess() {
                        listener.onSuccess();
                    }

                    @Override
                    public void onFailure(String message) {
                        listener.onFailure(message);
                    }
                });
            }

            @Override
            public void onFailure(String message) {
                if(!Pass.equals(DefaultPassword)) {
                    CheckCredentials(PIN, DefaultPassword, School, new NISApiAccount.LoginListener() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onSuccess() {
                            Login(PIN, DefaultPassword, School, new NISApiAccount.LoginListener() {
                                @Override
                                public void onStart() {

                                }

                                @Override
                                public void onSuccess() {
                                    ChangePassword(DefaultPassword, Pass, new NISApiAccount.ChangePasswordListener() {
                                        @Override
                                        public void onStart() {

                                        }

                                        @Override
                                        public void onSuccess() {
                                            listener.onSuccess();
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
            }
        });
    }

    public static void GetIMKOSubjects(final NISApiSubjects.IMKOSubjectsListener listener) {
        NISApiSubjects.GetIMKOSubjects(client, NISData.getSelectedChild(), listener);
    }

    public static void GetJKOSubjects(final NISApiSubjects.JKOSubjectsListener listener) {
        NISApiSubjects.GetJKOSubjects(client, NISData.getSelectedChild(), listener);
    }

    public static void GetIMKOGoals(IMKOLesson lesson,
                                    NISApiSubjects.IMKOMarksListener listener) {
        NISApiSubjects.GetIMKOMarks(client, lesson, NISData.getSelectedChild(), listener);
    }

    public static void GetJKOGoals(JKOLesson lesson, NISApiSubjects.JKOMarksListener listener) {
        NISApiSubjects.GetJKOMarks(client, lesson, listener);
    }

    public static void GetUsers() {

    }

    public static void ChangePassword(String OldPassword, String NewPassword,
                                      final NISApiAccount.ChangePasswordListener listener) {
        NISApiAccount.ChangePassword(client, NISData.getSchool(), OldPassword, NewPassword, listener);
    }

    public static void GetPasswordStrength(String password, NISApiMisc.PassStrengthListener listener) {
        NISApiMisc.GetPasswordStrength(client, password, listener);
    }

}
