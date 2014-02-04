import javax.sound.midi.*;

public class Output {

    private final static int MILLISECONDS = 1000, VELOCITY = 75;


    // This class contains the static methods necessary to send information to the keyboard.

    public static void playNote(int noteNumber, int velocity) {
        try {
            ShortMessage message = new ShortMessage();
            message.setMessage(ShortMessage.NOTE_ON, 0, noteNumber, velocity);
            Receiver receiver = MidiSystem.getReceiver();
            long timeStamp = -1;
            receiver.send(message, timeStamp);
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }

    public static void playChord(int chordNumber, int inversion, int key) {
        switch (chordNumber){
            case 1:
                playMajorChord(48 + key - 1, inversion);
                break;
            case 2:
                playMinorChord(48 + key + 1, inversion);
                break;
            case 3:
                playMinorChord(48 + key + 3, inversion);
                break;
            case 4:
                playMajorChord(48 + key + 4, inversion);
                break;
            case 5:
                playMajorChord(48 + key + 6, inversion);
                break;
            case 6:
                playMinorChord(48 + key + 8, inversion);
                break;
            case 7:
                int dimBaseNote = 48 + key + 10;
                switch (inversion) {
                    case 0:
                        playNote(dimBaseNote, VELOCITY);
                        playNote(dimBaseNote + 3, VELOCITY);
                        playNote(dimBaseNote + 6, VELOCITY);
                        break;
                    case 1:
                        playNote(dimBaseNote + 12, VELOCITY);
                        playNote(dimBaseNote + 3, VELOCITY);
                        playNote(dimBaseNote + 6, VELOCITY);
                        break;
                    case 2:
                        playNote(dimBaseNote + 12, VELOCITY);
                        playNote(dimBaseNote + 15, VELOCITY);
                        playNote(dimBaseNote + 6, VELOCITY);
                        break;
                }
                break;
        }
    }

    private static void playMajorChord(int noteNumber, int inversion){
        switch (inversion) {
            case 0:
                playNote(noteNumber, VELOCITY);
                playNote(noteNumber + 4, VELOCITY);
                playNote(noteNumber + 7, VELOCITY);
                break;
            case 1:
                playNote(noteNumber + 12, VELOCITY);
                playNote(noteNumber + 4, VELOCITY);
                playNote(noteNumber + 7, VELOCITY);
                break;
            case 2:
                playNote(noteNumber + 12, VELOCITY);
                playNote(noteNumber + 16, VELOCITY);
                playNote(noteNumber + 7, VELOCITY);
                break;
        }
    }

    private static void playMinorChord(int noteNumber, int inversion){
        switch (inversion) {
            case 0:
                playNote(noteNumber, VELOCITY);
                playNote(noteNumber + 3, VELOCITY);
                playNote(noteNumber + 7, VELOCITY);
                break;
            case 1:
                playNote(noteNumber + 12, VELOCITY);
                playNote(noteNumber + 3, VELOCITY);
                playNote(noteNumber + 7, VELOCITY);
                break;
            case 2:
                playNote(noteNumber + 12, VELOCITY);
                playNote(noteNumber + 15, VELOCITY);
                playNote(noteNumber + 7, VELOCITY);
                break;
        }
    }

    // Input is the number of seconds (INTEGER) to delay.
    public static void delay(double numberSeconds){
        try {
            Thread.sleep((long) (numberSeconds * MILLISECONDS));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
