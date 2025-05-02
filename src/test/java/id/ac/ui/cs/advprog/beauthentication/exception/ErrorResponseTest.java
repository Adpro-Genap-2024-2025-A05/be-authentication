package id.ac.ui.cs.advprog.beauthentication.exception;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    @Test
    void builderCreatesErrorResponse() {
        LocalDateTime timestamp = LocalDateTime.now();
        Map<String, String> details = new HashMap<>();
        details.put("field", "error message");
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(timestamp)
                .status(400)
                .error("Bad Request")
                .message("Invalid input")
                .details(details)
                .path("/api/endpoint")
                .build();
        
        assertEquals(timestamp, errorResponse.getTimestamp());
        assertEquals(400, errorResponse.getStatus());
        assertEquals("Bad Request", errorResponse.getError());
        assertEquals("Invalid input", errorResponse.getMessage());
        assertEquals(details, errorResponse.getDetails());
        assertEquals("/api/endpoint", errorResponse.getPath());
    }

    @Test
    void noArgsConstructorCreatesEmptyErrorResponse() {
        ErrorResponse errorResponse = ErrorResponse.builder().build();
        
        assertNull(errorResponse.getTimestamp());
        assertEquals(0, errorResponse.getStatus());
        assertNull(errorResponse.getError());
        assertNull(errorResponse.getMessage());
        assertNull(errorResponse.getDetails());
        assertNull(errorResponse.getPath());
    }

    @Test
    void gettersAndSettersWorkCorrectly() {
        ErrorResponse errorResponse = ErrorResponse.builder().build();
        LocalDateTime timestamp = LocalDateTime.now();
        Map<String, String> details = new HashMap<>();
        details.put("field", "error message");
        
        errorResponse.setTimestamp(timestamp);
        errorResponse.setStatus(400);
        errorResponse.setError("Bad Request");
        errorResponse.setMessage("Invalid input");
        errorResponse.setDetails(details);
        errorResponse.setPath("/api/endpoint");
        
        assertEquals(timestamp, errorResponse.getTimestamp());
        assertEquals(400, errorResponse.getStatus());
        assertEquals("Bad Request", errorResponse.getError());
        assertEquals("Invalid input", errorResponse.getMessage());
        assertEquals(details, errorResponse.getDetails());
        assertEquals("/api/endpoint", errorResponse.getPath());
    }

    @Test
    void equalsAndHashCodeWorkCorrectly() {
        LocalDateTime timestamp = LocalDateTime.now();
        Map<String, String> details = new HashMap<>();
        details.put("field", "error message");
        
        ErrorResponse errorResponse1 = ErrorResponse.builder()
                .timestamp(timestamp)
                .status(400)
                .error("Bad Request")
                .message("Invalid input")
                .details(details)
                .path("/api/endpoint")
                .build();
        
        ErrorResponse errorResponse2 = ErrorResponse.builder()
                .timestamp(timestamp)
                .status(400)
                .error("Bad Request")
                .message("Invalid input")
                .details(details)
                .path("/api/endpoint")
                .build();
        
        ErrorResponse errorResponse3 = ErrorResponse.builder()
                .timestamp(timestamp)
                .status(500)
                .error("Server Error")
                .message("Internal server error")
                .details(null)
                .path("/api/different")
                .build();
        
        assertEquals(errorResponse1, errorResponse2);
        assertNotEquals(errorResponse1, errorResponse3);
        assertEquals(errorResponse1.hashCode(), errorResponse2.hashCode());
        assertNotEquals(errorResponse1.hashCode(), errorResponse3.hashCode());
    }
}