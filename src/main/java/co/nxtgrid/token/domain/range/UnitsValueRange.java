package co.nxtgrid.token.domain.range;

import java.util.ArrayList;

public class UnitsValueRange implements Range {

    private double startUnitsRange ;
    private int noOfSteps;
    private double endUnitsRange ;
    private ArrayList<Double> steps ;

    public UnitsValueRange (double startUnits, int noOfSteps, double endUnits) {
        setStartUnitsRange(startUnits);
        setNoOfSteps(noOfSteps);
        setEndUnitsRange(endUnits);
        calculateSteps(startUnits, endUnits, noOfSteps) ;
    }

    public Double getStartRange () {
        return startUnitsRange ;
    }

    public void setStartUnitsRange(double startUnitsRange) {
        this.startUnitsRange = startUnitsRange ;
    }

    public Double getEndRange() {
        return endUnitsRange ;
    }

    public void setEndUnitsRange (double endUnitsRange) {
        this.endUnitsRange = endUnitsRange ;
    }

    public int getNoOfSteps () {
        return noOfSteps ;
    }

    public void setNoOfSteps(int noOfSteps) {
        this.noOfSteps = noOfSteps ;
    }

    public Double getStep(int stepCounter) {
        final String INVALID_INDEX_EXCEPTION = "invalid step index supplied" ;
        if (stepCounter < noOfSteps) {
            return steps.get(stepCounter) ;
        } else
            throw new IndexOutOfBoundsException(INVALID_INDEX_EXCEPTION) ;
    }

    private void calculateSteps(double startUnits, double endUnits, int noOfSteps) {
        steps = new ArrayList<>() ;
        double diff = (endUnits - startUnits)/(noOfSteps + 1);
        double currEvaluatedVal = startUnits ;
        for (int stepsCounter = 0 ;  stepsCounter < noOfSteps; stepsCounter++) {
            steps.add(currEvaluatedVal) ;
            currEvaluatedVal += diff ;
        }
    }
}

