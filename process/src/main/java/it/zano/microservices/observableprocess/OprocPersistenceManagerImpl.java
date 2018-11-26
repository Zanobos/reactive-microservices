package it.zano.microservices.observableprocess;

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
    public void freeResources(Integer id) {
        storage.remove(id);
        concurrencyHelperMap.remove(id);
    }

    @Override
    public void lock(Integer id) {
        getLock(id).lock();
    }

    @Override
    public void unlock(Integer id) {
        getLock(id).unlock();
    }

    @Override
    public boolean await(Integer id, long timeout) throws InterruptedException {
        return getCondition(id).await(timeout, TimeUnit.SECONDS);
    }

    @Override
    public void signalAll(Integer id) {
        getCondition(id).signalAll();
    }

    private Lock getLock(Integer id) {
        return getConcurrencyHelper(id).getLock();
    }

    private Condition getCondition(Integer id) {
        return getConcurrencyHelper(id).getChangedStatusCondition();
    }

    private ConcurrencyHelper getConcurrencyHelper(Integer id) {
        ConcurrencyHelper concurrencyHelper = new ConcurrencyHelper(new ReentrantLock());
        ConcurrencyHelper oldValue = concurrencyHelperMap.putIfAbsent(id, concurrencyHelper);
        if(oldValue != null)
            concurrencyHelper = oldValue;
        return concurrencyHelper;
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
