package com.freenow.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "invalid search query")
public class InvalidSearchQueryException extends Exception
{
    static final long serialVersionUID = -3387516993334229948L;


    public InvalidSearchQueryException(String message)
    {
        super(message);
    }
}
