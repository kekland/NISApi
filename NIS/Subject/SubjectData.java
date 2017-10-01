package com.kekland.enis.NIS.Subject;

/**
 * Created by Gulnar on 17.09.2017.
 */

public class SubjectData {
    private Quarter[] quarters;

    public SubjectData() {
        quarters = new Quarter[4];
    }

    public void SetQuarter(int period, Quarter quarter) {
        quarters[period] = quarter;
    }

    public Quarter GetQuarter(int period) {
        return quarters[period];
    }

    public boolean AllSet() {
        for(int i = 0; i < 4; i++) {
            if(quarters[i] == null) {
                return false;
            }
        }
        return true;
    }
}
