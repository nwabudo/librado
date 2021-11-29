package io.core.libra.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.core.libra.exception.Messages;
import lombok.ToString;

import java.util.Date;

@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResponse<T> {
    private String timestamp = new Date().toString();
    private String message;
    private boolean status;
    private T data;

    private final String NO_MESSAGE = "No Message";

    public ApiResponse() {
    }

    public ApiResponse(T data) {
        timestamp = new Date().toString();
        this.status = true;
        this.message = Messages.SUCCESS.getMessage();
        this.data = data;
    }

    public ApiResponse(T data, String message, boolean status) {
        timestamp = new Date().toString();
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public ApiResponse(String message, boolean status) {
        timestamp = new Date().toString();
        this.status = status;
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

