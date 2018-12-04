package it.zano.microservices.observableprocess;

import it.zano.microservices.observableprocess.tasks.OprocBaseTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author a.zanotti
 * @since 23/11/2018
 */
@Service
public class OprocManagerImpl extends ObservableProcessManager<OprocStateEnum, OprocEventEnum, Integer, OprocEventMessage,
        OprocImpl, OprocBaseTask> {

    @Autowired
    public OprocManagerImpl(ObservableProcessPersistenceManager<OprocStateEnum, Integer, OprocImpl> persistenceManager,
                            ObservableProcessProperties<OprocStateEnum, OprocEventEnum> properties,
                            TaskFactory<OprocEventEnum, OprocStateEnum, Integer, OprocEventMessage, OprocImpl,
                                                                OprocBaseTask> taskFactory) {
        super(persistenceManager, properties, taskFactory);
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
