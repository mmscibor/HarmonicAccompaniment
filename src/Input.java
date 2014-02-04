import javax.sound.midi.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Input {

    MidiDevice device;
    MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
    List<Integer> playedNotes = new ArrayList<Integer>();

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
                System.out.println("Note played");
                playedNotes.add((int) derivedMessage[1]);
                machineLearn();
            }
        }

        public void close() {
        }
    }

    private void machineLearn() {
        if (playedNotes.size() % 10 == 0) {
            System.out.println(machineKey());
        }
    }

    private int machineKey() {
        Integer[] playedNotesArray = playedNotes.toArray(new Integer[playedNotes.size()]);
        int[] pointsVector = new int[12];
        HashMap<Integer, Integer> noteCount = new HashMap<Integer, Integer>();
        // TODO: Dynamic programming

        for (int i=0; i < pointsVector.length; i++){
            pointsVector[i] = 0;
        }

        for (int i = 0; i < playedNotesArray.length; i++) {
            playedNotesArray[i] = playedNotesArray[i] % 12;
            if (noteCount.containsKey(playedNotesArray[i])) {
                noteCount.put(playedNotesArray[i], noteCount.get(playedNotesArray[i]) + 1);
            } else {
                noteCount.put(playedNotesArray[i], 1);
            }
        }

        for (int uniqueNote : noteCount.keySet()) {
            for (int key = 0; key < 12; key++) {
                for (int note = 0; note < 7; note++) {
                    if (GenChordProgression.keyMatrix[key][note] == uniqueNote) {
                        pointsVector[key] = pointsVector[key] + noteCount.get(uniqueNote);
                    }
                }
            }
        }

        return getMax(pointsVector);
    }

    private int getMax(int[] array) {
        int max = 0, maxIndex = 0;
        List<Integer> maxList = new ArrayList<Integer>();

        for (int i = 0; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
                maxIndex = i;
                maxList.add(maxIndex);
            } else if (array[i] == max) {
                maxList.add(i);
            }
        }
        if (maxList.size() == 1) {
            return maxIndex;
        } else {
            Random random = new Random();
            int index = random.nextInt(maxList.size());
            return maxList.get(index);
        }
    }
}
