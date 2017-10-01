package com.kekland.enis.NIS.Subject;

import android.text.Html;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 15/08/2017.
 */

public class Homework {
    public String Date;
    public String Description;

    public List<String> Files;
    public List<String> FilesNames;
    public Boolean hasFiles;

    public Homework(JSONObject homework) {
        try {
            Date = homework.getString("date");
            Description = homework.getString("description");

            Description = Description.replace("%u", "&#x");
            Description = Html.fromHtml(Description).toString();

            Description = Description.replace("%20", " ");

            for(int i = 0; i < Description.length(); i++) {
                if(Description.charAt(i) == '%') {
                    String ln = Description.substring(i, i + 3);
                    ln = ln.replace("%", "&#x");
                    ln = Html.fromHtml(ln).toString();
                    Description = Description.substring(0, i) + ln + Description.substring(i + 3, Description.length());
                }
            }

            int begin = 0;
            int end = 0;
            for(int i = 0; i < Description.length(); i++) {
                if(Description.charAt(i) == '<') {
                    begin = i;
                }
                else if(Description.charAt(i) == '>') {
                    end = i;
                    Description = Description.substring(0, begin) + Description.substring(end + 1, Description.length());
                    i = 0;
                }
            }
            Description = Description.replace("&nbsp;", " ");

            Files = new ArrayList<>();
            FilesNames = new ArrayList<>();
            JSONArray files = homework.getJSONArray("files");
            for(int i = 0; i < files.length(); i++) {
                String file = files.getString(i);
                Files.add(file);
                FilesNames.add(file.substring(file.indexOf('.') + 1, file.length()));
            }
            hasFiles = (files.length() != 0);

            if(Date.length() != 0) {
                Date = Date.substring(8, 10) + "/" + Date.substring(5, 7) + "/" + Date.substring(0, 4);
            }
        }
        catch(Exception e) {
            Log.e("Homework.JSON", e.toString());
        }
    }
}
