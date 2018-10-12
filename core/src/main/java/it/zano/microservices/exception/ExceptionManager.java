package it.zano.microservices.exception;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author a.zanotti
 */
@ControllerAdvice
public class ExceptionManager extends ResponseEntityExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(ExceptionManager.class);
	private static final Map<String, Method> handlerMap;
	static {
		handlerMap = Arrays.stream(ExceptionManager.class.getDeclaredMethods())
				.filter(method -> method.getParameterCount()==1 && method.isAnnotationPresent(ExceptionHandler.class))
				.flatMap(method -> Arrays.stream(method.getAnnotation(ExceptionHandler.class).value()).map(v -> new Pair<>(v.getSimpleName(), method)))
				.collect(Collectors.toMap(Pair::getKey, Pair::getValue));
	}

	@SuppressWarnings("unchecked")
	private ResponseEntity<MicroServiceExceptionResponse> handle(Exception exception) {
		Method handler = handlerMap.get(exception.getClass().getSimpleName());
		if(handler == null)
			handler = handlerMap.get(Exception.class.getSimpleName());

		try {
			return (ResponseEntity<MicroServiceExceptionResponse>) handler.invoke(this, exception);
		} catch (IllegalAccessException | InvocationTargetException | ClassCastException e) {
			return handleException(e); //This is an impossible case if methods are annotated correctly
		}
	}

	public void handleAndWrite(Exception exception, HttpServletResponse response) throws IOException {
		String s = handle(exception).getBody().toString();
		IOUtils.write(s, response.getWriter());
		response.addHeader("Content-Type", "application/json");
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(value = { MicroServiceException.class })
	protected ResponseEntity<MicroServiceExceptionResponse> handleMicroServiceException(MicroServiceException exception) {
		MicroServiceExceptionResponse response = new MicroServiceExceptionResponse(exception);
		completeLog(exception, !response.getMainError().getErrorType().equals(ErrorTypeEnum.BUSINESS),response.getMainError().getAdditionalInfo());
		return generateErrorPayload(response);
	}

	@ExceptionHandler(value = { Exception.class})
	protected ResponseEntity<MicroServiceExceptionResponse> handleException(Exception exception) {
		MicroServiceExceptionResponse response = new MicroServiceExceptionResponse(exception);
		completeLog(exception, !response.getMainError().getErrorType().equals(ErrorTypeEnum.BUSINESS),response.getMainError().getAdditionalInfo());
		return generateErrorPayload(response);
	}

	@ExceptionHandler(value = { ConstraintViolationException.class })
	protected ResponseEntity<MicroServiceExceptionResponse> handleConstraintViolationException(ConstraintViolationException exception) {
		MicroServiceExceptionResponse response = new MicroServiceExceptionResponse(exception);
		completeLog(exception, !response.getMainError().getErrorType().equals(ErrorTypeEnum.BUSINESS),response.getMainError().getAdditionalInfo());
		return generateErrorPayload(response);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
																  HttpHeaders headers, HttpStatus status, WebRequest request) {
		MicroServiceExceptionResponse response = new MicroServiceExceptionResponse(exception);
		completeLog(exception, !response.getMainError().getErrorType().equals(ErrorTypeEnum.BUSINESS),
				response.getMainError().getAdditionalInfo());
		return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	protected ResponseEntity<Object> handleBindException(BindException exception, HttpHeaders headers, HttpStatus status,
														 WebRequest request) {
		MicroServiceExceptionResponse response = new MicroServiceExceptionResponse(exception);
		completeLog(exception, !response.getMainError().getErrorType().equals(ErrorTypeEnum.BUSINESS), response.getMainError().getAdditionalInfo());
		return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private ResponseEntity<MicroServiceExceptionResponse> generateErrorPayload(MicroServiceExceptionResponse response) {
		ResponseEntity<MicroServiceExceptionResponse> responseEntity = response.toErrorPayloadResponseEntity();
		logger.info("Exiting: {}. Return: {}", ExceptionManager.class.getSimpleName(), responseEntity);
		return responseEntity;
	}

	private <E extends Exception> void completeLog(E exception, boolean printStackTrace, String errorId) {
		String error = String.format("%s: Message %s, Cause %s, ErrorId %s", exception.getClass().getName(), exception.getMessage(), exception.getCause(), errorId);
		if(printStackTrace)
			logger.error(error, exception); //Print also the stacktrace
		else
			logger.error(error);
	}


	public static class Pair<K,V> {

		private K key;
		private V value;

		Pair(K key, V value){
			this.key = key;
			this.value = value;
		}

		public K getKey() {
			return key;
		}

		public V getValue() {
			return value;
		}
	}
}
