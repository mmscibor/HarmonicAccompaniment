import java.util.ArrayList;

public class Range {
    private long lowerValue, upperValue;
    public ArrayList<Long> differentials = new ArrayList<Long>();

    public Range(long lowerValue, long upperValue) {
        this.lowerValue = lowerValue;
        this.upperValue = upperValue;
    }

    public long getLowerValue() {
        return this.lowerValue;
    }

    public long getUpperValue() {
        return this.upperValue;
    }

    public void setLowerValue(long lowerValue) {
        this.lowerValue = lowerValue;
    }

    public void setUpperValue(long upperValue) {
        this.upperValue = upperValue;
    }

    public void appendDifferential(long differential) {
        differentials.add(differential);
    }

    public boolean fitsInRange(long differential) {
        if (differential > lowerValue && differential < upperValue) {
            return true;
        }
        return false;
    }

    public long getAmountInRange() {
        long sum = 0;
        for (Long differential : differentials) {
            sum += differential;
        }
        return sum;
    }

    public long getAverageTime(){
        return getAmountInRange() / differentials.size();
    }

    public void clearInRange(){
        differentials = new ArrayList<Long>();
    }
}

