package com.kekland.enis.NIS.Subject;

import android.util.Log;

import org.json.JSONObject;

/**
 * Created by Admin on 17/08/2017.
 */

public class JKOGoal {
    public String Name;

    public String sectionScore;
    public String sectionMaxScore;
    public String summativeScore;
    public String summativeMaxScore;

    public JKOGoal(JSONObject f, JSONObject s) {
        try {
            Name = f.getString("Name");

            sectionScore = f.getString("Score");
            if(sectionScore.equals("-1")) {
                sectionScore = "0";
            }
            sectionMaxScore = f.getString("MaxScore");

            summativeScore = s.getString("Score");
            if(summativeScore.equals("-1")) {
                summativeScore = "0";
            }
            summativeMaxScore = s.getString("MaxScore");
        }
        catch(Exception e) {
            Log.e("MarkParse", e.toString());
        }

    }

    public Integer GetPercentage() {
        try {
            Integer ff = Integer.parseInt(sectionScore);
            Integer fs = Integer.parseInt(sectionMaxScore);
            Integer sf = Integer.parseInt(summativeScore);
            Integer ss = Integer.parseInt(summativeMaxScore);

            Double fTotal = ((double)ff / (double)fs) * 100.0;
            Double sTotal = ((double)sf / (double)ss) * 100.0;

            Double Total = (fTotal + sTotal) / 2.0;
            return (int)Math.round(Total);
        }
        catch(Exception e) {
            Log.e("ErrorMarksPercentage", e.toString());
            return 0;
        }
    }
}