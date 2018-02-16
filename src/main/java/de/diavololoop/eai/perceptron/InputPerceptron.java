package de.diavololoop.eai.perceptron;

import de.diavololoop.eai.individual.IAppeal;

import java.util.List;

public class InputPerceptron extends Perceptron {

    private final IAppeal appeal;

    public InputPerceptron(long id, IAppeal appeal) {
        super(id);

        this.appeal = appeal;
    }

    @Override
    public double value() {
        return value;
    }

    @Override
    public void perform() {
        nextValue = appeal.getNumericValue();
    }

    @Override
    public void swap() {
        value = nextValue;
    }

    @Override
    public void linkPerceptron(Perceptron p, double weight) {
    }

    @Override
    public void getConnections(List<Connection> connections) {
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
