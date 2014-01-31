public class BaseClass {

    final static int MILLISECONDS = 1000;

    public static void main(String[] args){
        Output.playNote(60, 100);
        delay(0.25);
        Output.playNote(61, 100);
        delay(0.25);
        Output.playMajorChord(60, 100);
        delay(0.25);
        Output.playMinorChord(60, 100);
        System.out.println("Note played!");
    }

    // Input is the number of seconds (INTEGER) to delay.
    private static void delay(double numberSeconds){
        try {
            Thread.sleep((long) (numberSeconds * MILLISECONDS));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
