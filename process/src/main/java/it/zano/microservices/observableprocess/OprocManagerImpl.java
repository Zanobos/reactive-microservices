package it.zano.microservices.observableprocess;

import it.zano.microservices.observableprocess.tasks.OprocBaseTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author a.zanotti
 * @since 23/11/2018
 */
@Service
public class OprocManagerImpl extends ObservableProcessManager<OprocStateEnum, OprocTransitionEnum, Integer, OprocTransitionMessage,
        OprocImpl, OprocBaseTask> {

    @Autowired
    public OprocManagerImpl(ObservableProcessPersistenceManager<OprocStateEnum, Integer, OprocImpl> persistenceManager,
                            ObservableProcessProperties<OprocStateEnum, OprocTransitionEnum> properties,
                            TransitionTaskFactory<OprocTransitionEnum, OprocStateEnum, Integer, OprocTransitionMessage, OprocImpl,
                                    OprocBaseTask> transitionTaskFactory) {
        super(persistenceManager, properties, transitionTaskFactory);
    }

    @Override
    protected OprocImpl createCancelledProcess(Integer processId) {
        OprocImpl oproc = new OprocImpl();
        oproc.init(processId, OprocStateEnum.CANCELLED);
        return oproc;
    }

    @Override
    protected OprocImpl createNew(Integer processId, Object... args) {
        OprocImpl oproc = new OprocImpl();
        oproc.init(processId, OprocStateEnum.NOT_OBSERVED);
        return oproc;
    }
}
