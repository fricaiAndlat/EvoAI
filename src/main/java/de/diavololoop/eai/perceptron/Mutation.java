package de.diavololoop.eai.perceptron;

import java.util.Random;

public class Mutation {

    private final static Random RAND = new Random(0);


    public static double mutate(double x, double chanceToMutate, double mutateRate) {
        return RAND.nextDouble() <= chanceToMutate ? x + x * 2 * (RAND.nextDouble()-0.5) * mutateRate : x;
    }

}
