package it.zano.microservices.rest.controllers;

import it.zano.microservices.rest.resources.OprocResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author a.zanotti
 * @since 23/11/2018
 */
@RestController
@RequestMapping(value = "/oproc", produces = {MediaType.APPLICATION_JSON_VALUE})
public class OprocController {


    private final Map<Integer, OprocResource> storage;
    private final ConcurrentMap<Integer,ConcurrencyHelper> lockMap;

    public OprocController() {
        storage = new HashMap<>();
        lockMap = new ConcurrentHashMap<>();
    }

    @PostMapping
    public ResponseEntity<OprocResource> saveObservableProcess(OprocResource observableProcess) {
        return ResponseEntity.ok(storage.put(observableProcess.getId(), observableProcess));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<OprocResource> retrieveObservableProcess(@PathVariable(value = "id") Integer id) {
        return ResponseEntity.ok(storage.get(id));
    }

    @GetMapping(value = "/{id}/lock")
    public ResponseEntity<Void> lock(@PathVariable(value = "id") Integer id) {
        ConcurrencyHelper concurrencyHelper = new ConcurrencyHelper(new ReentrantLock());
        ConcurrencyHelper oldValue = lockMap.putIfAbsent(id, concurrencyHelper);
        if(oldValue != null)
            concurrencyHelper = oldValue;

        concurrencyHelper.getLock().lock();
        try {
            while(concurrencyHelper.getProcessInUse()){
                concurrencyHelper.getCondiditionProcessInUse().await();
            }
            concurrencyHelper.setProcessInUse(true);
        } catch (InterruptedException e) {
            //Error!
        } finally {
            concurrencyHelper.getLock().unlock();
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/{id}/lock")
    public ResponseEntity<Void> unlock(@PathVariable(value = "id") Integer id) {
        ConcurrencyHelper concurrencyHelper = new ConcurrencyHelper(new ReentrantLock());
        ConcurrencyHelper oldValue = lockMap.putIfAbsent(id, concurrencyHelper);
        if(oldValue != null)
            concurrencyHelper = oldValue;

        concurrencyHelper.getLock().lock();
        try {
            concurrencyHelper.setProcessInUse(false);
            concurrencyHelper.getCondiditionProcessInUse().signalAll();
        } finally {
            concurrencyHelper.getLock().unlock();
        }

        return ResponseEntity.ok().build();
    }

    public static class ConcurrencyHelper {

        private Lock lock;
        private Condition condiditionProcessInUse;
        private Boolean processInUse;

        public ConcurrencyHelper() {}

        public ConcurrencyHelper(Lock lock) {
            this.lock = lock;
            this.condiditionProcessInUse = lock.newCondition();
            this.processInUse = false;
        }

        public Lock getLock() {
            return lock;
        }

        public Condition getCondiditionProcessInUse() {
            return condiditionProcessInUse;
        }

        public Boolean getProcessInUse() {
            return processInUse;
        }

        public void setProcessInUse(Boolean processInUse) {
            this.processInUse = processInUse;
        }
    }

}
