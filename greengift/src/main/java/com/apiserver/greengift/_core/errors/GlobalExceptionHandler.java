package com.apiserver.greengift._core.errors;

import com.apiserver.greengift._core.errors.exception.*;
import com.apiserver.greengift._core.utils.ApiUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    private <T> ResponseEntity<T> response(T body, HttpStatus status) {
        return new ResponseEntity<>(body, status);
    }

    // Field 내용이 잘못되었을 경우 공통 에러로 처리됨
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> validationException(MethodArgumentNotValidException e) {
        List<ObjectError> errors = e.getBindingResult().getAllErrors();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return response(ApiUtils.error(errors.get(0).getDefaultMessage(), status), status);
    }

    @ExceptionHandler({SQLException.class, DataAccessException.class})
    public ResponseEntity<?> databaseException(){
        BaseException e = BaseException.DATABASE_ERROR;
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return response(ApiUtils.error(e.getMessage(), status), status);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> badRequest(BadRequestException e){
        return response(e.body(), e.status());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<?> unAuthorized(UnauthorizedException e){
        return response(e.body(), e.status());
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<?> forbidden(ForbiddenException e){
        return response(e.body(), e.status());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> notFound(NotFoundException e){
        return response(e.body(), e.status());
    }

    @ExceptionHandler(ServerException.class)
    public ResponseEntity<?> serverError(ServerException e){
        return response(e.body(), e.status());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> unknownServerError(Exception e){
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return response(ApiUtils.error(BaseException.UNEXPECTED_EXCEPTION.getMessage(), status), status);
    }
}