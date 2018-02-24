package de.diavololoop.eai.testing.race;

import de.diavololoop.eai.Brain;
import de.diavololoop.eai.individual.IBody;
import de.diavololoop.eai.individual.Individual;
import de.diavololoop.eai.simulation.Simulator;
import de.diavololoop.eai.simulation.SimulatorOption;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Race {

    private static final int POPULATION_SIZE = 1000;
    private static final int BRAIN_SIZE = 20;

    public static void main(String[] args) {

        Track track = new Track();


        //Simulator<Car> simulator = new Simulator<>(track, createPopulation(), getOption(), Race::evolve);



    }

    private static <K extends IBody> List<Individual<K>> evolve(Map<Individual<K>, List<Double>> individualListMap) {

        double pivot = individualListMap.values().stream()
                .mapToDouble(v -> v.stream()
                        .mapToDouble(e -> e)
                        .sum())
                .sorted()
                .skip(individualListMap.size()/2)
                .min()
                .orElse(0);


        //individualListMap.entrySet()

        return null;


    }

    private static List<Individual<Car>> createPopulation() {

        List<Individual<Car>> population = new LinkedList<>();

        for (int i = 0; i < POPULATION_SIZE; ++i) {
            Car car = new Car();
            Brain<Car> brain = new Brain<Car>(car, BRAIN_SIZE, 0.1, 0.5, 0.2);

            population.add(new Individual<Car>(car, brain));
        }

        return population;
    }


    public static SimulatorOption getOption() {
        return new SimulatorOption(SimulatorOption.SIZE_INFINITY);
    }
}
