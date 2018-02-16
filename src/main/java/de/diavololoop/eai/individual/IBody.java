package de.diavololoop.eai.individual;

import java.util.List;

public interface IBody {

    List<IAppeal> getApeals();
    List<IReaction> getReactions();

    IBody reproduce();


}
