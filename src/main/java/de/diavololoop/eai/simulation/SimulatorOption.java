package de.diavololoop.eai.simulation;

public class SimulatorOption {

    public static final int SIZE_INFINITY = -1;


    public final int simulationSize;

    public SimulatorOption(int simulationSize) {

        if(simulationSize < 1) {
            throw new IllegalArgumentException("simulation group size must be > 0");
        }

        this.simulationSize = simulationSize;

    }

}
