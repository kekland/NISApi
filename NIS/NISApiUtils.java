package com.kekland.enis.NIS;

import java.util.ArrayList;

/**
 * Created by Gulnar on 29.09.2017.
 */

public class NISApiUtils {
    public static ArrayList<String> URLs = new ArrayList<String>()
    {{
        add("http://akt.nis.edu.kz/Aktau");
        add("http://akb.nis.edu.kz/Aktobe");
        add("http://fmalm.nis.edu.kz/Almaty_Fmsh");
        add("http://hbalm.nis.edu.kz/Almaty_HBSH");
        add("http://ast.nis.edu.kz/Astana_FMSH");
        add("http://atr.nis.edu.kz/Atyrau");
        add("http://krg.nis.edu.kz/Karaganda");
        add("http://kt.nis.edu.kz/Kokshetau");
        add("http://kst.nis.edu.kz/Kostanay");
        add("http://kzl.nis.edu.kz/Kyzylorda");
        add("http://pvl.nis.edu.kz/Pavlodar");
        add("http://ptr.nis.edu.kz/Petropavlovsk");
        add("http://sm.nis.edu.kz/Semey_FMSH");
        add("http://tk.nis.edu.kz/Taldykorgan");
        add("http://trz.nis.edu.kz/Taraz");
        add("http://ura.nis.edu.kz/Uralsk");
        add("http://ukk.nis.edu.kz/Oskemen");
        add("http://fmsh.nis.edu.kz/Shymkent_FMSH");
        add("http://hbsh.nis.edu.kz/Shymkent_HBSH");
    }};

    public static ArrayList<String> SchoolNames = new ArrayList<String>()
    {{
        add("Aktau CBD");
        add("Aktobe PhMD");
        add("Almaty PhMD");
        add("Almaty CBD");
        add("Astana PhMD");
        add("Atyrau CBD");
        add("Karaganda CBD");
        add("Kokshetau PhMD");
        add("Kostanay PhMD");
        add("Kyzylorda CBD");
        add("Pavlodar CBD");
        add("Petropavlovsk CBD");
        add("Semey PhMD");
        add("Taldykorgan PhMD");
        add("Taraz PhMD");
        add("Uralsk PhMD");
        add("Ust-Kamenogorsk CBD");
        add("Shymkent PhMD");
        add("Shymkent CBD");
    }};

    public static String ConvertURLToDatabaseURI(String link) {
        return link.substring(link.lastIndexOf('/'));
    }

    public static String GetSchoolNameByLink(String link) {
        return SchoolNames.get(URLs.indexOf(link));
    }

    public static NISChild GetChildByJSResponse(String response) {
        int is = response.indexOf("student: {");
        int b = 0;
        int st =  is;

        String studentId = "";
        for(int i = is; i < response.length(); i++) {
            if(response.charAt(i) == ':') {
                st = i + 1;
            }
            else if(response.charAt(i) == ',') {
                studentId = response.substring(st, i);
                break;
            }
        }
        is = response.indexOf("klass: {");
        String classId = "";
        for(int i = is; i < response.length(); i++) {
            if(response.charAt(i) == ':') {
                st = i + 1;
            }
            else if(response.charAt(i) == ',') {
                classId = response.substring(st, i);
                break;
            }
        }
        return new NISChild("", studentId, "", classId);
    }
}
