package it.zano.microservices.observableprocess;

/**
 * @author a.zanotti
 * @since 20/11/2018
 */
public interface ObservableProcessPersistenceManager<STATE, IDTYPE, OPROC extends ObservableProcess<STATE,IDTYPE>> {

    OPROC saveObservableProcess(OPROC observableProcess);
    OPROC retrieveObservableProcess(IDTYPE id);
    void freeResources(IDTYPE id);
    void lock(IDTYPE id);
    void unlock(IDTYPE id);
    boolean await(IDTYPE id, long timeout) throws InterruptedException;
    void signalAll(IDTYPE id);
}
