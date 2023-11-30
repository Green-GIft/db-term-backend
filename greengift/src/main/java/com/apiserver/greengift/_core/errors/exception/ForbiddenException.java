package com.apiserver.greengift._core.errors.exception;

import com.apiserver.greengift._core.errors.BaseException;
import com.apiserver.greengift._core.utils.ApiUtils;
import lombok.Getter;
import org.springframework.http.HttpStatus;

// 권한 없음 403
@Getter
public class ForbiddenException extends RuntimeException {
    private final BaseException exception;

    public ForbiddenException(BaseException exception){
        super(exception.getMessage());
        this.exception = exception;
    }

    public ApiUtils.ApiResult<?> body(){
        return ApiUtils.error(exception.getMessage(), HttpStatus.FORBIDDEN);
    }

    public HttpStatus status(){
        return HttpStatus.FORBIDDEN;
    }

}