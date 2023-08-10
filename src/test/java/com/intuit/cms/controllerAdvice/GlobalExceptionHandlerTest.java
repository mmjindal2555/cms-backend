package com.intuit.cms.controllerAdvice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    @Test
    void testHandleResourceNotFound() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Resource not found");
        // WebRequest webRequest = mock(WebRequest.class);

        ResponseEntity<ErrorResponse> responseEntity = exceptionHandler.handleResourceNotFound(exception);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        ErrorResponse responseBody = responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("Resource not found", responseBody.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, responseBody.getStatus());

    }

    @Test
    void testHandleResourceNotFound2() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Resource not found");
        // WebRequest webRequest = mock(WebRequest.class);

        ResponseEntity<ErrorResponse> responseEntity = exceptionHandler.handleResourceNotFound2(exception);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        ErrorResponse responseBody = responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("Resource not found", responseBody.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, responseBody.getStatus());

    }

    @Test
    void testRequestException() {
        RequestException exception = new RequestException("Bad request");
        // WebRequest webRequest = mock(WebRequest.class);

        ResponseEntity<ErrorResponse> responseEntity = exceptionHandler.requestException(exception);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        ErrorResponse responseBody = responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("Bad request", responseBody.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, responseBody.getStatus());
    }

    @Test
    void testUnauthorizedException() {
        UnauthorizedException exception = new UnauthorizedException("unauth");
        // WebRequest webRequest = mock(WebRequest.class);

        ResponseEntity<ErrorResponse> responseEntity = exceptionHandler.unauthorizedException(exception);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());

        ErrorResponse responseBody = responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("unauth", responseBody.getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED, responseBody.getStatus());
    }
}
