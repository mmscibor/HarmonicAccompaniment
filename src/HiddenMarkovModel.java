import be.ac.ulg.montefiore.run.jahmm.Hmm;
import be.ac.ulg.montefiore.run.jahmm.ObservationDiscrete;
import be.ac.ulg.montefiore.run.jahmm.OpdfDiscrete;
import be.ac.ulg.montefiore.run.jahmm.OpdfDiscreteFactory;
import be.ac.ulg.montefiore.run.jahmm.learn.BaumWelchLearner;
import be.ac.ulg.montefiore.run.jahmm.toolbox.KullbackLeiblerDistanceCalculator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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

    static List<List<ObservationDiscrete<States>>> generateSequences() {
        List<List<ObservationDiscrete<States>>> sequences = new ArrayList<List<ObservationDiscrete<States>>>();

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("trainingData.txt"));
            String currentLine;
            while ((currentLine = bufferedReader.readLine()) != null) {
                String data[] = currentLine.split(" ");
                ArrayList<ObservationDiscrete<States>> observations = new ArrayList<ObservationDiscrete<States>>();
                for (int i = 0; i < data.length; i++) {
                    observations.add(new ObservationDiscrete<States>(intToEnum(Integer.parseInt(data[i]) % 7)));
                }
                sequences.add(observations);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sequences;
    }

    static int enumToInt(States state) {
        if (state.equals(States.ONE)) {
            return 1;
        } else if (state.equals(States.TWO)) {
            return 2;
        } else if (state.equals(States.THREE)) {
            return 3;
        } else if (state.equals(States.FOUR)) {
            return 4;
        } else if (state.equals(States.FIVE)) {
            return 5;
        } else if (state.equals(States.SIX)) {
            return 6;
        }
        return 7;
    }

    static States intToEnum(int value) {
        switch (value) {
            case 1:
                return States.ONE;
            case 2:
                return States.TWO;
            case 3:
                return States.THREE;
            case 4:
                return States.FOUR;
            case 5:
                return States.FIVE;
            case 6:
                return States.SIX;
            default:
                return States.SEVEN;
        }
    }
}