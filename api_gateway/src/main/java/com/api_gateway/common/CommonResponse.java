package com.api_gateway.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class CommonResponse<T> {

    private int code;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public CommonResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public CommonResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
    public static <T> CommonResponse<T> ok() {
        return new CommonResponse<>(HttpStatus.OK.value(), "SUCCESS");
    }

    public static <T> CommonResponse<T> ok(T data) {
        return new CommonResponse<>(HttpStatus.OK.value(), "SUCCESS", data);
    }

    public static <T> CommonResponse<T> ok(String message, T data) {
        return new CommonResponse<>(HttpStatus.OK.value(), message, data);
    }

    public static CommonResponse<String> error(String message) {
//        throw new ApiInternalServerErrorException(message);
        throw new RuntimeException();
    }

}
