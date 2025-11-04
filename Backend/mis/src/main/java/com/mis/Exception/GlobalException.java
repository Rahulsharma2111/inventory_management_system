package com.mis.Exception;

import com.mis.Entity.Organisation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiError> handleUsernameNotFoundException(UsernameNotFoundException exception){
        ApiError apiError=new ApiError("Not Found: "+exception.getMessage(), HttpStatus.NOT_FOUND);
   return new ResponseEntity<>(apiError,apiError.getHttpStatus());
    }
}
