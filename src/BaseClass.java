import java.io.IOException;

public class BaseClass {

    final static int MILLISECONDS = 1000;

    public static void main(String[] args){
        Input input = new Input();

        Output.playNote(60, 100);
        delay(0.25);
        Output.playNote(61, 100);
        delay(0.25);
        Output.playMajorChord(60, 100);
        delay(0.25);
        Output.playMinorChord(60, 100);
        System.out.println("Note played!");

        pauseForInput(input);
    }

    // Input is the number of seconds (INTEGER) to delay.
    private static void delay(double numberSeconds){
        try {
            Thread.sleep((long) (numberSeconds * MILLISECONDS));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Once the user presses enter, the application stops running.
    private static void pauseForInput(Input input){
        try {
            for (int i = 0; i < input.playedNotes.size(); i++){
                System.out.println(input.playedNotes.get(i));
            }
            System.out.print("Press 'Enter' to exit application.");
            System.in.read();
            input.closeDevices();
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
