package com.projectTest.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ControllerException extends ResponseStatusException {

    public ControllerException(String message){
      super(HttpStatus.NOT_FOUND, message);
    }

    public ControllerException(String message, HttpStatus status){
      super(status, message);
    }

  }
