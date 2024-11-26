package co.nxtgrid.token.domain.range;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.util.ArrayList;

public class DateRange implements Range {

    public DateTime startTime ;
    public int noOfSteps ;
    public DateTime endTime;
    public ArrayList<Interval> intervals ;
    public long millisPerInterval = 0L;

    public DateRange(DateTime startTime, int noOfSteps, DateTime endTime) {
        setStartRange(startTime);
        setNoOfSteps(noOfSteps);
        setEndRange(endTime) ;
        calculateDurations(startTime, endTime,noOfSteps) ;
    }

    public DateTime getStartRange () {
        return startTime ;
    }

    public void setStartRange(DateTime startTime) {
        this.startTime  = startTime ;
    }

    public int getNoOfSteps() {
        return noOfSteps;
    }

    public void setNoOfSteps(int noOfSteps) {
        this.noOfSteps = noOfSteps ;
    }

    public DateTime getEndRange() {
        return endTime ;
    }

    public void setEndRange (DateTime endTime) {
        this.endTime = endTime ;
    }

    public DateTime getStep(int stepIndex) {
        final String STEP_OVER_BOUNDS_EXCEPTION = "Invalid step index specified" ;
        if (stepIndex < intervals.size()) {
            return intervals.get(stepIndex).getStart();
        } else
            throw new IndexOutOfBoundsException(STEP_OVER_BOUNDS_EXCEPTION) ;
    }

    public ArrayList<Interval> getIntervals() {
        return intervals ;
    }

    public long getMillisPerInterval() {
        return millisPerInterval ;
    }

    public void setMillisPerInterval(long millisPerInterval) {
        this.millisPerInterval = millisPerInterval ;
    }

    private void calculateDurations (DateTime startTime, DateTime endTime, int noOfSteps) {
        ArrayList<Interval> intervals = splitDuration(startTime,endTime, noOfSteps) ;
        setIntervals(intervals) ;
    }

    private ArrayList<Interval> splitDuration(DateTime startTime, DateTime endTime, long noOfIntervals) {
        long noOfMillisPerInterval = (endTime.getMillis() - startTime.getMillis()) / noOfIntervals;
        setMillisPerInterval(noOfMillisPerInterval);
        ArrayList<Interval> result = new ArrayList<>();
        for (int i = 0; i < noOfIntervals; i++) {
            result.add(new Interval(startTime.plus(noOfMillisPerInterval * i), startTime.plus(noOfMillisPerInterval * (i + 1))) );
        }
        return result ;
    }

    private void setIntervals(ArrayList<Interval> intervals) {
        this.intervals = intervals ;
    }
}
