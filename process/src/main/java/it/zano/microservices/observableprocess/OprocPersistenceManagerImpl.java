package it.zano.microservices.observableprocess;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author a.zanotti
 * @since 23/11/2018
 */
@Service
public class OprocPersistenceManagerImpl implements ObservableProcessPersistenceManager
        <OprocStateEnum, Integer, OprocImpl>  {

    private final static Logger logger = LoggerFactory.getLogger(OprocPersistenceManagerImpl.class);

    private final Map<Integer, OprocImpl> storage;
    private final ConcurrentMap<Integer,ConcurrencyHelper> concurrencyHelperMap;    

    public OprocPersistenceManagerImpl() {
        storage = new HashMap<>();
        concurrencyHelperMap = new ConcurrentHashMap<>();
    }


    @Override
    public OprocImpl saveObservableProcess(OprocImpl observableProcess) {
        return storage.put(observableProcess.getId(), observableProcess);
    }

    @Override
    public OprocImpl retrieveObservableProcess(Integer id) {
        return storage.get(id);
    }

    @Override
    public void removeObservableProcess(Integer id) {
        storage.remove(id);
    }

    @Override
    public void lock(Integer id) {
        //always succeed in lock (and crate one, with condition, if it does not exist)
        getOrCreateConcurrencyHelper(id).getLock().lock();
    }

    @Override
    public void unlock(Integer id) {
        Lock lock = getLock(id);
        if (lock != null) {
            lock.unlock();
        } else {
            logger.warn("Tried to unlock a non existent lock");
        }
    }

    @Override
    public boolean await(Integer id, long timeout) throws InterruptedException {
        Condition condition = getCondition(id);
        if (condition != null) {
            return condition.await(timeout, TimeUnit.SECONDS);
        } else {
            logger.warn("Tried to await a non condition of a non existent lock");
            return false;
        }

    }

    @Override
    public void signalAll(Integer id) {
        Condition condition = getCondition(id);
        if (condition != null) {
            condition.signalAll();
        } else {
            logger.warn("Tried to signal a condition of a non existent lock");
        }
    }

    private Lock getLock(Integer id) {
        ConcurrencyHelper concurrencyHelper = getConcurrencyHelper(id);
        return concurrencyHelper == null ? null : concurrencyHelper.getLock();
    }

    private Condition getCondition(Integer id) {
        ConcurrencyHelper concurrencyHelper = getConcurrencyHelper(id);
        return concurrencyHelper == null ? null : concurrencyHelper.getChangedStatusCondition();
    }

    private ConcurrencyHelper getOrCreateConcurrencyHelper(Integer id) {
        ConcurrencyHelper concurrencyHelper = new ConcurrencyHelper(new ReentrantLock());
        ConcurrencyHelper oldValue = concurrencyHelperMap.putIfAbsent(id, concurrencyHelper);
        if(oldValue != null) {
            concurrencyHelper = oldValue;
        } else {
            logger.info("First time dealing process id {}, creating a lock", id);
        }
        return concurrencyHelper;
    }

    private ConcurrencyHelper getConcurrencyHelper(Integer id) {
        return concurrencyHelperMap.get(id);
    }


    public static class ConcurrencyHelper {

        private Lock lock;
        private Condition changedStatusCondition;

        public ConcurrencyHelper() {}

        public ConcurrencyHelper(Lock lock) {
            this.lock = lock;
            this.changedStatusCondition = lock.newCondition();
        }

        public Lock getLock() {
            return lock;
        }

        public Condition getChangedStatusCondition() {
            return changedStatusCondition;
        }

    }
}
