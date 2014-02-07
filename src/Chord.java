
public class Chord {
    private int chordNumber, inversion, key;

    public Chord(int chordNumber, int inversion, int key) {
        this.chordNumber = chordNumber;
        this.inversion = inversion;
        this.key = key;
    }

    public void setChord(int chordNumber, int inversion, int key) {
        this.chordNumber = chordNumber;
        this.inversion = inversion;
        this.key = key;
    }

    public void setInversion(int inversion) {
        this.inversion = inversion;
    }

    public boolean isMajor() {
        if (chordNumber == 1 || chordNumber == 4 || chordNumber == 5) {
            return true;
        } else {
            return false;
        }
    }

    public int getChordNumber() {
        return chordNumber;
    }

    public int getInversion() {
        return inversion;
    }

    public int getKey() {
        return key;
    }
}
