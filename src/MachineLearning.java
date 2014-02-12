import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MachineLearning {

    int previousLength = 0;
    int[] pointsVectorNotes = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    List<Integer> playedNotes;
    static Chord lastChord = new Chord(-1, -1, -1), nextChord;

    public MachineLearning(List<Integer> playedNotes) {
        this.playedNotes = playedNotes;
    }

    public void determineChords() {
        int key = machineKey();
        int[] notesInKey = GenChordProgression.keyMatrix[key];

        int mostRecentNote = playedNotes.get(playedNotes.size() - 1) % 12;

        int index = -1;
        for (int j = 0; j < notesInKey.length; j++) {
            if (mostRecentNote == notesInKey[j]) {
                index = j;
            }
        }

        if (lastChord.getKey() == -1) {
            if (index == -1) {
                return;
            }

            List<Chord> tonalChords = new ArrayList<Chord>();

            tonalChords.add(new Chord(index, 0, key));
            tonalChords.add(new Chord((index - 2) % 7, 0, key));
            tonalChords.add(new Chord((index - 4) % 7, 0, key));

            for (Chord chord : tonalChords) {
                if (chord.isMajor()) {
                    lastChord = chord;
                }
            }

            if (lastChord.getKey() == -1) {
                Random random = new Random();
                int selector = random.nextInt(tonalChords.size());
                lastChord = tonalChords.get(selector);
            }
        } else {
            int previousChordNumber = lastChord.getChordNumber();
            double[] percentageTransitions = GenChordProgression.transMatrix[previousChordNumber];

            if (index > -1) {
                for (int i = 0; i < percentageTransitions.length; i++) {
                    if (i != index && i != (index - 2) % 7 && i != (index - 4) % 7) {
                        percentageTransitions[i] = percentageTransitions[i]*.50;
                    }
                }
            }

            double totalWeight = 0.0d;

            for (double percent : percentageTransitions) {
                totalWeight += percent;
            }

            int randomIndex = -1;
            double random = Math.random() * totalWeight;

            for (int i = 0; i < percentageTransitions.length; i++) {
                random -= percentageTransitions[i];
                if (random <= 0.0d) {
                    randomIndex = i;
                    break;
                }
            }

            lastChord = new Chord(randomIndex, 0, key);
        }

        Output.playChord(lastChord);
    }

    private int machineKey() {
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

        return getMax(pointsVectorKey);
    }

    private int getMax(int[] array) {
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
                    if (maxNoteIndex == maxKeys[i]){
                        return maxKeys[i]; // major key
                    }
                }
                for (int i = 0; i < maxList.size(); i++) {
                    if (maxNoteIndex == maxKeys[i+maxList.size()]){
                        return maxKeys[i]; // minor key -- returns key = 0 for key of A minor
                    }
                }
            }
            else {
                for (int maxNoteInd:maxNoteList){
                    for (int i = 0; i < maxList.size(); i++) {
                        if (maxNoteInd == maxKeys[i]){
                            return maxKeys[i]; // major key
                        }
                    }
                }
                for (int maxNoteInd:maxNoteList){
                    for (int i = 0; i < maxList.size(); i++) {
                        if (maxNoteInd == maxKeys[i+maxList.size()]){
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
}
