package de.diavololoop.eai;

import de.diavololoop.eai.individual.IAppeal;
import de.diavololoop.eai.individual.IBody;
import de.diavololoop.eai.individual.IReaction;
import de.diavololoop.eai.perceptron.*;

import java.util.*;
import java.util.stream.Collectors;

public class Brain<I extends IBody> {

    private final static Random RAND = new Random();

    private final Map<String, Perceptron> appeal   = new HashMap<>();
    private final Map<String, Perceptron> reaction = new HashMap<>();

    private final List<Perceptron> perceptrons = new ArrayList<Perceptron>();

    private double chanceNewPerceptron;
    private double mutateRate;
    private double chanceToMutate;

    private Brain(double chanceNewPerceptron, double mutateRate, double chanceToMutate) {
        this.chanceNewPerceptron = chanceNewPerceptron;
        this.mutateRate          = mutateRate;
        this.chanceToMutate      = chanceToMutate;
    }

    public Brain(I body, int initialSize, double chanceNewPerceptron, double mutateRate, double chanceToMutate) {

        this.chanceNewPerceptron = chanceNewPerceptron;
        this.mutateRate          = mutateRate;
        this.chanceToMutate      = chanceToMutate;

        final long[] id = {0};

        body.getApeals().forEach(a -> {
            InputPerceptron inputPerceptron = new InputPerceptron(id[0]++, a);
            appeal.put(a.getUniqeName(), inputPerceptron);
            perceptrons.add(inputPerceptron);
        });

        body.getReactions().forEach(r -> {
            OutputPerceptron outputPerceptron = new OutputPerceptron(id[0]++, r);
            reaction.put(r.getUniqeName(), outputPerceptron);
            perceptrons.add(outputPerceptron);
        });

        perceptrons.add(ConstantPerceptron.INSTANCE);

        for (int i = 0; i < initialSize; ++i) {
            perceptrons.add(new Perceptron(id[0]++));
        }

        perceptrons.forEach(p1 -> {
            perceptrons.stream().filter(p2 -> p2 != p1).forEach(p2 -> {
                p1.linkPerceptron(p2, Math.random()*2 - 1);
            });
        });


    }

    public void stimulate() {
        perceptrons.forEach(Perceptron::perform);
        perceptrons.forEach(Perceptron::swap);
    }

    public Brain<I> reproduce(I newBody) {

        //make new brain mutate values
        double newChanceNewPerceptron = Mutation.mutate(chanceNewPerceptron, chanceToMutate, mutateRate);
        double newMutateRate          = Mutation.mutate(mutateRate         , chanceToMutate, mutateRate);
        double newChanceToMutate      = Mutation.mutate(chanceToMutate     , chanceToMutate, mutateRate);
        Brain child = new Brain(newChanceNewPerceptron, newMutateRate, newChanceToMutate);


        //transfere appeals and reactions to the same Perceptron id
        List<IAppeal>   a = newBody.getApeals();
        List<IReaction> r = newBody.getReactions();

        if (a.size() != appeal.size() || r.size() != reaction.size()) {
            throw new IllegalArgumentException("both bodys must have the same input and output size!");
        }


        a.forEach(e -> {
            Perceptron p = appeal.get(e.getUniqeName());

            if (p == null) {
                throw new IllegalArgumentException("both bodys must have the same type of inputs (unique name)");
            }
            InputPerceptron inputPerceptron = new InputPerceptron(p.id, e);
            child.appeal.put(e.getUniqeName(), inputPerceptron);
            child.perceptrons.add(inputPerceptron);
        });

        r.forEach(e -> {
            Perceptron p = reaction.get(e.getUniqeName());

            if (p == null) {
                throw new IllegalArgumentException("both bodys must have the same type of outputs (unique name)");
            }
            OutputPerceptron outputPerceptron = new OutputPerceptron(p.id, e);
            child.reaction.put(e.getUniqeName(), outputPerceptron);
            child.perceptrons.add(outputPerceptron);
        });



        //make new child perceptrons
        child.perceptrons.addAll(perceptrons.stream().filter(p -> p.clonable()).map(p -> p.child()).collect(Collectors.toList()));

        //connect all new child perceptrons
        List<Perceptron.Connection> connections = new LinkedList<>();
        List<Perceptron> l = child.perceptrons;
        perceptrons.forEach(p -> p.getConnections(connections));

        connections.stream().collect(Collectors.groupingBy(p -> p.idTo)).forEach( (t, c) -> {

            Perceptron p = l.stream().filter(e -> e.id == t).findAny().get();

            c.forEach(con -> p.linkPerceptron(l.stream().filter(e -> e.id == con.idFrom).findAny().get(), con.wheight));
        });


        //perhaps create a new perceptron and connect it to all others
        if (RAND.nextDouble() <= this.chanceNewPerceptron) {

            //max id +1 -> new id
            Perceptron perceptron = new Perceptron(l.stream().mapToLong(p -> p.id).max().orElse(-1) + 1);

            l.forEach(p -> {
                p.linkPerceptron(perceptron, RAND.nextDouble()*2 - 1);
                perceptron.linkPerceptron(p, RAND.nextDouble()*2 - 1);
            });

            l.add(perceptron);
        }

        return child;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("\r\nBRAIN:\r\n");
        builder.append("## number of perceptrons: ").append(perceptrons.size()).append("\r\n");
        perceptrons.forEach(builder::append);
        builder.append("\r\nEND BRAIN:\r\n");

        return builder.toString();
    }
}