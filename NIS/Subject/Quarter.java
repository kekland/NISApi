package com.kekland.enis.NIS.Subject;

import android.util.Log;


import com.kekland.enis.NIS.NISDiary;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gulnar on 17.09.2017.
 */

public class Quarter {
    public Integer PeriodID;

    public List<IMKOLesson> IMKOSubjects;
    public List<JKOLesson> JKOSubjects;

    public NISDiary Type;

    public Quarter(NISDiary type, Integer periodID, JSONArray Subjects) {
        Type = type;
        PeriodID = periodID;
        try {
            if (type == NISDiary.IMKO) {
                IMKOSubjects = new ArrayList<>();
                for (int i = 0; i < Subjects.length(); i++) {
                    IMKOSubjects.add(new IMKOLesson(Subjects.getJSONObject(i), PeriodID));
                }
            }
            else if(type == NISDiary.JKO) {
                JKOSubjects = new ArrayList<>();
                for (int i = 0; i < Subjects.length(); i++) {
                    JKOSubjects.add(new JKOLesson(Subjects.getJSONObject(i), PeriodID));
                }
            }
        }
        catch(Exception e) {
            Log.e("Quarter.Create", e.getMessage());
        }
    }
}
