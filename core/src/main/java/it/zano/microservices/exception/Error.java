package it.zano.microservices.exception;


import it.zano.microservices.util.JsonUtils;

import java.io.Serializable;
import java.util.UUID;

public class Error implements Serializable{

	private static final long serialVersionUID = 42L;
    public static final String CODE_GENERIC = "GENERIC";
    public static final String CODE_REMOTE_CALL = "REMOTE_CALL";
    public static final String MESSAGE_UNKNOWN = "Message unspecified";
    public static final String TECH_INFO_PREFIX = "TECH_";

    private String message;
    private String code;
    private ErrorTypeEnum errorType;
    private String additionalInfo;
    private String rawRemoteError;

    public Error(String errorMessage, String errorCode, ErrorTypeEnum errorType) {
        this.message = errorMessage;
        this.code = errorCode;
        this.errorType = errorType;
        this.additionalInfo = TECH_INFO_PREFIX + UUID.randomUUID().toString();
    }

    public Error() {
        this(MESSAGE_UNKNOWN,CODE_GENERIC,ErrorTypeEnum.TECHNICAL);
    }

    public Error(String errorMessage) {
        this(errorMessage,CODE_GENERIC,ErrorTypeEnum.TECHNICAL);
    }

    public Error(String errorMessage, String errorCode) {
        this(errorMessage,errorCode,ErrorTypeEnum.TECHNICAL);
    }

    public Error(Throwable throwable) {
        this(throwable.getMessage(),CODE_GENERIC,ErrorTypeEnum.TECHNICAL);
    }

    public Error(Throwable throwable, String errorCode) {
        this(throwable);
        this.code = errorCode;
    }

    public Error(RestDefaultSpringError springError) {
        this(springError.getException(),CODE_REMOTE_CALL,ErrorTypeEnum.TECHNICAL);
        this.additionalInfo = springError.getMessage();
    }

    public Error(RestDefaultSpringError springError, String rawRemoteError) {
        this(springError);
        this.rawRemoteError = rawRemoteError;
    }

    public String getMessage() { return message;  }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ErrorTypeEnum getErrorType() {
        return errorType;
    }

    public void setErrorType(ErrorTypeEnum errorType) {
        this.errorType = errorType;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public String getRawRemoteError() {
        return rawRemoteError;
    }

    public void setRawRemoteError(String rawRemoteError) {
        this.rawRemoteError = rawRemoteError;
    }

    @Override
    public String toString() {
        return JsonUtils.toJson(this);
    }
}
