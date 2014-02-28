import java.util.ArrayList;
import java.util.Collections;

public class Timing {

    private static final int SUBSECTIONS = 4;
    private long minTime = 50, maxTime = 2550;
    public static ArrayList<Long> timeDifferentials = new ArrayList<Long>();
    private static ArrayList<Long> boundaries = new ArrayList<Long>();
    private static ArrayList<Range> ranges = new ArrayList<Range>();

    public Timing() {
        for (int i = 0; i <= SUBSECTIONS; i++) {
            boundaries.add(i * (maxTime - minTime) / SUBSECTIONS + minTime);
        }

        for (int i = 0; i < SUBSECTIONS; i++) {
            ranges.add(new Range(boundaries.get(i), boundaries.get(i + 1)));
        }
    }

    public Range determineTime() {
        while (timeDifferentials.size() > 50) {
            timeDifferentials.remove(0);
        }

        ArrayList<Long> temporarySorted = timeDifferentials;
        Collections.sort(temporarySorted);

        for (Range range : ranges) {
            range.clearInRange();
            for (Long differential : temporarySorted) {
                if (range.fitsInRange(differential)) {
                    range.appendDifferential(differential);
                }
            }
        }

        for (Range range : ranges) {
            System.out.print(range.getAmountInRange() + "      ");
        }

        System.out.println();

        long binCount = 0;
        Range selectedRange = new Range(0, 0);

        for (Range range : ranges) {
            if (range.getAmountInRange() > binCount) {
                selectedRange = range;
                binCount = range.getAmountInRange();
            }
        }

        System.out.println("Selected Range: " + ranges.indexOf(selectedRange));

        return selectedRange;
    }

    public int retrieveRangeIndex(Range range) {
        return ranges.indexOf(range);
    }
}
