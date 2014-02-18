import javax.sound.midi.*;

public class Output {

    private final static int MILLISECONDS = 1000, VELOCITY = 90;

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

    public static void playChord(Chord chord) {
        switch (chord.getChordNumber()) {
            case 0:
                playMajorChord(48 + chord.getKey() - 1, chord.getInversion());
                break;
            case 1:
                playMinorChord(48 + chord.getKey() + 1, chord.getInversion());
                break;
            case 2:
                playMinorChord(48 + chord.getKey() + 3, chord.getInversion());
                break;
            case 3:
                playMajorChord(48 + chord.getKey() + 4, chord.getInversion());
                break;
            case 4:
                playMajorChord(48 + chord.getKey() + 6, chord.getInversion());
                break;
            case 5:
                playMinorChord(48 + chord.getKey() + 8, chord.getInversion());
                break;
            case 6:
                int dimBaseNote = 48 + chord.getKey() + 10;
                switch (chord.getInversion()) {
                    case 0:
                        playNote(dimBaseNote, VELOCITY);
                        playNote(dimBaseNote + 3, VELOCITY);
                        playNote(dimBaseNote + 6, VELOCITY);
                        break;
                    case 1:
                        playNote(dimBaseNote, VELOCITY);
                        playNote(dimBaseNote + 3, VELOCITY);
                        playNote(dimBaseNote - 6, VELOCITY);
                        break;
                    case 2:
                        playNote(dimBaseNote, VELOCITY);
                        playNote(dimBaseNote - 9, VELOCITY);
                        playNote(dimBaseNote - 6, VELOCITY);
                        break;
                }
                break;
        }
    }

    private static void playMajorChord(int noteNumber, int inversion) {
        switch (inversion) {
            case 0:
                playNote(noteNumber, VELOCITY);
                playNote(noteNumber + 4, VELOCITY);
                playNote(noteNumber + 7, VELOCITY);
                break;
            case 1:
                playNote(noteNumber, VELOCITY);
                playNote(noteNumber + 4, VELOCITY);
                playNote(noteNumber - 5, VELOCITY);
                break;
            case 2:
                playNote(noteNumber, VELOCITY);
                playNote(noteNumber - 8, VELOCITY);
                playNote(noteNumber - 5, VELOCITY);
                break;
        }
    }

    private static void playMinorChord(int noteNumber, int inversion) {
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
    public static void delay(double numberSeconds) {
        try {
            Thread.sleep((long) (numberSeconds * MILLISECONDS));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
