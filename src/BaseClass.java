import be.ac.ulg.montefiore.run.jahmm.Hmm;
import be.ac.ulg.montefiore.run.jahmm.ObservationInteger;

import java.io.IOException;

public class BaseClass {

    public static void main(String[] args) throws IOException {
        // Create a learnt HMM from the provided training data.
        HiddenMarkovModel hiddenMarkovModel = new HiddenMarkovModel();
        Hmm<ObservationInteger> learntHmm = hiddenMarkovModel.getHmm();

        Input input = new Input(learntHmm);
        pauseForInput(input);
    }

    // Once the user presses enter, the application stops running.
    private static void pauseForInput(Input input) {
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