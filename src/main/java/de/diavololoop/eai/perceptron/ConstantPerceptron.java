package de.diavololoop.eai.perceptron;

import java.util.List;


public class ConstantPerceptron extends Perceptron {

    private ConstantPerceptron() {
        super(-1);
    }

    public final static ConstantPerceptron INSTANCE = new ConstantPerceptron();

    @Override
    public double value() {
        return 1;
    }

    @Override
    public void perform() {}

    @Override
    public void swap() {
    }

    @Override
    public void linkPerceptron(Perceptron p, double weight) {
    }

    @Override
    public void getConnections(List<Connection> connections) {
    }

    @Override
    public boolean clonable() {
        return true;
    }

    @Override
    public Perceptron child() {
        return this;
    }

}
