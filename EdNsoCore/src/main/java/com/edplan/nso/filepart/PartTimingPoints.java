package com.edplan.nso.filepart;

import com.edplan.nso.OsuFilePart;
import com.edplan.nso.timing.TimingPoints;
import com.edplan.nso.timing.TimingPoint;

public class PartTimingPoints implements OsuFilePart {
    public static final String TAG = "TimingPoints";

    public TimingPoints timingPoints = null;

    public PartTimingPoints() {
        timingPoints = new TimingPoints();
    }

    public void setTimingPoints(TimingPoints timingPoints) {
        this.timingPoints = timingPoints;
    }

    public TimingPoints getTimingPoints() {
        return timingPoints;
    }

    public void addTimingPoint(TimingPoint t) {
        getTimingPoints().addTimingPoint(t);
    }

    @Override
    public String getTag() {

        return TAG;
    }

    @Override
    public String toString() {
        return (timingPoints != null) ? timingPoints.toString() : "{@TimingPoints}";
    }

}
