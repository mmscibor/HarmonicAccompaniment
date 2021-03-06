import be.ac.ulg.montefiore.run.jahmm.Hmm;
import be.ac.ulg.montefiore.run.jahmm.ObservationInteger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MachineLearning {

    static int previousLength = 0;
    static int[] pointsVectorNotes = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    static List<Integer> playedNotes;
    static Chord lastChord = new Chord(-1, -1, -1);
    private static final int NUM_COL = 7, NUM_ROW = 7, INVERSIONS_TO_CHOOSE = 3;
    static Hmm<ObservationInteger> learntHmm;
    public static int key = 0;

    public static void determineChords(List<Integer> playedNotesList, Hmm<ObservationInteger> hmm) {
        learntHmm = hmm;
        playedNotes = playedNotesList;
        Thread keythread = new Thread() {
            public void run() {
                try {
                    key = getKey();
                } catch(Exception v) {
                    System.out.println(v);
                }
            }
        };

        keythread.start();

        int[] notesInKey = GenChordProgression.keyMatrix[key];

        int currentNote = playedNotes.get(playedNotes.size() - 1) % 12;
        int generalScaleCurrent = findInArray(notesInKey, currentNote);

        if (lastChord.getChordNumber() == -1) {
            if (generalScaleCurrent >= 0) {
                lastChord = new Chord(generalScaleCurrent, 0, key);
            } else {
                return;
            }
        } else {
            int previousChordNumber = lastChord.getChordNumber();

            Random random = new Random();

            ArrayList<Integer> containThisNote = chordsContain(generalScaleCurrent);
            ArrayList<Double> percentageTransitions = new ArrayList<Double>();

            for (Integer containing: containThisNote) {
                System.out.println("i = " + previousChordNumber + "\t\tj = " + containing);
                percentageTransitions.add(random.nextDouble() * learntHmm.getAij(previousChordNumber, containing));
            }

            double max = 0;
            int predictedChord = 0;

            for (int i = 0; i < percentageTransitions.size(); i++) {
                System.out.println("Percentage: " + percentageTransitions.get(i));
                if (percentageTransitions.get(i) > max) {
                    max = percentageTransitions.get(i);
                    predictedChord = i;
                }
            }

            predictedChord = containThisNote.get(predictedChord);

            System.out.println("Selected chord: " + predictedChord);

            int inversion = getInversion(predictedChord, key);
            lastChord.setChord(predictedChord, inversion, key);
        }
    }

    private static int getKey() {
        // Determine the key, based on notes played
        for (int i = previousLength; i < playedNotes.size(); i++) {
            pointsVectorNotes[playedNotes.get(i) % 12]++;
        }

        previousLength = playedNotes.size();
        int[] pointsVectorKey = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        for (int key = 0; key < 12; key++) {
            for (int note = 0; note < GenChordProgression.keyMatrix[key].length; note++) {
                pointsVectorKey[key] += pointsVectorNotes[GenChordProgression.keyMatrix[key][note]];
            }
        }
        return maxInArray(pointsVectorKey);
    }

    private static int maxInArray(double[] array) {
        double max = 0;
        int maxIndex = 0;
        List<Integer> maxList = new ArrayList<Integer>();

        for (int i = 0; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
                maxIndex = i;
            }
        }

        for (int i = 0; i < array.length; i++) {
            if (array[i] == max) {
                maxList.add(i);
            }
        }

        if (maxList.size() == 1) {
            return maxIndex;
        } else {
            Random random = new Random();
            int index = random.nextInt(maxList.size());
            return maxList.get(index);
        }
    }

    private static int maxInArray(int[] array) {
        int max = 0, maxIndex = 0;
        List<Integer> maxList = new ArrayList<Integer>();

        for (int i = 0; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
                maxIndex = i;
            }
        }

        for (int i = 0; i < array.length; i++) {
            if (array[i] == max) {
                maxList.add(i);
            }
        }

        if (maxList.size() == 1) {
            return maxIndex;
        } else {
            int maxNote = 0, maxNoteIndex = 0;
            List<Integer> maxNoteList = new ArrayList<Integer>();

            for (int i = 0; i < pointsVectorNotes.length; i++) {
                if (pointsVectorNotes[i] > maxNote) {
                    maxNote = pointsVectorNotes[i];
                    maxNoteIndex = i;
                }
            }

            for (int i = 0; i < array.length; i++) {
                if (array[i] == max) {
                    maxNoteList.add(i);
                }
            }

            int[] maxKeys = new int[(maxList.size()) * 2];
            for (int i = 0; i < maxList.size(); i++) {
                maxKeys[i] = maxList.get(i);
                maxKeys[i + maxList.size()] = GenChordProgression.keyMatrix[maxList.get(i)][5];
            }

            if (maxNoteList.size() == 1) {
                for (int i = 0; i < maxList.size(); i++) {
                    if (maxNoteIndex == maxKeys[i]) {
                        return maxKeys[i]; // major key
                    }
                }
                for (int i = 0; i < maxList.size(); i++) {
                    if (maxNoteIndex == maxKeys[i + maxList.size()]) {
                        return maxKeys[i]; // minor key -- returns key = 0 for key of A minor
                    }
                }
            } else {
                for (int maxNoteInd : maxNoteList) {
                    for (int i = 0; i < maxList.size(); i++) {
                        if (maxNoteInd == maxKeys[i]) {
                            return maxKeys[i]; // major key
                        }
                    }
                }
                for (int maxNoteInd : maxNoteList) {
                    for (int i = 0; i < maxList.size(); i++) {
                        if (maxNoteInd == maxKeys[i + maxList.size()]) {
                            return maxKeys[i]; // minor key -- returns key = 0 for key of A minor
                        }
                    }
                }
            }
            // if no key stands out as being more likely, the original code to choose one at random is utilized
            Random random = new Random();
            int index = random.nextInt(maxList.size());
            return maxList.get(index);
        }
    }

    private static double[][] getTransitionMatrix(List<Integer> playedNotes, int key) {
        double[][] transitionArray = new double[NUM_ROW][NUM_COL];

        for (int i = 0; i < playedNotes.size() - 1; i++) {
            int row = findInArray(GenChordProgression.keyMatrix[key], playedNotes.get(i) % 12);
            int col = findInArray(GenChordProgression.keyMatrix[key], playedNotes.get(i + 1) % 12);
            if (row != -1 && col != -1) {
                transitionArray[row][col]++;
            }
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

        return transitionArray;
    }

    private static int findInArray(int[] array, int value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return -1;
    }

    private static int getInversion(int predictedChordNumber, int key) {
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

        for (int i = 0; i < INVERSIONS_TO_CHOOSE; i++) {
            switch (i) {
                case 0:
                    break;
                case 1:
                    notesInPredictedChord[2] -= 12;
                    break;
                case 2:
                    notesInPredictedChord[2] -= 12;
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

    static ArrayList<Integer> chordsContain(int playedNote) {
        int thisNote = playedNote + 1;
        ArrayList<Integer> containing = new ArrayList<Integer>();
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 3; j++) {
                if (GenChordProgression.chordValues[i][j] == thisNote) {
                    containing.add(i);
                }
            }
        }
        return containing;
    }
}
