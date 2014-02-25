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
        Collections.sort(timeDifferentials);

        for (Range range : ranges) {
            for (Long differential : timeDifferentials) {
                if (range.fitsInRange(differential)) {
                    range.appendDifferential(differential);
                }
            }
        }

        int binCount = 0;
        Range selectedRange = new Range(0, 0);

        for (Range range : ranges) {
            if (range.getAmountInRange() > binCount) {
                selectedRange = range;
            }
        }

        return selectedRange;
    }
}
