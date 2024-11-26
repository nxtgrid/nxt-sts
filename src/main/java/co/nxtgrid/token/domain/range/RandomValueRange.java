package co.nxtgrid.token.domain.range;

import co.nxtgrid.token.domain.RandomNo;
import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.exceptions.InvalidNoOfStepsException;
import co.nxtgrid.token.generators.utils.Utils;
import co.nxtgrid.token.miscellaneous.Strings;

import java.util.ArrayList;

public class RandomValueRange implements Range {

    private RandomNo startRandomValueRange ;
    private RandomNo endRandomValueRange ;
    private int noOfSteps;
    private ArrayList<Integer> steps ;
    private int diffPerSteps = 0;

    public RandomValueRange(RandomNo startRandomValueRange, int noOfSteps, RandomNo endRandomValueRange)
        throws InvalidNoOfStepsException {
        setStartRandomValueRange(startRandomValueRange);
        setEndRandomValueRange(endRandomValueRange);
        setNoOfSteps(noOfSteps);
        calculateRNDSteps(startRandomValueRange, endRandomValueRange) ;
    }

    public RandomNo getStartRange () {
        return startRandomValueRange ;
    }

    public RandomNo getEndRange() {
        return endRandomValueRange ;
    }

    public int getNoOfSteps () {
        return noOfSteps ;
    }

    public void setNoOfSteps(int noOfSteps) throws InvalidNoOfStepsException {
        final int MAX_RND_VALUES = 15;
        if (MAX_RND_VALUES % noOfSteps != 0)
            throw new InvalidNoOfStepsException(Strings.INVALID_STEPS_EXCEPTION) ;
        this.noOfSteps = noOfSteps ;
    }

    public int getDiffPerSteps() {
        return diffPerSteps ;
    }

    public void setDiffPerSteps(int diffPerSteps) {
        this.diffPerSteps = diffPerSteps ;
    }

    public RandomNo getStep(int stepCounter) {
        RandomNo currValue = null ;
        try {
            String val = Integer.toBinaryString(steps.get(stepCounter)) ;
            currValue = new RandomNo(new BitString(Utils.convertBitStringToLong(val)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return currValue ;
    }

    public void calculateRNDSteps(RandomNo startRandomValueRange, RandomNo endRandomValueRange) {
        int rangeStart = (int) startRandomValueRange.getBitString().getValue() ;
        int rangeStop = (int) endRandomValueRange.getBitString().getValue() ;
        steps = new ArrayList<>() ;

        if (rangeStart <= 15 && rangeStop <= 15 && rangeStop > rangeStart) {
            int diff = (rangeStop - rangeStart)/noOfSteps ;
            setDiffPerSteps(diff);
            int currValue = rangeStart ;
            for (int randomValueSteps = 0; randomValueSteps < noOfSteps; randomValueSteps++) {
                steps.add(currValue) ;
                currValue += diff ;
            }
        }
    }

    public void setStartRandomValueRange(RandomNo startRandomValueRange) {
        this.startRandomValueRange = startRandomValueRange;
    }

    public void setEndRandomValueRange(RandomNo endRandomValueRange) {
        this.endRandomValueRange = endRandomValueRange;
    }
}
