package it.zano.microservices.model.entities;

import it.zano.microservices.model.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author a.zanotti
 * @since 15/10/2018
 */
@Entity
@Table(name = "processes")
public class ProcessInfo extends BaseEntity {

    @Column(name = "process_code")
    private String processCode;

    @Column(name = "process_state")
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
