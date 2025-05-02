package id.ac.ui.cs.advprog.beauthentication.exception;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    @Nested
    class ConstructorTests {
        @Test
        void builderCreatesErrorResponse() {
            LocalDateTime timestamp = LocalDateTime.now();
            Map<String, String> details = new HashMap<>();
            details.put("field", "error message");
            
            ErrorResponse errorResponse = createFullErrorResponse(timestamp, details);
            
            assertErrorResponseValues(errorResponse, timestamp, 400, "Bad Request", 
                    "Invalid input", details, "/api/endpoint");
        }

        @Test
        void noArgsConstructorCreatesEmptyErrorResponse() {
            ErrorResponse errorResponse = ErrorResponse.builder().build();
            
            assertErrorResponseValues(errorResponse, null, 0, null, null, null, null);
        }
    }

    @Nested
    class PropertyTests {
        @Test
        void gettersAndSettersWorkCorrectly() {
            ErrorResponse errorResponse = ErrorResponse.builder().build();
            LocalDateTime timestamp = LocalDateTime.now();
            Map<String, String> details = new HashMap<>();
            details.put("field", "error message");
            
            setErrorResponseValues(errorResponse, timestamp, 400, "Bad Request", 
                    "Invalid input", details, "/api/endpoint");
            
            assertErrorResponseValues(errorResponse, timestamp, 400, "Bad Request", 
                    "Invalid input", details, "/api/endpoint");
        }

        @Test
        void equalsAndHashCodeWorkCorrectly() {
            LocalDateTime timestamp = LocalDateTime.now();
            Map<String, String> details = new HashMap<>();
            details.put("field", "error message");
            
            ErrorResponse errorResponse1 = createFullErrorResponse(timestamp, details);
            ErrorResponse errorResponse2 = createFullErrorResponse(timestamp, details);
            ErrorResponse errorResponse3 = createDifferentErrorResponse(timestamp);
            
            assertEquals(errorResponse1, errorResponse2);
            assertNotEquals(errorResponse1, errorResponse3);
            assertEquals(errorResponse1.hashCode(), errorResponse2.hashCode());
            assertNotEquals(errorResponse1.hashCode(), errorResponse3.hashCode());
        }
    }

    private ErrorResponse createFullErrorResponse(LocalDateTime timestamp, Map<String, String> details) {
        return ErrorResponse.builder()
                .timestamp(timestamp)
                .status(400)
                .error("Bad Request")
                .message("Invalid input")
                .details(details)
                .path("/api/endpoint")
                .build();
    }

    private ErrorResponse createDifferentErrorResponse(LocalDateTime timestamp) {
        return ErrorResponse.builder()
                .timestamp(timestamp)
                .status(500)
                .error("Server Error")
                .message("Internal server error")
                .details(null)
                .path("/api/different")
                .build();
    }

    private void setErrorResponseValues(ErrorResponse errorResponse, LocalDateTime timestamp, 
                                       int status, String error, String message, 
                                       Map<String, String> details, String path) {
        errorResponse.setTimestamp(timestamp);
        errorResponse.setStatus(status);
        errorResponse.setError(error);
        errorResponse.setMessage(message);
        errorResponse.setDetails(details);
        errorResponse.setPath(path);
    }

    private void assertErrorResponseValues(ErrorResponse errorResponse, LocalDateTime timestamp, 
                                          int status, String error, String message, 
                                          Map<String, String> details, String path) {
        assertEquals(timestamp, errorResponse.getTimestamp());
        assertEquals(status, errorResponse.getStatus());
        assertEquals(error, errorResponse.getError());
        assertEquals(message, errorResponse.getMessage());
        assertEquals(details, errorResponse.getDetails());
        assertEquals(path, errorResponse.getPath());
    }
}