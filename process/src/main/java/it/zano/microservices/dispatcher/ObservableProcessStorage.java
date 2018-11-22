package it.zano.microservices.dispatcher;

import it.zano.microservices.model.beans.ObservableProcess;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author a.zanotti
 * @since 20/11/2018
 */
public interface ObservableProcessStorage {

    ObservableProcess saveProcess(ObservableProcess observableProcess);
    ObservableProcess retrieveProcess(Integer id);
    Lock getLock(Integer id);
    Condition getChangedStatusCondition(Integer id);
}
