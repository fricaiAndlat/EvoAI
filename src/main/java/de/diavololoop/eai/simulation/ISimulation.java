package de.diavololoop.eai.simulation;

import de.diavololoop.eai.individual.IBody;
import de.diavololoop.eai.individual.Individual;

import java.util.Map;

public interface ISimulation<K extends IBody> {


    //setup
    public void reset();
    public void addMember(Individual<K> member);

    //progress
    public boolean isFinished();
    public void step();

    //evaluation
    public Map<Individual<K>, Double> getResult();
}
