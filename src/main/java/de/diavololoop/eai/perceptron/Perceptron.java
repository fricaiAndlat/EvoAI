package de.diavololoop.eai.perceptron;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class Perceptron {

    private final static Random RAND = new Random(0);

    protected final Map<Perceptron, Double> weights = new HashMap<>();

    protected double value;
    protected double nextValue;

    private final double chanceToMutate;
    private final double mutateRate;

    public final long id;

    public Perceptron(long id) {
        this(id, RAND.nextDouble(), RAND.nextDouble());
    }

    public Perceptron(long id, double chanceToMutate, double mutateRate) {
        this.chanceToMutate = Math.min(1, Math.max(0, chanceToMutate));
        this.mutateRate     = mutateRate;
        this.id             = id;
    }

    public double value() {
        return value;
    }

    public void perform() {
        nextValue = sig(weights.entrySet().stream().collect(Collectors.summingDouble(e -> e.getKey().value() * e.getValue())));
    }

    public void swap() {
        value = nextValue;
    }

    public void linkPerceptron(Perceptron p, double weight) {
        weights.put(p, weight);
    }

    public void getConnections(List<Connection> connections) {
        connections.addAll(
                weights.entrySet().stream()
                        .map(e -> new Connection(
                                e.getKey().id,
                                id,
                                mutate(e.getValue())))
                        .collect(Collectors.toList()));
    }

    public boolean clonable() {
        return true;
    }

    public Perceptron child() {

        return new Perceptron(id, mutate(chanceToMutate), mutate(mutateRate));

    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("[%d]: %s\r\n\tchanceToMutate: %f\r\n\tmutateRate: %f\r\n", this.id, getClass().getName(), chanceToMutate, mutateRate));
        weights.forEach((p, w) -> builder.append(String.format("\t[%3d] -> [%+3.4f]\r\n", p.id, w)));

        builder.append("\r\n");

        return builder.toString();
    }

    protected double sig(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    private double mutate(double x) {
        return Mutation.mutate(x, chanceToMutate, mutateRate);
    }

    public class Connection {
        public final long idFrom;
        public final long idTo;
        public final double wheight;


        Connection(long idFrom, long idTo, double wheight) {
            this.idFrom  = idFrom;
            this.idTo    = idTo;
            this.wheight = wheight;
        }
    }



}
