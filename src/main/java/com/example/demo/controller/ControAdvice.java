package com.example.demo.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControAdvice extends ResponseEntityExceptionHandler{

	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<String> exceptionHandler(NullPointerException e) {

		return new ResponseEntity<String>("Null pointer exception occurred! As a result of:", HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = ArithmeticException.class)
	public ResponseEntity<ExceptionResponse> handleArithmeticException(ArithmeticException e) {
		ExceptionResponse error = new ExceptionResponse();
		error.setMessage("Arithmetic exception occure in : " + e.getMessage());
		HttpStatus status = HttpStatus.BAD_REQUEST;
		return new ResponseEntity<ExceptionResponse>(error, status);
	}

/*	@ExceptionHandler(value = NullPointerException.class)
	public ResponseEntity<ApiError> handleNullPointerException(NullPointerException e) {
		ApiError error = new ApiError();
		error.setException("Null pointer exception: " + e.getMessage());
		HttpStatus status = HttpStatus.NOT_ACCEPTABLE;
		return new ResponseEntity<ApiError>(error, status);
	}

	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<ApiError> handleNullPointerException(Exception e) {
		ApiError error = new ApiError();
		error.setException("Exception: " + e.getMessage());
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<ApiError>(error, status);
	}*/
	
	 @ExceptionHandler(GlobalExceptionConfig.class)
	    public ResponseEntity<Object> handleExceptions( GlobalExceptionConfig exception, WebRequest webRequest) {
	        ExceptionResponse response = new ExceptionResponse();
	  //      response.setDateTime(LocalDateTime.now());
	        response.setMessage("custom exp");
	        ResponseEntity<Object> entity = new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
	        return entity;
	 }
}
