package com.kekland.enis.NIS.Subject;

import android.util.Log;

import org.json.JSONObject;

/**
 * Created by Admin on 17/08/2017.
 */

public class JKOGoal {
    public String Name;

    public String f1;
    public String f2;
    public String s1;
    public String s2;

    public JKOGoal(JSONObject f, JSONObject s) {
        try {
            Name = f.getString("Name");

            f1 = f.getString("Score");
            if(f1.equals("-1")) {
                f1 = "0";
            }
            f2 = f.getString("MaxScore");

            s1 = s.getString("Score");
            if(s1.equals("-1")) {
                s1 = "0";
            }
            s2 = s.getString("MaxScore");
        }
        catch(Exception e) {
            Log.e("MarkParse", e.toString());
        }

    }

    public Integer GetPercentage() {
        try {
            Integer ff = Integer.parseInt(f1);
            Integer fs = Integer.parseInt(f2);
            Integer sf = Integer.parseInt(s1);
            Integer ss = Integer.parseInt(s2);

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