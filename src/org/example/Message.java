package org.example;

import java.io.Serializable;

public class Message implements Serializable {
    private Integer consumerId;
    private final Integer queueingNumber;
    private Boolean confirmed;

    public Message(Integer consumerId, Integer queueingNumber, Boolean confirmed) {
        this.consumerId = consumerId;
        this.queueingNumber = queueingNumber;
        this.confirmed = confirmed;
    }

    public Integer getConsumerId() {
        return consumerId;
    }

    public Integer getQueueingNumber() {
        return queueingNumber;
    }

    public Boolean getConfirmed() {
        return confirmed;
    }

    public void setConsumerId(Integer consumerId) {
        this.consumerId = consumerId;
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }
}
