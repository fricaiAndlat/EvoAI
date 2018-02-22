package de.diavololoop.eai.testing.race;

import de.diavololoop.eai.individual.IAppeal;
import de.diavololoop.eai.individual.IBody;
import de.diavololoop.eai.individual.IReaction;

import java.util.List;

/**
 *
 *     \    |    /       INs: d(Left), d(right),d(diagLeft), d(diagRight), d(front)
 *       \  |  /              v, a, t(v), a(v)
 *   ----   __    ----
 *         /  \          OUTs: a, a(v)
 *         |  |
 *         \__/
 *
 *
 */
public class Car implements IBody {

    @Override
    public List<IAppeal> getApeals() {
        return null;
    }

    @Override
    public List<IReaction> getReactions() {
        return null;
    }

    @Override
    public IBody reproduce() {
        return null;
    }
}
