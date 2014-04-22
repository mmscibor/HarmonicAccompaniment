import be.ac.ulg.montefiore.run.jahmm.Hmm;
import be.ac.ulg.montefiore.run.jahmm.ObservationInteger;

import javax.sound.midi.*;
import java.util.ArrayList;
import java.util.List;

public class Input {

    MidiDevice device;
    Hmm<ObservationInteger> learntHmm;
    MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
    List<Integer> playedNotes = new ArrayList<Integer>();
    long currentTimeStamp = System.currentTimeMillis(), averageTime, nextTime = System.currentTimeMillis();
    Timing timing = new Timing();
    Range selectedRange;

    public Input(Hmm<ObservationInteger> learntHmm) {
        this.learntHmm = learntHmm;

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
            if (derivedMessage.length > 1 && ((int) derivedMessage[2]) != 0) { // Only occur on down note, not on note release
                playedNotes.add((int) derivedMessage[1]); // Append played note to List
                timing.timeDifferentials.add(Math.abs(System.currentTimeMillis() - currentTimeStamp));

                selectedRange = timing.determineTime();
                if (System.currentTimeMillis() > nextTime) {
                    averageTime = selectedRange.getAverageTime();
                    switch (timing.retrieveRangeIndex(selectedRange)) {
                        case 0:
                            averageTime = averageTime * 4;
                            break;
                        case 1:
                            averageTime = averageTime * 2;
                            break;
                        case 2:
                            break;
                        case 3:
                            break;
                    }

                    nextTime = System.currentTimeMillis() + averageTime;

                    MachineLearning.determineChords(playedNotes, learntHmm);
                    Output.playChord(MachineLearning.lastChord);
                    System.out.println("Chord played: " + MachineLearning.lastChord.getChordNumber());
                }
                currentTimeStamp = System.currentTimeMillis();
            }
        }

        public void close() {
        }
    }
}
