package it.zano.microservices.observableprocess;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author a.zanotti
 * @since 22/11/2018
 */
public abstract class ObservableProcessManager<STATE, EVENT, IDTYPE, MESSAGE,
        OPROC extends ObservableProcess<STATE, IDTYPE>, TASK extends BaseTask<EVENT, STATE, IDTYPE, MESSAGE, OPROC>> {

    private final static Logger logger = LoggerFactory.getLogger(ObservableProcessManager.class);

    private final ObservableProcessPersistenceManager<STATE, IDTYPE, OPROC> persistenceManager;
    private final ObservableProcessProperties<STATE, EVENT> properties;
    private final TaskFactory<EVENT, STATE, IDTYPE, MESSAGE, OPROC, TASK> taskFactory;
    private final ExecutorService executorService;

    protected ObservableProcessManager(ObservableProcessPersistenceManager<STATE, IDTYPE, OPROC> persistenceManager,
                                       ObservableProcessProperties<STATE, EVENT> properties,
                                       TaskFactory<EVENT, STATE, IDTYPE, MESSAGE, OPROC, TASK> taskFactory) {
        this.persistenceManager = persistenceManager;
        this.properties = properties;
        this.taskFactory = taskFactory;
        this.executorService = Executors.newWorkStealingPool();
    }

    public OPROC executeEvent(EVENT EVENT, IDTYPE processId, Object... eventArgs) {

        //Lock to protect against multiple events on same process. If we are in HA, this must be shared
        persistenceManager.lock(processId);
        //Starting point
        OPROC observableProcess = persistenceManager.retrieveObservableProcess(processId);
        //If the process requested does not exists, then it's a new process. I have to create one
        if (observableProcess == null) {
            observableProcess = createNew(processId, eventArgs);
        }

        try {
            //From the configured properties, I get the following expected state
            STATE startingState = properties.getEvents().get(EVENT).getFrom();
            //If the EVENT is expecting the process to be in the right state, I go on, if not, abort
            if (startingState.equals(observableProcess.getActualState())) {
                logger.info("Start dealing with {} event", EVENT);
                //I get from the configuration the landing state after the EVENT
                STATE landingState = properties.getEvents().get(EVENT).getTo();
                //While the thread is executing, I update the state - usually something ACTION_X_IN_PROGRESS
                observableProcess.setActualState(landingState);
                //I save back the process in the persistence
                persistenceManager.saveObservableProcess(observableProcess);
                //I create using a factory a task, that will be runned in a new thread. I specify the EVENT
                TASK task = taskFactory.createTask(EVENT, observableProcess, eventArgs);
                if (task == null) {
                    logger.info("The factory gave back a null task, putting nothing in execution");
                } else {
                    //I execute the task in a new thread
                    executorService.execute(task);
                }
                //I notify all the threads that the process has changed status. Again, if we there are multiple JVMs
                //this condition must be shared in some way
                persistenceManager.signalAll(processId);
                logger.info("Finished dealing with {} event", EVENT);
            } else {
                logger.warn("Event {} discarded because actual state is {} while expected state is {}",
                        EVENT, observableProcess.getActualState(), startingState);
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
        OPROC observableProcess = null;
        try {
            observableProcess = persistenceManager.retrieveObservableProcess(processId);
            //If process does not exists, error, but not NPE, so we return null
            if (observableProcess == null) {
                throw new ObservableProcessNotFoundException();
            }

            //I get the last observed state
            STATE lastObservedState = observableProcess.getLastObservedState();

            //In a while (to protect against spurious awakening) I get back the process from the persistence and
            // I check if the status is different from the last observed. If the status is the same of the last
            //time observed, I wait

            //NPE here means process cancelled, I catch and throw Process cancelled
            try {
                while ((observableProcess = persistenceManager.retrieveObservableProcess(processId)).getActualState()
                        .equals(lastObservedState)) {
                    //The amount to wait is different for each state. This allow us - for example, the last one to
                    //return immediately
                    Integer timeout = properties.getStates().get(observableProcess.getActualState()).getTimeout();
                    //Await on condition, and with a timeout
                    boolean await = persistenceManager.await(processId, timeout);
                    if (!await) {
                        logger.info("Request timeout!");
                        break;
                    }
                }
            } catch (NullPointerException npe) {
                throw new ObservableProcessCancelledException();
            }
            //In any case, If I exit the loop, I have observed the process, and align the state consequently
            observableProcess.observe();
            //Save back the process in persistence
            persistenceManager.saveObservableProcess(observableProcess);
        } catch (ObservableProcessCancelledException npe) {
            observableProcess = createCancelledProcess(processId);
        } catch (ObservableProcessNotFoundException nfe) {
            logger.info("Asked for a process non existent {}, returning null", processId);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        } finally {
            persistenceManager.unlock(processId);
        }
        return observableProcess;

    }

    public void clearResources(IDTYPE processId) {
        persistenceManager.lock(processId);
        persistenceManager.removeObservableProcess(processId);
        persistenceManager.signalAll(processId);
        persistenceManager.unlock(processId);
    }

    protected abstract OPROC createCancelledProcess(IDTYPE processId);

    protected abstract OPROC createNew(IDTYPE processId, Object... args);

    private static class ObservableProcessCancelledException extends Exception {
    }

    private static class ObservableProcessNotFoundException extends Exception {
    }
}
