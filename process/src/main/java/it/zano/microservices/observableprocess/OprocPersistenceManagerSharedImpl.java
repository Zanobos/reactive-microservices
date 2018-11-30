package it.zano.microservices.observableprocess;

import it.zano.microservices.webservices.oprocremote.OprocResource;
import it.zano.microservices.webservices.oprocremote.OprocTemplate;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;

/**
 * @author a.zanotti
 * @since 23/11/2018
 */

public class OprocPersistenceManagerSharedImpl implements ObservableProcessPersistenceManager
        <OprocStateEnum, Integer, OprocImpl>  {

    private static final String URI_ID = "id";

    private OprocTemplate oprocTemplate;
    private ConcurrentMap<Integer,Lock> lockMap;


    public OprocPersistenceManagerSharedImpl(OprocTemplate oprocTemplate) {
        this.oprocTemplate = oprocTemplate;
        this.lockMap = new ConcurrentHashMap<>();
    }

    @Override
    public OprocImpl saveObservableProcess(OprocImpl observableProcess) {
        OprocResource oprocResource = new OprocResource();
        oprocResource.setId(observableProcess.getId());
        oprocResource.setActualState(observableProcess.getActualState());
        oprocResource.setLastObservedState(observableProcess.getLastObservedState());

        ResponseEntity<OprocResource> responseEntity = oprocTemplate.postForEntity
                (oprocTemplate.getEndpoint(),oprocResource,OprocResource.class);

        OprocResource body = responseEntity.getBody();
        OprocImpl oproc = new OprocImpl();
        oproc.setId(body.getId());
        oproc.setActualState(body.getActualState());
        oproc.setLastObservedState(body.getLastObservedState());
        return oproc;

    }

    @Override
    public OprocImpl retrieveObservableProcess(Integer id) {
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put(URI_ID, "" + id);
        ResponseEntity<OprocResource> responseEntity = oprocTemplate.getForEntity
                (oprocTemplate.getEndpoint() + "{" + URI_ID + "}", OprocResource.class, uriVariables);
        OprocResource body = responseEntity.getBody();
        OprocImpl oproc = new OprocImpl();
        oproc.setId(body.getId());
        oproc.setActualState(body.getActualState());
        oproc.setLastObservedState(body.getLastObservedState());
        return oproc;
    }

    @Override
    public void removeObservableProcess(Integer id) {
        //TODO should call remote delete
    }

    @Override
    public void lock(Integer id) {
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put(URI_ID, "" + id);
        ResponseEntity<Void> responseEntity = oprocTemplate.getForEntity
                (oprocTemplate.getEndpoint() + "{" + URI_ID + "}", Void.class, uriVariables);
    }

    @Override
    public void unlock(Integer id) {
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put(URI_ID, "" + id);
        ResponseEntity<Void> responseEntity = oprocTemplate.postForEntity
                (oprocTemplate.getEndpoint() + "{" + URI_ID + "}", null,Void.class, uriVariables);
    }

    @Override
    public boolean await(Integer id, long timeout) throws InterruptedException {
        return false;
    }

    @Override
    public void signalAll(Integer id) {

    }


}
