import java.io.IOException;
import java.util.Arrays;

public class BaseClass {

    public static void main(String[] args) throws IOException {
        Input input = new Input();

        Chord chord1 = new Chord(0,0,0); // 0
        int chord2inv = getInversion(4,0,chord1);
        Chord chord2 = new Chord(4,chord2inv,0); // correct inv is 2
        int chord3inv = getInversion(5,0,chord2);
        Chord chord3 = new Chord(5,chord3inv,0); // correct inv is 1
        int chord4inv = getInversion(1,0,chord3);
        Chord chord4 = new Chord(1,0,0); //0
        System.out.println(chord2inv + " " + chord3inv + " " + chord4inv);

        Output.playChord(chord1);
        Output.delay(.5);
        Output.playChord(chord2);
        Output.delay(.5);
        Output.playChord(chord3);
        Output.delay(.5);
        Output.playChord(chord4);

        pauseForInput(input);
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

    private static int getInversion(int predictedChordNumber, int key, Chord lastChord) {
        int[] notesInLastChord = GenChordProgression.noteInChord(lastChord.getKey(), lastChord.getChordNumber());

        for (int i = 0; i < notesInLastChord.length - 1; i++) { // Sets acquired notes to 0th inversion
            if (notesInLastChord[i + 1] < notesInLastChord[i]) {
                notesInLastChord[i + 1] += 12;
            }
        }

        int inversion = lastChord.getInversion();

        switch (inversion) {
            case 0:
                break;
            case 1:
                notesInLastChord[2] -= 12;
                break;
            case 2:
                notesInLastChord[2] -= 12;
                notesInLastChord[1] -= 12;
                break;
        }

        Arrays.sort(notesInLastChord);

        int[] notesInPredictedChord = GenChordProgression.noteInChord(key, predictedChordNumber);

        for (int i = 0; i < notesInPredictedChord.length - 1; i++) { // Sets acquired notes to 0th inversion
            if (notesInPredictedChord[i + 1] < notesInPredictedChord[i]) {
                notesInPredictedChord[i + 1] += 12;
            }
        }

        int selectedInversion = 0;
        int[] distance = {0, 0, 0};

        for (int i = 0; i < 3; i++) {
            switch (i) {
                case 0:
                    break;
                case 1:
                    notesInPredictedChord[2] -= 12;
                    break;
                case 2:
                    notesInPredictedChord[2] -= 12;
                    notesInPredictedChord[1] -= 12;
                    break;
            }
            Arrays.sort(notesInPredictedChord);
            for (int j = 0; j < notesInLastChord.length; j++) {
                distance[i] += Math.abs(notesInLastChord[j] - notesInPredictedChord[j]);
            }
        }

        int min = 100;
        for (int i = 0; i < distance.length; i++) {
            if (distance[i] < min) {
                min = distance[i];
                selectedInversion = i;
            }
        }

        return selectedInversion;
    }
}