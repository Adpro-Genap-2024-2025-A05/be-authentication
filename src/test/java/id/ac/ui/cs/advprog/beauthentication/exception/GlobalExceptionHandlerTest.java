package id.ac.ui.cs.advprog.beauthentication.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
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

    @Nested
    class ValidationExceptions {
        @Test
        void handleValidationExceptionsReturnsValidationErrors() {
            List<FieldError> fieldErrors = new ArrayList<>();
            fieldErrors.add(new FieldError("object", "field1", "Field 1 error"));
            fieldErrors.add(new FieldError("object", "field2", null));

            when(bindingResult.getAllErrors()).thenReturn(new ArrayList<>(fieldErrors));

            ResponseEntity<ErrorResponse> response = exceptionHandler.handleValidationExceptions(
                    methodArgumentNotValidException, request);

            assertResponseEntity(response, HttpStatus.BAD_REQUEST, "Validation Error", 
                    "Please check the input fields");
            
            Map<String, String> details = getResponseDetails(response);
            assertEquals("Field 1 error", details.get("field1"));
            assertEquals("Invalid value", details.get("field2"));
        }
    }

    @Nested
    class RequestExceptions {
        @Test
        void handleMethodNotSupportedReturnsMethodNotAllowedStatus() {
            Collection<String> supportedMethods = Arrays.asList("GET", "PUT");
            HttpRequestMethodNotSupportedException ex = new HttpRequestMethodNotSupportedException(
                    "POST", supportedMethods);

            ResponseEntity<ErrorResponse> response = exceptionHandler.handleMethodNotSupported(ex, request);

            assertResponseEntity(response, HttpStatus.METHOD_NOT_ALLOWED, "Method Not Allowed", null);
            assertTrue(response.getBody().getMessage().contains("POST"));
            assertTrue(response.getBody().getMessage().contains("GET, PUT"));
        }

        @Test
        void handleMediaTypeNotSupportedReturnsUnsupportedMediaTypeStatus() {
            HttpMediaTypeNotSupportedException ex = new HttpMediaTypeNotSupportedException("Unsupported media type");
            
            ResponseEntity<ErrorResponse> response = exceptionHandler.handleMediaTypeNotSupported(ex, request);

            assertResponseEntity(response, HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Unsupported Media Type", null);
            assertTrue(response.getBody().getMessage().contains("application/json"));
        }

        @Test
        void handleMessageNotReadableReturnsBadRequestStatus() {
            HttpInputMessage inputMessage = new MockHttpInputMessage("Invalid content".getBytes());
            HttpMessageNotReadableException ex = new HttpMessageNotReadableException(
                    "Message not readable", inputMessage);

            ResponseEntity<ErrorResponse> response = exceptionHandler.handleMessageNotReadable(ex, request);

            assertResponseEntity(response, HttpStatus.BAD_REQUEST, "Malformed Request Body", null);
            assertTrue(response.getBody().getMessage().contains("JSON"));
        }

        @Test
        void handleMissingParamsReturnsBadRequestStatus() {
            MissingServletRequestParameterException ex = new MissingServletRequestParameterException(
                    "param", "String");

            ResponseEntity<ErrorResponse> response = exceptionHandler.handleMissingParams(ex, request);

            assertResponseEntity(response, HttpStatus.BAD_REQUEST, "Missing Parameter", null);
            assertTrue(response.getBody().getMessage().contains("param"));
            assertTrue(response.getBody().getMessage().contains("String"));
        }

        @Test
        void handleMissingHeadersReturnsBadRequestStatus() {
            MissingRequestHeaderException ex = new MissingRequestHeaderException("Authorization", null);

            ResponseEntity<ErrorResponse> response = exceptionHandler.handleMissingHeaders(ex, request);

            assertResponseEntity(response, HttpStatus.BAD_REQUEST, "Missing Header", null);
            assertTrue(response.getBody().getMessage().contains("Authorization"));
        }

        @Test
        void handleTypeMismatchReturnsBadRequestStatus() {
            MethodArgumentTypeMismatchException ex = new MethodArgumentTypeMismatchException(
                    "value", Integer.class, "paramName", null, new Exception());

            ResponseEntity<ErrorResponse> response = exceptionHandler.handleTypeMismatch(ex, request);

            assertResponseEntity(response, HttpStatus.BAD_REQUEST, "Type Mismatch", null);
            assertTrue(response.getBody().getMessage().contains("paramName"));
            assertTrue(response.getBody().getMessage().contains("Integer"));
        }
    }

    @Nested
    class AuthenticationExceptions {
        @Test
        void handleBadCredentialsExceptionReturnsUnauthorizedStatus() {
            BadCredentialsException ex = new BadCredentialsException("Bad credentials");

            ResponseEntity<ErrorResponse> response = exceptionHandler.handleBadCredentialsException(ex, request);

            assertResponseEntity(response, HttpStatus.UNAUTHORIZED, "Authentication Failed", 
                    "Invalid username or password");
        }

        @Test
        void handleUsernameNotFoundExceptionReturnsUnauthorizedStatus() {
            UsernameNotFoundException ex = new UsernameNotFoundException("User not found");

            ResponseEntity<ErrorResponse> response = exceptionHandler.handleUsernameNotFoundException(ex, request);

            assertResponseEntity(response, HttpStatus.UNAUTHORIZED, "Authentication Failed", 
                    "User not found");
        }

        @Test
        void handleAccountStatusExceptionsReturnsForbiddenStatus() {
            LockedException ex = new LockedException("Account locked");

            ResponseEntity<ErrorResponse> response = exceptionHandler.handleAccountStatusExceptions(ex, request);

            assertResponseEntity(response, HttpStatus.FORBIDDEN, "Account Error", 
                    "Account locked");
        }

        @Test
        void handleExpiredJwtExceptionReturnsUnauthorizedStatus() {
            ExpiredJwtException ex = new ExpiredJwtException(null, null, "JWT expired");

            ResponseEntity<ErrorResponse> response = exceptionHandler.handleExpiredJwtException(ex, request);

            assertResponseEntity(response, HttpStatus.UNAUTHORIZED, "JWT Error", null);
            assertTrue(response.getBody().getMessage().contains("expired"));
        }

        @Test
        void handleSignatureExceptionReturnsUnauthorizedStatus() {
            SignatureException ex = new SignatureException("Invalid signature");

            ResponseEntity<ErrorResponse> response = exceptionHandler.handleSignatureException(ex, request);

            assertResponseEntity(response, HttpStatus.UNAUTHORIZED, "JWT Error", 
                    "Invalid authentication token");
        }

        @Test
        void handleAccessDeniedExceptionReturnsForbiddenStatus() {
            AccessDeniedException ex = new AccessDeniedException("Access denied");

            ResponseEntity<ErrorResponse> response = exceptionHandler.handleAccessDeniedException(ex, request);

            assertResponseEntity(response, HttpStatus.FORBIDDEN, "Access Denied", null);
            assertTrue(response.getBody().getMessage().contains("permission"));
        }
    }

    @Nested
    class GeneralExceptions {
        @Test
        void handleIllegalArgumentExceptionReturnsBadRequestStatus() {
            IllegalArgumentException ex = new IllegalArgumentException("Invalid argument");

            ResponseEntity<ErrorResponse> response = exceptionHandler.handleIllegalArgumentException(ex, request);

            assertResponseEntity(response, HttpStatus.BAD_REQUEST, "Invalid Request", 
                    "Invalid argument");
        }

        @Test
        void handleAllUncaughtExceptionReturnsInternalServerErrorStatus() {
            Exception ex = new Exception("Unexpected error");

            ResponseEntity<ErrorResponse> response = exceptionHandler.handleAllUncaughtException(ex, request);

            assertResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR, "Server Error", null);
            assertTrue(response.getBody().getMessage().contains("unexpected error"));
        }
    }

    private void assertResponseEntity(
            ResponseEntity<ErrorResponse> response, 
            HttpStatus expectedStatus, 
            String expectedError, 
            String expectedMessage) {
        
        assertEquals(expectedStatus, response.getStatusCode());
        
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(expectedStatus.value(), errorResponse.getStatus());
        assertEquals(expectedError, errorResponse.getError());
        
        if (expectedMessage != null) {
            assertEquals(expectedMessage, errorResponse.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, String> getResponseDetails(ResponseEntity<ErrorResponse> response) {
        return (Map<String, String>) response.getBody().getDetails();
    }
}