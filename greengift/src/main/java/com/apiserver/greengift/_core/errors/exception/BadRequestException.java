package com.apiserver.greengift._core.errors.exception;

import com.apiserver.greengift._core.errors.BaseException;
import com.apiserver.greengift._core.utils.ApiUtils;
import lombok.Getter;
import org.springframework.http.HttpStatus;

// 유효성 검사 실패, 잘못된 파라메터 요청 400
@Getter
public class BadRequestException extends RuntimeException {
    private final BaseException exception;

    public BadRequestException(BaseException exception){
        super(exception.getMessage());
        this.exception = exception;
    }

    public ApiUtils.ApiResult<?> body(){
        return ApiUtils.error(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    public HttpStatus status(){
        return HttpStatus.BAD_REQUEST;
    }
}