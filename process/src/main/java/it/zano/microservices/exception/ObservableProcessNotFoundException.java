package it.zano.microservices.exception;

/**
 * @author a.zanotti
 * @since 30/11/2018
 */
public class ObservableProcessNotFoundException extends MicroServiceException {

    public ObservableProcessNotFoundException(Integer id) {
        super("Oproc with id " + id + " not found", "404", ErrorTypeEnum.BUSINESS);
    }
}
