package id.ac.ui.cs.advprog.beauthentication.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mock.http.MockHttpInputMessage;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    @Mock
    private WebRequest request;

    @Mock
    private MethodArgumentNotValidException methodArgumentNotValidException;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(request.getDescription(false)).thenReturn("uri=/api/test");
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);
    }

    @Test
    void handleValidationExceptionsReturnsValidationErrors() {
        List<FieldError> fieldErrors = new ArrayList<>();
        FieldError error1 = new FieldError("object", "field1", "Field 1 error");
        FieldError error2 = new FieldError("object", "field2", null);
        fieldErrors.add(error1);
        fieldErrors.add(error2);

        when(bindingResult.getAllErrors()).thenReturn(new ArrayList<>(fieldErrors));

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleValidationExceptions(
                methodArgumentNotValidException, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getStatus());
        assertEquals("Validation Error", errorResponse.getError());
        assertEquals("Please check the input fields", errorResponse.getMessage());
        assertNotNull(errorResponse.getDetails());

        @SuppressWarnings("unchecked")
        Map<String, String> details = (Map<String, String>) errorResponse.getDetails();
        assertEquals("Field 1 error", details.get("field1"));
        assertEquals("Invalid value", details.get("field2")); // Should get the default "Invalid value"
    }

    @Test
    void handleMethodNotSupportedReturnsMethodNotAllowedStatus() {

        Collection<String> supportedMethods = Arrays.asList("GET", "PUT");
        HttpRequestMethodNotSupportedException ex = new HttpRequestMethodNotSupportedException("POST",
                supportedMethods);

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleMethodNotSupported(ex, request);

        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED.value(), errorResponse.getStatus());
        assertEquals("Method Not Allowed", errorResponse.getError());
        assertTrue(errorResponse.getMessage().contains("POST"));
        assertTrue(errorResponse.getMessage().contains("GET, PUT"));
    }

    @Test
    void handleMediaTypeNotSupportedReturnsUnsupportedMediaTypeStatus() {
        HttpMediaTypeNotSupportedException ex = new HttpMediaTypeNotSupportedException("Unsupported media type");
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleMediaTypeNotSupported(ex, request);

        assertEquals(HttpStatus.UNSUPPORTED_MEDIA_TYPE, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), errorResponse.getStatus());
        assertEquals("Unsupported Media Type", errorResponse.getError());
        assertTrue(errorResponse.getMessage().contains("application/json"));
    }

    @Test
    void handleMessageNotReadableReturnsBadRequestStatus() {
        HttpInputMessage inputMessage = new MockHttpInputMessage("Invalid content".getBytes());
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException(
                "Message not readable", inputMessage);

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleMessageNotReadable(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getStatus());
        assertEquals("Malformed Request Body", errorResponse.getError());
        assertTrue(errorResponse.getMessage().contains("JSON"));
    }

    @Test
    void handleMissingParamsReturnsBadRequestStatus() {
        MissingServletRequestParameterException ex = new MissingServletRequestParameterException(
                "param", "String");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleMissingParams(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getStatus());
        assertEquals("Missing Parameter", errorResponse.getError());
        assertTrue(errorResponse.getMessage().contains("param"));
        assertTrue(errorResponse.getMessage().contains("String"));
    }

    @Test
    void handleMissingHeadersReturnsBadRequestStatus() {
        MissingRequestHeaderException ex = new MissingRequestHeaderException("Authorization", null);

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleMissingHeaders(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getStatus());
        assertEquals("Missing Header", errorResponse.getError());
        assertTrue(errorResponse.getMessage().contains("Authorization"));
    }

    @Test
    void handleTypeMismatchReturnsBadRequestStatus() {
        Class<?> requiredType = Integer.class;
        MethodArgumentTypeMismatchException ex = new MethodArgumentTypeMismatchException(
                "value", requiredType, "paramName", null, new Exception());

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleTypeMismatch(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getStatus());
        assertEquals("Type Mismatch", errorResponse.getError());
        assertTrue(errorResponse.getMessage().contains("paramName"));
        assertTrue(errorResponse.getMessage().contains("Integer"));
    }

    @Test
    void handleBadCredentialsExceptionReturnsUnauthorizedStatus() {
        BadCredentialsException ex = new BadCredentialsException("Bad credentials");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleBadCredentialsException(ex, request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(HttpStatus.UNAUTHORIZED.value(), errorResponse.getStatus());
        assertEquals("Authentication Failed", errorResponse.getError());
        assertEquals("Invalid username or password", errorResponse.getMessage());
    }

    @Test
    void handleUsernameNotFoundExceptionReturnsUnauthorizedStatus() {
        UsernameNotFoundException ex = new UsernameNotFoundException("User not found");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleUsernameNotFoundException(ex, request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(HttpStatus.UNAUTHORIZED.value(), errorResponse.getStatus());
        assertEquals("Authentication Failed", errorResponse.getError());
        assertEquals("User not found", errorResponse.getMessage());
    }

    @Test
    void handleAccountStatusExceptionsReturnsForbiddenStatus() {
        LockedException ex = new LockedException("Account locked");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleAccountStatusExceptions(ex, request);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(HttpStatus.FORBIDDEN.value(), errorResponse.getStatus());
        assertEquals("Account Error", errorResponse.getError());
        assertEquals("Account locked", errorResponse.getMessage());
    }

    @Test
    void handleExpiredJwtExceptionReturnsUnauthorizedStatus() {
        ExpiredJwtException ex = new ExpiredJwtException(null, null, "JWT expired");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleExpiredJwtException(ex, request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(HttpStatus.UNAUTHORIZED.value(), errorResponse.getStatus());
        assertEquals("JWT Error", errorResponse.getError());
        assertTrue(errorResponse.getMessage().contains("expired"));
    }

    @Test
    void handleSignatureExceptionReturnsUnauthorizedStatus() {
        SignatureException ex = new SignatureException("Invalid signature");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleSignatureException(ex, request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(HttpStatus.UNAUTHORIZED.value(), errorResponse.getStatus());
        assertEquals("JWT Error", errorResponse.getError());
        assertEquals("Invalid authentication token", errorResponse.getMessage());
    }

    @Test
    void handleAccessDeniedExceptionReturnsForbiddenStatus() {
        AccessDeniedException ex = new AccessDeniedException("Access denied");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleAccessDeniedException(ex, request);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(HttpStatus.FORBIDDEN.value(), errorResponse.getStatus());
        assertEquals("Access Denied", errorResponse.getError());
        assertTrue(errorResponse.getMessage().contains("permission"));
    }

    @Test
    void handleIllegalArgumentExceptionReturnsBadRequestStatus() {
        IllegalArgumentException ex = new IllegalArgumentException("Invalid argument");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleIllegalArgumentException(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getStatus());
        assertEquals("Invalid Request", errorResponse.getError());
        assertEquals("Invalid argument", errorResponse.getMessage());
    }

    @Test
    void handleAllUncaughtExceptionReturnsInternalServerErrorStatus() {
        Exception ex = new Exception("Unexpected error");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleAllUncaughtException(ex, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorResponse.getStatus());
        assertEquals("Server Error", errorResponse.getError());
        assertTrue(errorResponse.getMessage().contains("unexpected error"));
    }
}