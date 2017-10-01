package com.kekland.enis.NIS.Subject;


import com.kekland.enis.Utilities.DebugLog;

import org.json.JSONObject;

/**
 * Created by Gulnar on 17.09.2017.
 */

public class IMKOLesson {
    public String Name = "";
    public String Formative = "";
    public String MaxFormative = "";
    public String Summative = "";
    public String MaxSummative = "";
    public String Grade = "";
    public String DateOfChange = "";
    public String id = "";

    public Integer Period;

    public Boolean update = false;

    public IMKOLesson(JSONObject lessonObj, Integer period) {
        try {
            Period = period;
            id = lessonObj.getString("Id");
            Name = lessonObj.getString("Name");
            MaxFormative= lessonObj.getString("Cnt");
            Formative = lessonObj.getString("ApproveCnt");
            Summative = lessonObj.getString("ApproveISA");
            MaxSummative = lessonObj.getString("MaxISA");
            Grade = lessonObj.getString("Period");
            DateOfChange = lessonObj.getString("LastChanged");

            Summative = Summative.substring(0, Summative.indexOf('.'));
            MaxSummative = MaxSummative.substring(0, MaxSummative.indexOf('.'));
            //TODO : Lesson update
            //update = u;
        }
        catch(Exception e) {
            DebugLog.e(e.getMessage());
        }
    }

    public int GetProgress() {
        int f = 0;
        if(Formative != "") {
            f = Integer.parseInt(Formative);
        }
        int mf = Integer.parseInt(MaxFormative);
        return Math.round((float)f / (float)mf * 100f);
    }

    public String GetFormativeString() {
        String ln = "";
        if(Formative == "") {
            ln += "0";
        }
        else {
            ln += Formative;
        }

        ln += " | " + MaxFormative;
        return ln;
    }


    public String GetSummativeString() {
        String ln = "";
        if(Summative == "") {
            ln += "0";
        }
        else {
            ln += Summative;
        }

        ln += " | " + MaxSummative;
        return ln;
    }

    public String GetGrade() {
        String grade = "";
        if(grade.equals("")) {
            grade = "N/A";
        }
        return Grade;
    }
}
