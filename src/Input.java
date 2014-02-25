import javax.sound.midi.*;
import java.util.ArrayList;
import java.util.List;

public class Input {

    MidiDevice device;
    MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
    List<Integer> playedNotes = new ArrayList<Integer>();
    List<Long> timeDifferentials = new ArrayList<Long>();
    boolean playChord = false;
    long currentTimeStamp = System.currentTimeMillis(), nextTime = System.currentTimeMillis();
    Timing timing = new Timing();

    public Input() {
        for (int i = 0; i < infos.length; i++) {
            try {
                device = MidiSystem.getMidiDevice(infos[i]);
                System.out.println(infos[i]);

                List<Transmitter> transmitters = device.getTransmitters();

                for (int j = 0; j < transmitters.size(); j++) {
                    transmitters.get(j).setReceiver(new MidiInputReceiver());
                }

                Transmitter trans = device.getTransmitter();
                trans.setReceiver(new MidiInputReceiver());

                device.open();

                System.out.println("Was successfully opened");
                break; // Go until a MIDI device is detected and opened, then stop opening the rest.
                // Hopefully the device we want is among these, otherwise we may need to remove break;

            } catch (MidiUnavailableException e) {
                System.out.println("Was NOT successfully opened");
            }
        }
    }

    public void closeDevices() {
        for (int i = 0; i < infos.length; i++) {
            try {
                device = MidiSystem.getMidiDevice(infos[i]);
                device.close();
            } catch (MidiUnavailableException e) {
                e.printStackTrace();
            }
        }
    }

    private class MidiInputReceiver implements Receiver {
        public void send(MidiMessage message, long timeStamp) {
            byte[] derivedMessage = message.getMessage();
            if (((int) derivedMessage[2]) != 0) { // Only occur on down note, not on note release
                playedNotes.add((int) derivedMessage[1]); // Append played note to List
                Timing.timeDifferentials.add(Math.abs(System.currentTimeMillis() - currentTimeStamp));
                currentTimeStamp = System.currentTimeMillis();

                Range selectedRange = timing.determineTime();
                if (selectedRange.fitsInRange(Math.abs(nextTime - currentTimeStamp))) {
                    MachineLearning.determineChords(playedNotes);
                    Output.playChord(MachineLearning.lastChord);
                    nextTime = System.currentTimeMillis();
                }
            }
        }

        public void close() {
        }
    }
}
