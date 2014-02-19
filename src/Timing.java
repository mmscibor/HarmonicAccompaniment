import java.util.List;

public class Timing {

    static final int MEASURE_LENGTH = 2;

    public static long determineTime(List<Long> timeDifferentials) {
        long averageTime = 0, maxTime = 0;

        if (timeDifferentials.size() > 1) {
            timeDifferentials.remove(0);
        }

        for (long differential : timeDifferentials) {
            averageTime += differential;
            if (differential > maxTime) {
                maxTime = differential;
            }
        }

        averageTime = averageTime / timeDifferentials.size();

        long selectedTime;

        if (maxTime > averageTime * MEASURE_LENGTH) {
            selectedTime = maxTime;
        } else {
            selectedTime = averageTime * MEASURE_LENGTH;
        }

        return selectedTime;
    }
}
