package it.zano.microservices.exception;


import java.util.List;

public class MicroServiceException extends Exception {

    private Error mainError;
    private List<Error> errors;

    public MicroServiceException() {
        super();
    }

    public MicroServiceException(String errorMessage) {
        this.mainError = new Error(errorMessage);
    }

    public MicroServiceException(String errorMessage, String errorCode) {
        this.mainError = new Error(errorMessage,errorCode);
    }

    public MicroServiceException(String errorMessage, String errorCode, ErrorTypeEnum errorType) {
        this.mainError = new Error(errorMessage,errorCode,errorType);
    }

    public MicroServiceException(Exception e) {
        this.mainError = new Error(e);
    }

    public MicroServiceException(Exception e, String errorCode) {
        this.mainError = new Error(e, errorCode);
    }

    public MicroServiceException(Error mainError) {
        this.mainError = mainError;
    }

    public MicroServiceException(MicroServiceExceptionResponse microServiceExceptionResponse) {
        this.mainError = microServiceExceptionResponse.getMainError();
        this.errors = microServiceExceptionResponse.getErrors();
    }

    public MicroServiceException(RestDefaultSpringError springError) {
        this.mainError = new Error(springError);
    }

    public MicroServiceException(RestDefaultSpringError springError, String rawRemoteError) {
        this.mainError = new Error(springError, rawRemoteError);
    }

    public Error getMainError() {
        return mainError;
    }

    public void setMainError(Error mainError) {
        this.mainError = mainError;
    }

    public List<Error> getErrors() {
        return errors;
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }

    @Override
    public String getMessage(){
        if(mainError == null)
            return super.getMessage();
        return mainError.getMessage();
    }
}