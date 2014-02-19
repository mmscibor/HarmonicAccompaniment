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
            if (((int) derivedMessage[2]) != 0) {
                if (playChord) {
                    Output.playChord(MachineLearning.lastChord);
                    playChord = false;
                }
                playedNotes.add((int) derivedMessage[1]); // Append played note to List
                timeDifferentials.add(Math.abs(System.currentTimeMillis() - currentTimeStamp));
                currentTimeStamp = System.currentTimeMillis();

                long selectedTime = Timing.determineTime(timeDifferentials);
                if (nextTime < System.currentTimeMillis()) {
                    MachineLearning.determineChords(playedNotes);
                    playChord = true;
                    nextTime = System.currentTimeMillis() + selectedTime;
                }
            }
        }

        public void close() {
        }
    }
}
