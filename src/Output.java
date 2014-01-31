import javax.sound.midi.*;

public class Output {

    // This class contains the static methods necessary to send information to the keyboard.

    public static void playNote(int noteNumber, int velocity){
        try{
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

    public static void playMajorChord(int rootNote, int velocity, int inversion){
        switch (inversion){
            case 0:
                playNote(rootNote, velocity);
                playNote(rootNote + 4, velocity);
                playNote(rootNote + 7, velocity);
                break;
            case 1:
                playNote(rootNote + 12, velocity);
                playNote(rootNote + 4, velocity);
                playNote(rootNote + 7, velocity);
                break;
            case 2:
                playNote(rootNote + 12, velocity);
                playNote(rootNote + 16, velocity);
                playNote(rootNote + 7, velocity);
                break;
        }
    }

    public static void playMinorChord(int rootNote, int velocity, int inversion){
        switch (inversion){
            case 0:
                playNote(rootNote, velocity);
                playNote(rootNote + 3, velocity);
                playNote(rootNote + 7, velocity);
                break;
            case 1:
                playNote(rootNote + 12, velocity);
                playNote(rootNote + 3, velocity);
                playNote(rootNote + 7, velocity);
                break;
            case 2:
                playNote(rootNote + 12, velocity);
                playNote(rootNote + 15, velocity);
                playNote(rootNote + 7, velocity);
                break;
        }
    }
}
