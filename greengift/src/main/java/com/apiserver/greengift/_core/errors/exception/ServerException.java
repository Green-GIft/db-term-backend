package com.apiserver.greengift._core.errors.exception;

import com.apiserver.greengift._core.errors.BaseException;
import com.apiserver.greengift._core.utils.ApiUtils;
import lombok.Getter;
import org.springframework.http.HttpStatus;

// 서버 에러 500
@Getter
public class ServerException extends RuntimeException {
    private final BaseException exception;

    public ServerException(BaseException exception){
        super(exception.getMessage());
        this.exception = exception;
    }

    public ApiUtils.ApiResult<?> body(){
        return ApiUtils.error(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public HttpStatus status(){
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

}