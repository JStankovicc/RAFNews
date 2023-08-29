package com.raf.rafnews.entities.enums;

public enum Type {
    ADMIN("admin"),
    CONTENT_CREATOR("content creator");

    private String value;

    Type(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Type fromString(String value) {
        for (Type type : Type.values()) {
            if (type.getValue().equalsIgnoreCase(value)) {
                return type;
            }
        }
        return null;
    }
}
