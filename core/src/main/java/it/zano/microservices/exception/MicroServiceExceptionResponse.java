package it.zano.microservices.exception;


import it.zano.microservices.util.JsonUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;
import java.util.List;

public class MicroServiceExceptionResponse implements Serializable{
	private static final long serialVersionUID = 42L;

    private Error mainError;
    private List<Error> errors;

    public MicroServiceExceptionResponse() {
    }

    public MicroServiceExceptionResponse(Throwable exception) {
        this.mainError = new Error(exception);
    }

    public MicroServiceExceptionResponse(MicroServiceException microServiceException) {
        this.mainError= microServiceException.getMainError();
        this.errors = microServiceException.getErrors();
    }

    public List<Error> getErrors() {
        return errors;
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }

    public Error getMainError() {
        return mainError;
    }

    public void setMainError(Error mainError) {
        this.mainError = mainError;
    }

    public ResponseEntity<MicroServiceExceptionResponse> toErrorPayloadResponseEntity() {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(this, httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public String toString() {
        return JsonUtils.toJson(this);
    }
}
