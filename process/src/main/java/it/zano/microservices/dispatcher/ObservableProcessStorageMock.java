package it.zano.microservices.dispatcher;

import it.zano.microservices.model.beans.ObservableProcess;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author a.zanotti
 * @since 20/11/2018
 */
@Service
public class ObservableProcessStorageMock implements ObservableProcessStorage{

    private Map<Integer, ObservableProcess> storage;

    public ObservableProcessStorageMock() {
        storage = new HashMap<>();
    }

    @Override
    public ObservableProcess saveProcess(ObservableProcess observableProcess) {
        return storage.put(observableProcess.getId(), observableProcess);
    }

    @Override
    public ObservableProcess retrieveProcess(Integer id) {
        return storage.get(id);
    }
}
