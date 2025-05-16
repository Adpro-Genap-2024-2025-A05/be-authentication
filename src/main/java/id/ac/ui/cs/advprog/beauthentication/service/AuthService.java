package id.ac.ui.cs.advprog.beauthentication.service;

import id.ac.ui.cs.advprog.beauthentication.dto.*;

public interface AuthService {
    RegisterResponseDto registerPacilian(RegisterPacilianDto registerDto);
    RegisterResponseDto registerCaregiver(RegisterCaregiverDto registerDto);
    LoginResponseDto login(LoginDto loginDto);
    void logout();
    TokenVerificationResponseDto verifyToken(String token);
}