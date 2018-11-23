package it.zano.microservices.observableprocess;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author a.zanotti
 * @since 20/11/2018
 */
public interface ObservableProcessPersistenceManager<STATE, IDTYPE, OPROC extends ObservableProcess<STATE,IDTYPE>> {

    OPROC saveObservableProcess(OPROC observableProcess);
    OPROC retrieveObservableProcess(IDTYPE id);
    Lock getLock(IDTYPE id);
    Condition getChangedStatusCondition(IDTYPE id);
}
