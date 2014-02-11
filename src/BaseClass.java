import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BaseClass {

    private static final int NUM_COL = 7, NUM_ROW = 7;
    static long startTime, endTime;

    public static void main(String[] args) throws IOException {
//        Input input = new Input();
//        pauseForInput(input);

        List<Integer> playedNotes = new ArrayList<Integer>();
        Random random = new Random();
        for (int j = 0; j < 20; j++) {
            playedNotes.add(GenChordProgression.keyMatrix[0][random.nextInt(7)]);
            System.out.print(playedNotes.get(j) + " ");
        }

        System.out.println();

        long startTime = System.currentTimeMillis();

        double[][] transitionArray = new double[NUM_ROW][NUM_COL];

        for (int i = 0; i < playedNotes.size() - 1; i++) {
            transitionArray[findInArray(GenChordProgression.keyMatrix[0], playedNotes.get(i))][
                    findInArray(GenChordProgression.keyMatrix[0], playedNotes.get(i + 1))]++;
        }

        for (int i = 0; i < NUM_ROW; i++) {
            int sum = 0;
            for (int j = 0; j < NUM_COL; j++) {
                sum += transitionArray[i][j];
            }
            if (sum != 0) {
                for (int j = 0; j < NUM_COL; j++) {
                    transitionArray[i][j] = transitionArray[i][j] / sum;
                }
            }
        }

        long endTime = System.currentTimeMillis();

        System.out.println();
        System.out.println("Took " + (endTime-startTime)/1000.0 + " seconds");
        System.out.println();

        for (int i = 0; i < NUM_ROW; i++) {
            for (int j = 0; j < NUM_COL; j++) {
                System.out.print(transitionArray[i][j] + "  ");
            }
            System.out.println();
        }
        System.out.println();

    }

    // Once the user presses enter, the application stops running.
    private static void pauseForInput(Input input) {
        try {
            System.out.print("Press 'Enter' to exit application.");
            System.in.read();
            input.closeDevices();
            System.out.println("Size of playedNotes: " + input.playedNotes.size());
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int findInArray(int[] array, int value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return -1;
    }
}
