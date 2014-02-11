import be.ac.ulg.montefiore.run.jahmm.Hmm;
import be.ac.ulg.montefiore.run.jahmm.ObservationDiscrete;
import be.ac.ulg.montefiore.run.jahmm.OpdfDiscreteFactory;

import java.io.IOException;
import java.util.List;

public class BaseClass {

    public enum State {
        C, Csh, D, Dsh, E, F, Fsh, G, Gsh, A, Ash, B;

    }

    public static void main(String[] args){
        Input input = new Input();
        pauseForInput(input);
//        List<ObservationDiscrete<State>> sequence;
//        sequence.add()

    }

    // Once the user presses enter, the application stops running.
    private static void pauseForInput(Input input){
        try {
            System.out.print("Press 'Enter' to exit application.");
            System.in.read();
            input.closeDevices();
            System.out.println("Size of playedNotes: " + input.playedNotes.size());
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
