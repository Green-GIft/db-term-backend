package com.greengift.apiserver._core.errors.exception;

import com.greengift.apiserver._core.errors.BaseException;
import com.greengift.apiserver._core.utils.ApiUtils;
import lombok.Getter;
import org.springframework.http.HttpStatus;

// 인증 안됨 401
@Getter
public class UnauthorizedException extends RuntimeException {
    private final BaseException exception;

    public UnauthorizedException(BaseException exception){
        super(exception.getMessage());
        this.exception = exception;
    }

    public ApiUtils.ApiResult<?> body(){
        return ApiUtils.error(exception);
    }

    public HttpStatus status(){
        return HttpStatus.UNAUTHORIZED;
    }

    public int code() {
        return exception.getCode();
    }
}
