package com.integnology.phoneapp.model;

public enum StatusEnum {
    PENDING_APPROVAL("pending_approval"),
    PENDING_ACTIVATION("pending_activation"),
    CLOSED("closed"),
    OPEN("open"),
    REJECTED("rejected");

    private String status;

    StatusEnum(String status) {
        this.status = status;
    }
}