package id.ac.ui.cs.advprog.beauthentication.dto;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

class ApiResponseDtoTest {

    @Test
    void creatingApiResponseDto_shouldSetAllFields() {
        int status = HttpStatus.OK.value();
        String message = "Test message";
        String testData = "Test data";
        ZonedDateTime now = ZonedDateTime.now();

        ApiResponseDto<String> response = ApiResponseDto.<String>builder()
                .status(status)
                .message(message)
                .timestamp(now)
                .data(testData)
                .build();

        assertEquals(status, response.getStatus());
        assertEquals(message, response.getMessage());
        assertEquals(now, response.getTimestamp());
        assertEquals(testData, response.getData());
    }

    @Test
    void successMethod_shouldCreateSuccessResponse() {
        int status = HttpStatus.OK.value();
        String message = "Success message";
        String testData = "Success data";

        ApiResponseDto<String> response = ApiResponseDto.success(status, message, testData);

        assertEquals(status, response.getStatus());
        assertEquals(message, response.getMessage());
        assertNotNull(response.getTimestamp());
        assertTrue(ChronoUnit.SECONDS.between(response.getTimestamp(), ZonedDateTime.now()) < 2);
        assertEquals(testData, response.getData());
    }

    @Test
    void errorMethod_shouldCreateErrorResponse() {
        int status = HttpStatus.BAD_REQUEST.value();
        String message = "Error message";

        ApiResponseDto<Object> response = ApiResponseDto.error(status, message);

        assertEquals(status, response.getStatus());
        assertEquals(message, response.getMessage());
        assertNotNull(response.getTimestamp());
        assertTrue(ChronoUnit.SECONDS.between(response.getTimestamp(), ZonedDateTime.now()) < 2);
        assertNull(response.getData());
    }

    @Test
    void equals_shouldWorkCorrectly() {
        ZonedDateTime now = ZonedDateTime.now();
        ApiResponseDto<String> response1 = ApiResponseDto.<String>builder()
                .status(200)
                .message("test")
                .timestamp(now)
                .data("data")
                .build();

        ApiResponseDto<String> response2 = ApiResponseDto.<String>builder()
                .status(200)
                .message("test")
                .timestamp(now)
                .data("data")
                .build();

        ApiResponseDto<String> response3 = ApiResponseDto.<String>builder()
                .status(404)
                .message("different")
                .timestamp(now)
                .data("other")
                .build();

        assertEquals(response1, response2);
        assertNotEquals(response1, response3);
    }
}