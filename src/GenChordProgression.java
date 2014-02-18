public class GenChordProgression {

    static double[][] transMatrix = {{0, .069, 0, .289, .495, .138, .01},
            {.208, 0, .098, .220, .232, .232, .01},
            {.068, .113, 0, .371, .090, .349, .01},
            {.527, 0, 0, 0, .347, .116, .01},
            {.334, .074, 0, .285, 0, .297, .01},
            {.140, .076, .076, .381, .317, 0, .01},
            {.333, .094, 0, 0, .104, .469, 0}};

    static int[][] keyMatrix = {{0, 2, 4, 5, 7, 9, 11},
            {1, 3, 5, 6, 8, 10, 0},
            {2, 4, 6, 7, 9, 11, 1},
            {3, 5, 7, 8, 10, 0, 2},
            {4, 6, 8, 9, 11, 1, 3},
            {5, 7, 9, 10, 0, 2, 4},
            {6, 8, 10, 11, 1, 3, 5},
            {7, 9, 11, 0, 2, 4, 6},
            {8, 10, 0, 1, 3, 5, 7},
            {9, 11, 1, 2, 4, 6, 8},
            {10, 0, 2, 3, 5, 7, 9},
            {11, 1, 3, 4, 6, 8, 10}};

    public static double getTrans(int row, int column) {
        return transMatrix[row - 1][column - 1];
    }

    public static int[] noteInChord(int key, int chordNumber) {
        int[] keyArray = keyMatrix[key];
        int[] noteNumbers = new int[3];
        noteNumbers[0] = keyArray[chordNumber];
        noteNumbers[1] = keyArray[(chordNumber + 2) % 7];
        noteNumbers[2] = keyArray[(chordNumber + 4) % 7];
        return noteNumbers;
    }
}
