package com.raf.rafnews.entities.enums;

public enum Status {
    ACTIVE("active"),
    INACTIVE("inactive");


    private String value;

    Status(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Status fromString(String value) {
        for (Status status : Status.values()) {
            if (status.getValue().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid Status value: " + value);
    }
}
