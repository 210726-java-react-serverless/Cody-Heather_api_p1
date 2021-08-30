package com.revature.ncu.web.dtos;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Class for sending a proper Success Response to the user.
 * */
public class SuccessResponse {

    private int statusCode;
    private String message;
    private String timestamp;

    public SuccessResponse(int statusCode, String message){
        this.statusCode = statusCode;
        this.message = message;
        this.timestamp = LocalDateTime.now().toString();
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SuccessResponse that = (SuccessResponse) o;
        return statusCode == that.statusCode && Objects.equals(message, that.message) && Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(statusCode, message, timestamp);
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "statusCode=" + statusCode +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}

