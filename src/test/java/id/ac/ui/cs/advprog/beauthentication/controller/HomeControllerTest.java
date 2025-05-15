package id.ac.ui.cs.advprog.beauthentication.controller;

import id.ac.ui.cs.advprog.beauthentication.dto.ApiResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HomeControllerTest {

    private HomeController homeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        homeController = new HomeController();
    }

    @Test
    void healthCheck_shouldReturnUpStatus() {
        ResponseEntity<ApiResponseDto<Map<String, String>>> response = homeController.healthCheck();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApiResponseDto<Map<String, String>> body = response.getBody();
        assertNotNull(body);
        assertEquals(HttpStatus.OK.value(), body.getStatus());
        assertEquals("Service is up and running", body.getMessage());
        assertNotNull(body.getTimestamp());
        
        Map<String, String> data = body.getData();
        assertNotNull(data);
        assertEquals("UP", data.get("status"));
        assertEquals("Back-End Authentication & Profile API", data.get("service"));
    }
    
    @Test
    void healthCheck_shouldNotReturnNullResponse() {
        ResponseEntity<ApiResponseDto<Map<String, String>>> response = homeController.healthCheck();

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getTimestamp());
        assertNotNull(response.getBody().getData());
    }
}