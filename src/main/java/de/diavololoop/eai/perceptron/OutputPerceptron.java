package de.diavololoop.eai.perceptron;

import de.diavololoop.eai.individual.IReaction;

import java.util.stream.Collectors;

public class OutputPerceptron extends Perceptron {

    private final IReaction reaction;

    public OutputPerceptron(long id, IReaction reaction) {
        super(id);

        this.reaction = reaction;
    }

    @Override
    public double value() {
        return value;
    }

    @Override
    public void perform() {
        nextValue = weights.entrySet().stream().collect(Collectors.summingDouble(e -> e.getKey().value * e.getValue()));
    }

    @Override
    public void swap() {
        value = sig(nextValue);
        reaction.setNumericValue(nextValue);
    }

    @Override
    public Perceptron child() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean clonable() {
        return false;
    }
}
