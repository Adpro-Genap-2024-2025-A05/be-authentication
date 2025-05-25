package id.ac.ui.cs.advprog.beauthentication.controller;

import id.ac.ui.cs.advprog.beauthentication.dto.*;
import id.ac.ui.cs.advprog.beauthentication.model.Caregiver;
import id.ac.ui.cs.advprog.beauthentication.model.Pacilian;
import id.ac.ui.cs.advprog.beauthentication.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    @PostMapping(path = "/register/pacilian", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<RegisterResponseDto>> registerPacilian(@Valid @RequestBody RegisterPacilianDto registerDto) {
        RegisterResponseDto response = authService.registerPacilian(registerDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDto.success(HttpStatus.CREATED.value(), 
                                        "Pacilian registered successfully", 
                                        response));
    }

    @PostMapping(path = "/register/caregiver", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<RegisterResponseDto>> registerCaregiver(@Valid @RequestBody RegisterCaregiverDto registerDto) {
        RegisterResponseDto response = authService.registerCaregiver(registerDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDto.success(HttpStatus.CREATED.value(), 
                                        "Caregiver registered successfully", 
                                        response));
    }

    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<LoginResponseDto>> login(@Valid @RequestBody LoginDto loginDto) {
        LoginResponseDto response = authService.login(loginDto);
        return ResponseEntity.ok(
                ApiResponseDto.success(HttpStatus.OK.value(), 
                                "Login successful", 
                                response));
    }

    @PostMapping(path = "/logout")
    public ResponseEntity<ApiResponseDto<Void>> logout(HttpServletRequest request) {
        authService.logout();
        return ResponseEntity.ok(
                ApiResponseDto.success(HttpStatus.OK.value(), 
                                "Logged out successfully", 
                                null));
    }

    @PostMapping(path = "/verify", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<TokenVerificationResponseDto>> verifyToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponseDto.error(HttpStatus.UNAUTHORIZED.value(), 
                                        "Invalid authentication token"));
        }

        String token = authHeader.substring(7);
        TokenVerificationResponseDto response = authService.verifyToken(token);

        if (response.isValid()) {
            return ResponseEntity.ok(
                    ApiResponseDto.success(HttpStatus.OK.value(), 
                                    "Token verified successfully", 
                                    response));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponseDto.error(HttpStatus.UNAUTHORIZED.value(), 
                                        "Invalid or expired token"));
        }
    }
}