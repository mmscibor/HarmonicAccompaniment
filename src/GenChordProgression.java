public class GenChordProgression {

    static double[][] transMatrix = {{0, .069, 0, .289, .495, .138, .01},
            {.208, 0, .098, .220, .232, .232, .01},
            {.068, .113, 0, .371, .090, .349, .01},
            {.527, 0, 0, 0, .347, .116, .01},
            {.334, .074, 0, .285, 0, .297, .01},
            {.140, .076, .076, .381, .317, 0, .01},
            {.333, .094, 0, 0, .104, .469, 0}};

    static int[][] keyMatrix = new int[12][7];

    public static void createKeyMatrix(){
        int[] arrayCMaj = new int[]{0, 2, 4, 5, 7, 9, 11};
        keyMatrix[0] = arrayCMaj;
        for (int i=1; i<12; i++){
            keyMatrix[i] = addToArray(arrayCMaj, i);
        }
    }

    public static double getTrans(int row, int column){
        return transMatrix[row-1][column-1];
    }

    private static int[] addToArray(int[] array, int i){
        int[] newArray = new int[7];

        for (int j=0; j<newArray.length; j++){
            newArray[j] = array[j] + i;
            newArray[j] = newArray[j] % 12;
        }

        return newArray;
    }
}
