package it.zano.microservices.dispatcher;

import it.zano.microservices.model.beans.ObservableProcess; /**
 * @author a.zanotti
 * @since 20/11/2018
 */
public interface ObservableProcessStorage {
    ObservableProcess saveProcess(ObservableProcess observableProcess);

    ObservableProcess retrieveProcess(Integer id);
}
