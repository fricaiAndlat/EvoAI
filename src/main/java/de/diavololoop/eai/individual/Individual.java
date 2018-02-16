package de.diavololoop.eai.individual;

import de.diavololoop.eai.Brain;

public class Individual<K extends IBody> {

    private final K body;
    private final Brain<K> brain;

    public Individual(K body, Brain<K> brain) {
        this.body = body;
        this.brain = brain;
    }

    public K getBody() {
        return body;
    }

    public Brain<K> getBrain() {
        return brain;
    }
}