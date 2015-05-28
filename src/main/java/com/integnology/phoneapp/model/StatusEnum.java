package com.integnology.phoneapp.model;

public enum StatusEnum {
    PENDING_APPROVAL("pending.approval"),
    PENDING_ACTIVATION("pending.activation"),
    CLOSED("closed"),
    OPEN("open"),
    REJECTED("rejected");

    private String status;

    StatusEnum(String status) {
        this.status = status;
    }

    /**
     * Gets the StatusEnum.
     * @param value String value of the enum
     * @return StatusEnum
     */
    public static StatusEnum getStatus(String value) {
        for (StatusEnum st : StatusEnum.values() ) {
            if (st.status.equals(value)) {
                return st;
            }
        }
        throw new AssertionError("unknown StatusEnum value: " + value);
    }
}