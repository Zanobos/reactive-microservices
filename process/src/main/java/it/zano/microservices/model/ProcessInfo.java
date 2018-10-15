package it.zano.microservices.model;

/**
 * @author a.zanotti
 * @since 15/10/2018
 */
public class ProcessInfo {

    private String processCode;
    private String processState;

    public String getProcessCode() {
        return processCode;
    }

    public void setProcessCode(String processCode) {
        this.processCode = processCode;
    }

    public String getProcessState() {
        return processState;
    }

    public void setProcessState(String processState) {
        this.processState = processState;
    }
}
