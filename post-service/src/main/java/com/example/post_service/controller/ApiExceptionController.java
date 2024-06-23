package com.example.post_service.controller;


import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import static com.example.post_service.utils.ApiUtils.ApiResult;
import static com.example.post_service.utils.ApiUtils.error;

@Slf4j
@RestControllerAdvice
public class ApiExceptionController {


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ApiResult exHandle(Exception e) {
        log.info("[exceptionHandle] Exception ", e);
        return error(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResult illegalExHandle(IllegalArgumentException e) {
        log.info("[exceptionHandle] IllegalArgumentException ", e);
        return error(e, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({NotFoundException.class, NoHandlerFoundException.class})
    public ApiResult notFoundExHandle(Exception e) {
        log.info("[exceptionHandle] NotFoundException ", e);
        return error(e, HttpStatus.NOT_FOUND);
    }

}
