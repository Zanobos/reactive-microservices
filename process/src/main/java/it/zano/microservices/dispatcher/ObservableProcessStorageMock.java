package it.zano.microservices.dispatcher;

import it.zano.microservices.model.beans.ObservableProcess;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author a.zanotti
 * @since 20/11/2018
 */
@Service
public class ObservableProcessStorageMock implements ObservableProcessStorage{

    private final Map<Integer, ObservableProcess> storage;
    private final ConcurrentMap<Integer,ConcurrencyHelper> concurrencyHelperMap;

    public ObservableProcessStorageMock() {
        storage = new HashMap<>();
        concurrencyHelperMap = new ConcurrentHashMap<>();
    }

    @Override
    public ObservableProcess saveProcess(ObservableProcess observableProcess) {
        return storage.put(observableProcess.getId(), observableProcess);
    }

    @Override
    public ObservableProcess retrieveProcess(Integer id) {
        return storage.get(id);
    }

    @Override
    public Lock getLock(Integer id) {
        ConcurrencyHelper concurrencyHelper = new ConcurrencyHelper(new ReentrantLock());
        ConcurrencyHelper oldValue = concurrencyHelperMap.putIfAbsent(id, concurrencyHelper);
        if(oldValue != null)
            concurrencyHelper = oldValue;
        return concurrencyHelper.getLock();
    }

    @Override
    public Condition getChangedStatusCondition(Integer id) {
        //Add a new condition variable for this process
        ConcurrencyHelper concurrencyHelper = new ConcurrencyHelper(new ReentrantLock());
        ConcurrencyHelper oldValue = concurrencyHelperMap.putIfAbsent(id, concurrencyHelper);
        if(oldValue != null)
            concurrencyHelper = oldValue;
        return concurrencyHelper.getChangedStatusCondition();
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
