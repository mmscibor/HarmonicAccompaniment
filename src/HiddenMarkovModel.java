import be.ac.ulg.montefiore.run.jahmm.*;
import be.ac.ulg.montefiore.run.jahmm.learn.BaumWelchLearner;
import be.ac.ulg.montefiore.run.jahmm.toolbox.KullbackLeiblerDistanceCalculator;

import java.util.ArrayList;
import java.util.List;

public class HiddenMarkovModel {
    /* Possible packet reception status */

    public enum States {
        ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN;
    }

    static public void trainModel() throws java.io.IOException {
        // Observed sequences were generated in MATLAB
        List<List<ObservationDiscrete<States>>> sequences;
        sequences = generateSequences();

        // Use this to iterate and improve the HMM
        BaumWelchLearner baumWelchLearner = new BaumWelchLearner();
        Hmm<ObservationDiscrete<States>> learntHmm = buildInitHmm();

        // This object measures the distance between two HMMs
        KullbackLeiblerDistanceCalculator distanceCalculator = new KullbackLeiblerDistanceCalculator();

        // Incrementally improve the solution
        for (int i = 0; i < 15; i++) {
            Hmm<ObservationDiscrete<States>> newHmm = baumWelchLearner.iterate(learntHmm, sequences);
            System.out.println("Distance at iteration " + i + ": " + distanceCalculator.distance(learntHmm, newHmm));
            learntHmm = newHmm;
        }

        System.out.println("Resulting HMM:\n" + learntHmm);
    }

    static Hmm<ObservationDiscrete<States>> buildInitHmm() {
        // Use this to create initial HMM which gets recursively better
        final int NUM_STATES = 7;
        Hmm<ObservationDiscrete<States>> hmm = new Hmm<ObservationDiscrete<States>>(NUM_STATES, new OpdfDiscreteFactory<States>(States.class));

        double[] initialProbabilities = new double[NUM_STATES];

        // Initiate the array of observed element transition probabilities (all 1 / 7)
        for (int i = 0; i < NUM_STATES; i++) {
            initialProbabilities[i] = 1 / 7.0;
        }

        // Set the probabilities of a state being the initial state (all 1 / 7)
        for (int i = 0; i < NUM_STATES; i++) {
            hmm.setPi(i, (1 / 7.0));
            hmm.setOpdf(i, new OpdfDiscrete<States>(States.class, initialProbabilities));
        }

        // Set initial state transition probabilities (all 1 / 7)
        for (int i = 0; i < NUM_STATES; i++) {
            for (int j = 0; j < NUM_STATES; j++) {
                hmm.setAij(i, j, (1 / 7.0));
            }
        }

        return hmm;
    }

    static <O extends Observation> List<List<O>> generateSequences() {

        List<List<O>> sequences = new ArrayList<List<O>>();
        for (int i = 0; i < 200; i++) ;

        return sequences;
    }
}