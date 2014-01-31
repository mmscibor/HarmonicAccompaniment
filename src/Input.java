import javax.sound.midi.*;
import java.util.List;

public class Input {

    MidiDevice device;
    MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();

    public Input(){
        for (int i=0; i < infos.length; i++){
            try {
                device = MidiSystem.getMidiDevice(infos[i]);
                System.out.println(infos[i]);

                List<Transmitter> transmitters = device.getTransmitters();

                for (int j=0; j < transmitters.size(); j++){
                    transmitters.get(j).setReceiver(new MidiInputReceiver(device.getDeviceInfo().toString()));
                }

                Transmitter trans = device.getTransmitter();
                trans.setReceiver(new MidiInputReceiver(device.getDeviceInfo().toString()));

                device.open();

                System.out.println("Was successfully opened");
            } catch (MidiUnavailableException e) {
                e.printStackTrace();
            }
        }
    }

    public void closeDevices(){
        for (int i=0; i < infos.length; i++){
            try {
                device = MidiSystem.getMidiDevice(infos[i]);
                device.close();
            } catch (MidiUnavailableException e) {
                e.printStackTrace();
            }
        }
    }

    private class MidiInputReceiver implements Receiver {
        private String name;
        public MidiInputReceiver(String name) {
            this.name = name;
        }
        public void send(MidiMessage message, long timeStamp){
            byte[] derivedMessage = message.getMessage();
            System.out.println(derivedMessage[1]);
        }
        public void close(){

        }
    }
}
