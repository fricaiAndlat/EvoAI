package de.diavololoop.eai.testing;

import de.diavololoop.eai.Brain;
import de.diavololoop.eai.individual.IAppeal;
import de.diavololoop.eai.individual.IBody;
import de.diavololoop.eai.individual.IReaction;

import java.util.LinkedList;
import java.util.List;

public class TestBrain {

    public static void main(String[] args) {

        List<IAppeal>   appeals   = new LinkedList<>();
        List<IReaction> reactions = new LinkedList<>();

        for (int i = 0; i < 2; ++i) {
            int value = i;
            appeals.add(new IAppeal() {
                @Override
                public double getNumericValue() {
                    return value;
                }

                @Override
                public String getUniqeName() {
                    return "get"+value;
                }
            });

            reactions.add(new IReaction() {

                @Override
                public void setNumericValue(double value) {
                    System.out.println("set output "+value+" to "+value);
                }

                @Override
                public String getUniqeName() {
                    return "set"+value;
                }

            });
        }

        IBody body = new IBody() {

            @Override
            public List<IAppeal> getApeals() {
                return appeals;
            }

            @Override
            public List<IReaction> getReactions() {
                return reactions;
            }

            @Override
            public IBody reproduce() {
                return this;
            }
        };

        Brain brain = new Brain(body, 2, 0.1, 0.5, 0.2);
        Brain brain2 = brain.reproduce(body);

        System.out.println(brain);
        System.out.println();
        System.out.println(brain2);

    }

}