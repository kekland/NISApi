package com.kekland.enis.NIS.Subject;

import com.kekland.enis.Utilities.DebugLog;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Gulnar on 17.09.2017.
 */

public class JKOLesson {
    public String Name = "";

    public String Id = "";
    public String JournalId = "";


    public String SArID = "";
    public String SOpID = "";


    public String Percent = "";
    public String Mark = "";

    public Integer Period;
    public JKOLesson(JSONObject lesson, Integer period) {

        try {
            Period = period;
            Name = lesson.getString("Name");
            Id = lesson.getString("Id");
            JournalId = lesson.getString("JournalId");
            Mark = lesson.getString("Mark");
            Percent = lesson.getString("Score") + "%";

            JSONArray Evaluations = lesson.getJSONArray("Evalutions");
            if (Evaluations.length() != 0) {
                SArID = (Evaluations.getJSONObject(0)).getString("Id");
                SOpID = (Evaluations.getJSONObject(1)).getString("Id");
            }
            if (Mark.equals("0")) {
                Mark = "N/A";
            }
        }
        catch(Exception e) {
            DebugLog.e(e.getMessage());
        }
    }
}
