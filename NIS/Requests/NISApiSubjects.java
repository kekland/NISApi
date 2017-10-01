package com.kekland.enis.NIS.Requests;

import com.kekland.enis.NIS.NISApi;
import com.kekland.enis.NIS.NISApiUtils;
import com.kekland.enis.NIS.NISChild;
import com.kekland.enis.NIS.NISData;
import com.kekland.enis.NIS.NISDiary;
import com.kekland.enis.NIS.Subject.GoalsData;
import com.kekland.enis.NIS.Subject.IMKOLesson;
import com.kekland.enis.NIS.Subject.JKOLesson;
import com.kekland.enis.NIS.Subject.Quarter;
import com.kekland.enis.NIS.Subject.SubjectData;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Gulnar on 29.09.2017.
 */

public class NISApiSubjects {
    public static void GetIMKOSubjects(AsyncHttpClient client,
                                       final NISChild child,
                                       final IMKOSubjectsListener listener) {
        NISApiMisc.UpdateCookies(client, new NISApiMisc.CookieUpdateListener() {
            @Override
            public void onSuccess(AsyncHttpClient client) {
                final NISChild Child;

                if(child == null) {
                    Child = new NISChild("", "", "", "");
                }
                else {
                    Child = child;
                }

                listener.onStart();
                final SubjectData data = new SubjectData();

                for(int period = 0; period < 4; period++) {
                    final int fPeriod = period;
                    GetIMKOSubjectsByPeriod(client, period, Child, new IMKOSubjectPeriodListener() {
                        @Override
                        public void onSuccess(Quarter quarter) {
                            data.SetQuarter(fPeriod, quarter);
                            if(data.AllSet()) {
                                listener.onSuccess(data);
                            }
                        }

                        @Override
                        public void onFailure(String message) {
                            listener.onFailure(message);
                        }
                    });
                }
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }
    public interface IMKOSubjectsListener {
        void onStart();
        void onSuccess(SubjectData data);
        void onFailure(String message);
    }

    private static void GetIMKOSubjectsByPeriod(AsyncHttpClient client,
                                                final int period, NISChild child,
                                                final IMKOSubjectPeriodListener listener) {
        RequestParams params = new RequestParams();
        params.put("periodId", period + 1);
        params.put("studentId", child.studentID);

        Requester.Request(client, NISData.getSchool() + "/ImkoDiary/Subjects", params, false,
                new Requester.JSONObjectListener() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    Quarter quarter = new Quarter(NISDiary.IMKO, period, response.getJSONArray("data"));
                    listener.onSuccess(quarter);
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

    private interface IMKOSubjectPeriodListener {
        void onSuccess(Quarter quarter);
        void onFailure(String message);
    }

    public static void GetJKOSubjects(AsyncHttpClient client,
                                      final NISChild child,
                                      final JKOSubjectsListener listener) {
        listener.onStart();
        NISApiMisc.UpdateCookies(client, new NISApiMisc.CookieUpdateListener() {
            @Override
            public void onSuccess(final AsyncHttpClient client) {
                if(child == null) {
                    client.post(NISData.getSchool() + "/JceDiary/JceDiary", null, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            String response = new String(responseBody);
                            NISChild Child = NISApiUtils.GetChildByJSResponse(response);
                            GetAllJKOSubjects(client, Child, listener);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            listener.onFailure("Connection to JCE Diary was aborted");
                        }
                    });
                }
                else {
                    GetAllJKOSubjects(client, child, listener);
                }
            }

            @Override
            public void onFailure(String message) {
                listener.onFailure(message);
            }
        });
    }

    private static void GetAllJKOSubjects(AsyncHttpClient client, NISChild child, final JKOSubjectsListener listener) {
        final SubjectData data = new SubjectData();

        for(int period = 0; period < 4; period++) {
            final int finalPeriod = period;
            GetJKOSubjectsByPeriod(client, child, period, new JKOPeriodListener() {
                @Override
                public void onSuccess(Quarter quarter) {
                    data.SetQuarter(finalPeriod, quarter);
                    if(data.AllSet()) {
                        listener.onSuccess(data);
                    }
                }

                @Override
                public void onFailure(String message) {
                    listener.onFailure(message);
                }
            });
        }
    }

    public interface JKOSubjectsListener {
        void onStart();
        void onSuccess(SubjectData data);
        void onFailure(String message);
    }

    private static void GetJKOSubjectsByPeriod(final AsyncHttpClient client, NISChild child,
                                               final int period,
                                               final JKOPeriodListener listener) {
        GetJKOLink(client, child, period, new JKOLinkListener() {
            @Override
            public void onSuccess(String link) {
                RequestParams newParams = new RequestParams();
                newParams.put("page", "1");
                newParams.put("start", "0");
                newParams.put("limit", "100");

                Requester.RequestWithReferer(client, NISData.getSchool() + "/Jce/Diary/GetSubjects",
                        newParams, link, new Requester.JSONObjectListener() {
                            @Override
                            public void onSuccess(JSONObject response) {
                                try {
                                    Quarter quarter = new Quarter(NISDiary.JKO, period,
                                            response.getJSONArray("data"));
                                    listener.onSuccess(quarter);
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

            @Override
            public void onFailure(String message) {
                listener.onFailure(message);
            }
        });
    }

    public interface JKOPeriodListener {
        void onSuccess(Quarter quarter);
        void onFailure(String message);
    }

    private static void GetJKOLink(final AsyncHttpClient client, NISChild child,
                                   int period,
                                   final JKOLinkListener listener) {

        RequestParams params = new RequestParams();
        params.put("periodId", period + 1);
        params.put("studentId", child.studentID);
        params.put("klassId", child.classID);

        Requester.Request(client, NISData.getSchool() + "/JCEDiary/GetDiaryURL", params, true, new Requester.JSONObjectListener() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    final String link = response.getString("data");

                    client.post(link, null, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            listener.onSuccess(link);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            listener.onFailure("Error: connection was aborted");
                        }
                    });
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

    private interface JKOLinkListener {
        void onSuccess(String link);
        void onFailure(String message);
    }

    public static void GetIMKOMarks(final AsyncHttpClient client,
                                     final IMKOLesson lesson, final int period, final NISChild child,
                                     final IMKOMarksListener listener) {
        listener.onStart();
        RequestParams params = new RequestParams();
        params.put("periodId", period + 1);
        params.put("subjectId", lesson.id);
        if (child != null) {
            params.put("studentId", child.studentID);
        }

        Requester.Request(client, NISData.getSchool() + "/ImkoDiary/Goals", params, true, new Requester.JSONObjectListener() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONObject data = response.getJSONObject("data");
                    GoalsData gData = new GoalsData(NISDiary.IMKO, data.getJSONArray("goals"),
                            null,  data.getJSONArray("homeworks"));
                    listener.onSuccess(gData);
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

    public interface IMKOMarksListener {
        void onStart();
        void onSuccess(GoalsData data);
        void onFailure(String message);
    }

    public static void GetJKOMarks(final AsyncHttpClient client, final JKOLesson lesson,
                                    final JKOMarksListener listener) {
        listener.onStart();

        RequestParams params = new RequestParams();
        params.put("evalId", lesson.SArID);
        params.put("journalId", lesson.JournalId);

        params.put("page", 1);
        params.put("start", 0);
        params.put("limit", 100);

        Requester.Request(client, NISData.getSchool() + "/Jce/Diary/GetResultByEvalution",
                params, true, new Requester.JSONObjectListener() {
                    @Override
                    public void onSuccess(final JSONObject firstResponse) {
                        RequestParams params = new RequestParams();
                        params.put("evalId", lesson.SOpID);
                        params.put("journalId", lesson.JournalId);

                        params.put("page", 1);
                        params.put("start", 0);
                        params.put("limit", 100);

                        Requester.Request(client, NISData.getSchool() + "/Jce/Diary/GetResultByEvalution",
                                params, false, new Requester.JSONObjectListener() {
                                    @Override
                                    public void onSuccess(JSONObject secondResponse) {
                                        try {

                                            GoalsData data = new GoalsData(NISDiary.JKO,
                                                    firstResponse.getJSONArray("data"),
                                                    secondResponse.getJSONArray("data"),
                                                    null);
                                            listener.onSuccess(data);
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

                    @Override
                    public void onFailure(String message) {
                        listener.onFailure(message);
                    }
                });
    }

    public interface JKOMarksListener {
        void onStart();
        void onSuccess(GoalsData data);
        void onFailure(String message);
    }
}
