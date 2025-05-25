package id.ac.ui.cs.advprog.beauthentication.service;

import id.ac.ui.cs.advprog.beauthentication.dto.*;
import id.ac.ui.cs.advprog.beauthentication.model.Caregiver;
import id.ac.ui.cs.advprog.beauthentication.model.Pacilian;

public interface AuthService {
    RegisterResponseDto registerPacilian(RegisterPacilianDto registerDto);
    RegisterResponseDto registerCaregiver(RegisterCaregiverDto registerDto);
    LoginResponseDto login(LoginDto loginDto);
    void logout();
    TokenVerificationResponseDto verifyToken(String token);
    Caregiver getCaregiverByID(String id);
    Pacilian getPacilianByID(String id);

}