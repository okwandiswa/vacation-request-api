package com.example.vacation_api;

public enum RequestStatus {
    PENDING("pending"), 
    APPROVED("approved"), 
    REJECTED("rejected");
    
    private final String value;
    
    RequestStatus(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
    
    public static RequestStatus fromString(String status) {
        for (RequestStatus rs : RequestStatus.values()) {
            if (rs.value.equalsIgnoreCase(status)) {
                return rs;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + status);
    }
}
