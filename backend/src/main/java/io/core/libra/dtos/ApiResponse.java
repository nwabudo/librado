package io.core.libra.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.core.libra.exception.Messages;
import lombok.ToString;

import java.util.Date;

@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private String timestamp;
    private String message;
    private boolean status;
    private T data;

    public ApiResponse(T data) {
        timestamp = new Date().toString();
        this.status = true;
        this.message = Messages.SUCCESS.getMessage();
        this.data = data;
    }

    public ApiResponse(String message, boolean status) {
        timestamp = new Date().toString();
        this.status = status;
        this.message = message;
        this.data = null;
    }

    public ApiResponse(String message) {
        timestamp = new Date().toString();
        this.status = true;
        this.message = message;
        this.data = null;
    }

    public boolean getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

}

