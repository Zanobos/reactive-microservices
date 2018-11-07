package it.zano.microservices.webservices;

/**
 * @author a.zanotti
 * @since 07/11/2018
 */
public abstract class ArchSoapClientProperties {

    private String name;
    private String endpoint;
    private String contextPath;
    private AuthType authType;
    private String username;
    private String password;
    private String certificateName;
    private String certificatePassword;
    private Integer timeout;
    private Integer maxLoggingLength;
    private Boolean mtomEnabled;
    private boolean customSoapHeader;
    private boolean secureProcessingInSoapHeader;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public AuthType getAuthType() {
        return authType;
    }

    public void setAuthType(AuthType authType) {
        this.authType = authType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCertificateName() {
        return certificateName;
    }

    public void setCertificateName(String certificateName) {
        this.certificateName = certificateName;
    }

    public String getCertificatePassword() {
        return certificatePassword;
    }

    public void setCertificatePassword(String certificatePassword) {
        this.certificatePassword = certificatePassword;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Integer getMaxLoggingLength() {
        return maxLoggingLength;
    }

    public void setMaxLoggingLength(Integer maxLoggingLength) {
        this.maxLoggingLength = maxLoggingLength;
    }

    public Boolean getMtomEnabled() {
        return mtomEnabled;
    }

    public void setMtomEnabled(Boolean mtomEnabled) {
        this.mtomEnabled = mtomEnabled;
    }

    public boolean isCustomSoapHeader() {
        return customSoapHeader;
    }

    public void setCustomSoapHeader(boolean customSoapHeader) {
        this.customSoapHeader = customSoapHeader;
    }

    public boolean isSecureProcessingInSoapHeader() {
        return secureProcessingInSoapHeader;
    }

    public void setSecureProcessingInSoapHeader(boolean secureProcessingInSoapHeader) {
        this.secureProcessingInSoapHeader = secureProcessingInSoapHeader;
    }

    public enum AuthType {

        USERNAME_TOKEN("username_token"),
        BASIC("basic"),
        MUTUAL("mutual"),
        NONE("none"),
        UNKNOWN("");

        private String value;

        AuthType(String value) {
            this.value = value;
        }

    }
}
