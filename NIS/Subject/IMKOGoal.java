package com.kekland.enis.NIS.Subject;

import android.util.Log;

import org.json.JSONObject;

/**
 * Created by Admin on 28/07/2017.
 */

public class IMKOGoal {
    public String Name;
    public String Description;
    public String Date;
    public String Achieved;
    public GoalStatus Status;
    public String Comment;

    public String GroupIndex;
    public String GroupName;

    public IMKOGoal(JSONObject goal) {
        try {
            Name = goal.getString("Name");
            Description = goal.getString("Description");
            Date = goal.getString("Changed");
            if(Date.equals("null")) {
                Date = "";
            }
            String ach = goal.getString("Value");
            Achieved = ach;
            Comment = goal.getString("Comment");

            GroupIndex = goal.getString("GroupIndex");
            GroupName = goal.getString("GroupName");

            if(ach.equals("Достиг") || ach.equals("Жетті") || ach.equals("Achieved")) {
                Status = GoalStatus.Achieved;
            }
            else if(ach.equals("Стремится") || ach.equals("Тырысады") || ach.equals("Working towards")) {
                Status = GoalStatus.WorkingTowards;
            }
            else {
                Status = GoalStatus.NotAssessed;
            }

            if(Date.length() != 0) {
                Date = Date.substring(8, 10) + "/" + Date.substring(5, 7) + "/" + Date.substring(0, 4);
            }
        }
        catch(Exception e) {
            Log.e("Error.JSON", e.toString());
        }
    }
    //"" - None
    //"Achieved"
    //"Declined"
}