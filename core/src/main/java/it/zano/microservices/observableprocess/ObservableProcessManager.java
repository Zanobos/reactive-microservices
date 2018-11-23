package it.zano.microservices.observableprocess;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author a.zanotti
 * @since 22/11/2018
 */
public abstract class ObservableProcessManager<STATE, TRANSITION, IDTYPE,
        OPROC extends ObservableProcess<STATE, IDTYPE>, TASK extends BaseTransitionTask<TRANSITION, IDTYPE>> {

    private final static Logger logger = LoggerFactory.getLogger(ObservableProcessManager.class);

    private final ObservableProcessPersistenceManager<STATE,IDTYPE, OPROC> persistenceManager;
    private final ObservableProcessProperties<STATE, TRANSITION> properties;
    private final TransitionTaskFactory<TRANSITION, IDTYPE, TASK> transitionTaskFactory;
    private final ExecutorService executorService;

    protected ObservableProcessManager(ObservableProcessPersistenceManager<STATE, IDTYPE, OPROC> persistenceManager,
                                    ObservableProcessProperties<STATE, TRANSITION> properties,
                                    TransitionTaskFactory<TRANSITION, IDTYPE, TASK> transitionTaskFactory) {
        this.persistenceManager = persistenceManager;
        this.properties = properties;
        this.transitionTaskFactory = transitionTaskFactory;
        this.executorService = Executors.newWorkStealingPool();
    }

    public OPROC executeEvent(TRANSITION transition, IDTYPE processId) {

        //Lock to protect against multiple events on same process. If we are in HA, this must be shared
        persistenceManager.lock(processId);
        //Starting point
        OPROC observableProcess = persistenceManager.retrieveObservableProcess(processId);
        //If the process requested does not exists, then it's a new process. I have to create one
        if(observableProcess == null) {
            observableProcess = createNew(processId);
        }

        try {
            //From the configured properties, I get the following expected state
            STATE startingState = properties.getTransitions().get(transition).getFrom();
            //If the transition is expecting the process to be in the right state, I go on, if not, abort
            if(startingState == observableProcess.getActualState()) {
                logger.info("Start dealing with {} event", transition);
                //I get from the configuration the landing state after the transition
                STATE landingState = properties.getTransitions().get(transition).getTo();
                //I create using a factory a task, that will be runned in a new thread. I specify the transition
                TASK task = transitionTaskFactory.createTask(transition, processId);
                //I execute the task in a new thread
                executorService.execute(task);
                //While the thread is executing, I update the state - usually something ACTION_X_IN_PROGRESS
                observableProcess.setActualState(landingState);
                //I save back the process in the persistence
                persistenceManager.saveObservableProcess(observableProcess);
                //I notify all the threads that the process has changed status. Again, if we there are multiple JVMs
                //this condition must be shared in some way
                persistenceManager.signalAll(processId);
                logger.info("Finished dealing with {} event", transition);
            } else {
                logger.warn("Event {} discarded because actual state is {} while expected state is {}",
                        transition, observableProcess.getActualState(), startingState);
            }
        } finally {
            persistenceManager.unlock(processId);
        }

        return observableProcess;
    }

    public OPROC getObservableProcessStatus(IDTYPE processId) {

        //Lock to protect against multiple events on same process. If we are in HA, this must be shared
        persistenceManager.lock(processId);
        //Starting point. If the process is null, I asked for a non existent id
        OPROC observableProcess = persistenceManager.retrieveObservableProcess(processId);

        //I get the last observed state
        STATE lastObservedState = observableProcess.getLastObservedState();
        try {
            //In a while (to protect against spurious awakening) I get back the process from the persistence and
            // I check if the status is different from the last observed. If the status is the same of the last
            //time observed, I wait
            while ((observableProcess = persistenceManager.retrieveObservableProcess(processId)).getActualState()
                    .equals(lastObservedState)) {
                //The amount to wait is different for each state. This allow us - for example, the last one to
                //return immediately
                Integer timeout = properties.getStates().get(observableProcess.getActualState()).getTimeout();
                //Await on condition, and with a timeout
                boolean await = persistenceManager.await(processId, timeout);
                if(!await) {
                    logger.info("Request timeout!");
                    break;
                }
            }
            //In any case, If I exit the loop, I have observed the process, and align the state consequently
            observableProcess.observe();
            //Save back the process in persistence
            persistenceManager.saveObservableProcess(observableProcess);
        } catch (InterruptedException e) {
            logger.warn(e.getMessage());
        } finally {
            persistenceManager.unlock(processId);
        }
        return observableProcess;

    }

    protected abstract OPROC createNew(IDTYPE processId);

}
