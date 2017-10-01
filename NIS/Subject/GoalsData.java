package com.kekland.enis.NIS.Subject;

import android.support.annotation.Nullable;
import android.util.Log;

import com.kekland.enis.NIS.NISDiary;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gulnar on 17.09.2017.
 */

public class GoalsData {
    public Integer PeriodID;

    public List<IMKOGoal> IMKOGoals;
    public List<JKOGoal> JKOGoals;

    public List<Homework> HomeworkList;

    public NISDiary Type;

    public GoalsData(NISDiary type, JSONArray Goals, @Nullable JSONArray JKOSecondGoals, @Nullable JSONArray Homework) {
        Type = type;
        try {
            if (type == NISDiary.IMKO) {
                IMKOGoals = new ArrayList<>();
                for(int i = 0; i < Goals.length(); i++) {
                    IMKOGoals.add(new IMKOGoal(Goals.getJSONObject(i)));
                }
                HomeworkList = new ArrayList<>();
                for(int i = 0; i < Homework.length(); i++) {
                    HomeworkList.add(new Homework(Homework.getJSONObject(i)));
                }
            }
            else if(type == NISDiary.JKO) {
                JKOGoals = new ArrayList<>();
                for(int i = 0; i < Goals.length(); i++) {
                    JKOGoals.add(new JKOGoal(Goals.getJSONObject(i), JKOSecondGoals.getJSONObject(i)));
                }
            }
        }
        catch(Exception e) {
            Log.e("Goals.Create", e.getMessage());
        }
    }
}
