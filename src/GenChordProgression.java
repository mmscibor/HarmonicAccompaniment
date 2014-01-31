public class GenChordProgression {

    static int[] keyCMajorNotes = {36, 38, 40, 41, 43, 45, 47, 48, 50, 52, 53, 55, 57, 59, 60};
    static int[][] rootChords = new int[keyCMajorNotes.length - 4][3];

    public static void returnChords(int[] rootNotes){
        for (int i=0; i < rootNotes.length - 4; i++){
            rootChords[i][0] = rootNotes[i];
            rootChords[i][1] = rootNotes[i+2];
            rootChords[i][2] = rootNotes[i+4];
        }
    }
}
