package it.zano.microservices.layers.controller.soa;

import it.zano.microservices.exception.MicroServiceException;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.ParameterizedType;

/**
 * Created by a.zanotti on 18/01/2018
 */
public abstract class BaseSoaLogicExecutor<REQ extends BaseSoaRequestPayload,RES extends BaseSoaResponsePayload, IREQ, IRES> {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected Mapper mapper;

    public BaseSoaLogicExecutor(Mapper mapper){
        this.mapper = mapper;
    }

    public ResponseEntity<RES> execute(HttpHeaders httpHeaders, REQ request) throws MicroServiceException {
        IREQ innerRequest = mapperInput(httpHeaders, request);
        IRES innerResponse = performBusinessLogic(innerRequest);
        return mapperOutput(innerResponse);
    }

    /**
     * Override the method in case of custom mapping
     * @param httpHeaders the headers request
     * @param request the request that arrived from the client
     * @return an innerRequest to be processed by the performBusinessLogic method
     * @throws MicroServiceException in case of any problem
     */
    protected IREQ mapperInput(HttpHeaders httpHeaders, REQ request) throws MicroServiceException {
        IREQ innerRequest = instantiate(2);
        mapper.map(request, innerRequest);
        return innerRequest;
    }

    /**
     * Perform the business logic over the request
     * @param innerRequest the request transformed by the mapper input
     * @return the innerResponse for the mapper output
     */
    protected abstract IRES performBusinessLogic(IREQ innerRequest);

    /**
     * Override this method in case of custom mapping logic
     * @param innerResponse the output of performBusinessLogic
     * @return the final Response Entity that will go to the client
     * @throws MicroServiceException in case of any problem
     */
    protected ResponseEntity<RES> mapperOutput(IRES innerResponse) throws MicroServiceException {
        RES response = instantiate(1);
        mapper.map(innerResponse, response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @SuppressWarnings("unchecked")
    private <T> T instantiate(int genericTypeNumber) throws MicroServiceException {
        try {
            return (T) ((Class)((ParameterizedType) this.getClass().
                    getGenericSuperclass()).getActualTypeArguments()[genericTypeNumber]).newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new MicroServiceException(e.getMessage());
        }
    }



}
