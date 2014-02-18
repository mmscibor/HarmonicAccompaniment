import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MachineLearning {

    int previousLength = 0;
    int[] pointsVectorNotes = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    List<Integer> playedNotes;
    static Chord lastChord = new Chord(-1, -1, -1);
    private static final int NUM_COL = 7, NUM_ROW = 7;

    public MachineLearning(List<Integer> playedNotes) {
        this.playedNotes = playedNotes;
    }

    public void determineChords() {
        int key = getKey();
        int[] notesInKey = GenChordProgression.keyMatrix[key];

        int mostRecentNote = playedNotes.get(playedNotes.size() - 1) % 12;
        int chordNumber = findInArray(notesInKey, mostRecentNote);

        if (lastChord.getChordNumber() == -1) {
            if (chordNumber >= 0) {
                lastChord = new Chord(chordNumber, 0, key);
            } else {
                return;
            }
        } else {
            if (chordNumber >= 0) {
                double[] noteTransMatrix = getTransitionMatrix(playedNotes, key)[chordNumber];
                int predictedNote = maxInArray(noteTransMatrix);

                int previousChordNumber = lastChord.getChordNumber();
                double[] percentageTransitions = GenChordProgression.transMatrix[previousChordNumber];

                for (int i = 0; i < percentageTransitions.length; i++) {
                    int predictedChord = maxInArray(percentageTransitions);
                    int[] notesInChord = GenChordProgression.noteInChord(key, predictedChord);
                    if (findInArray(notesInChord, predictedNote) >= 0) {
//                        int inversion = getInversion(predictedChord, key);
                        lastChord.setChord(predictedChord, 0, key);
                    } else {
                        percentageTransitions[predictedChord] = 0;
                    }
                }
            }
        }
    }

    private int getKey() {
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

    private int maxInArray(double[] array) {
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

    private int maxInArray(int[] array) {
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

    private double[][] getTransitionMatrix(List<Integer> playedNotes, int key) {
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
//
//    private static int getInversion(int predictedChordNumber, int key) {
//        int previousInversion = lastChord.getInversion(), previousChordNumber = lastChord.getChordNumber();
//
//    }
}
