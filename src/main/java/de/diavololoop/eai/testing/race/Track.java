package de.diavololoop.eai.testing.race;

import de.diavololoop.eai.individual.Individual;
import de.diavololoop.eai.simulation.ISimulation;

import javax.swing.*;
import java.util.Map;

public class Track extends JFrame implements ISimulation<Car> {


    public Track() {
        setTitle("Track Simulation");
        setSize(1600, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

    }


    @Override
    public void reset() {

    }

    @Override
    public void addMember(Individual<Car> member) {

    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void step() {

    }

    @Override
    public Map<Individual<Car>, Double> getResult() {
        return null;
    }
}
