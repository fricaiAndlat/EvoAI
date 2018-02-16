package de.diavololoop.eai.simulation;

import de.diavololoop.eai.individual.IBody;
import de.diavololoop.eai.individual.Individual;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Simulator<K extends IBody> {

    private final static Random RAND = new Random(0);

    private final Function<Map<Individual<K>, List<Double>>, List<Individual<K>>> evolution;
    private final ISimulation<K> simulation;
    private final SimulatorOption option;
    private Map<Individual<K>, List<Double>> population;

    public Simulator(ISimulation<K> simulation, List<Individual<K>> population, SimulatorOption option, Function<Map<Individual<K>, List<Double>>, List<Individual<K>>> evolution) {
        this.evolution = evolution;
        this.simulation = simulation;
        this.option = option;

        this.population = population.stream().collect(Collectors.toMap(e -> e, e -> new LinkedList<Double>()));
    }

    public List<Individual<K>> getPopulation() {
        return population.keySet().stream().collect(Collectors.toList());
    }

    public void nextGeneration(int rounds) {

        for (int i = 0; i < rounds; ++i) {
            if (option.simulationSize == SimulatorOption.SIZE_INFINITY) {
                simulateAll();
            } else {
                simulateGroup(option.simulationSize);
            }

        }

        List<Individual<K>> newPopulation = evolution.apply(population);
        population.clear();
        newPopulation.forEach(i -> population.put(i, new LinkedList<Double>()));

    }

    private void simulateAll() {
        simulation.reset();
        population.keySet().forEach(simulation::addMember);

        while (!simulation.isFinished()) {
            simulation.step();
        }

        simulation.getResult().forEach((k, v) -> population.get(k).add(v));
    }

    private void simulateGroup(int groupSize) {
        ArrayList<Individual<K>> orderedPopulation = new ArrayList<>();
        orderedPopulation.addAll(population.keySet().stream().collect(Collectors.toList()));

        for (int i = 0; i < orderedPopulation.size(); ++i) {

            simulation.reset();
            simulation.addMember(orderedPopulation.get(i));


            LinkedList<Individual<K>> queue = new LinkedList<>();
            queue.addAll(population.entrySet().stream()
                    .sorted(Comparator.comparingInt(o -> o.getValue().size()))
                    .map(e -> e.getKey())
                    .collect(Collectors.toList()));

            for (int j = 1; j < groupSize; ++j) {
                Individual<K> member = queue.pollFirst();

                if (member == orderedPopulation.get(i)) {
                    --j;
                } else {
                    simulation.addMember(member);
                }
            }

            while (!simulation.isFinished()) {
                simulation.step();
            }

            simulation.getResult().forEach((k, v) -> population.get(k).add(v));

        }
    }

}