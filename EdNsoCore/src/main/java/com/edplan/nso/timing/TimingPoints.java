package com.edplan.nso.timing;

import java.util.ArrayList;

import com.edplan.framework.utils.U;

public class TimingPoints {
    private ArrayList<TimingPoint> timings;

    public TimingPoints() {
        timings = new ArrayList<TimingPoint>();
    }

    public void addTimingPoint(TimingPoint t) {
        timings.add(t);
    }

    public ArrayList<TimingPoint> getTimingPointList() {
        return timings;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        for (TimingPoint t : timings) {
            sb.append(t.toString()).append(U.NEXT_LINE);
        }
        return sb.toString();
    }
}
