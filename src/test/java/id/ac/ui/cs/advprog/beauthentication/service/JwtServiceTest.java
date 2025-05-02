package id.ac.ui.cs.advprog.beauthentication.service;

import id.ac.ui.cs.advprog.beauthentication.enums.Role;
import id.ac.ui.cs.advprog.beauthentication.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    private final String TEST_SECRET_KEY = "0000000000000000000000000000000000000000000000000000000000000000";
    private final long TEST_EXPIRATION_TIME = 3600000;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secretKey", TEST_SECRET_KEY);
        ReflectionTestUtils.setField(jwtService, "jwtExpirationTime", TEST_EXPIRATION_TIME);
    }

    @Test
    void generateTokenUserDetailsReturnsValidToken() {
        User user = User.builder()
                .id("user-id")
                .email("user@example.com")
                .name("User Name")
                .role(Role.PACILIAN)
                .build();

        String token = jwtService.generateToken(user);

        assertNotNull(token);
        assertEquals("user@example.com", jwtService.extractUsername(token));

        Claims claims = ReflectionTestUtils.invokeMethod(jwtService, "extractAllClaims", token);
        assertEquals("user-id", claims.get("id"));
        assertEquals(Role.PACILIAN.getValue(), claims.get("role"));
    }

    @Test
    void generateTokenWithExtraClaimsReturnsValidToken() {
        User user = User.builder()
                .id("user-id")
                .email("user@example.com")
                .name("User Name")
                .role(Role.PACILIAN)
                .build();

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("custom", "value");

        String token = jwtService.generateToken(extraClaims, user);

        assertNotNull(token);
        assertEquals("user@example.com", jwtService.extractUsername(token));

        Claims claims = ReflectionTestUtils.invokeMethod(jwtService, "extractAllClaims", token);
        assertEquals("value", claims.get("custom"));
    }

    @Test
    void isTokenValidValidTokenReturnsTrue() {
        User user = User.builder()
                .id("user-id")
                .email("user@example.com")
                .name("User Name")
                .role(Role.PACILIAN)
                .build();

        String token = jwtService.generateToken(user);

        boolean isValid = jwtService.isTokenValid(token, user);

        assertTrue(isValid);
    }

    @Test
    void isTokenValidExpiredTokenThrowsExpiredJwtException() {
        User user = User.builder()
                .id("user-id")
                .email("user@example.com")
                .name("User Name")
                .role(Role.PACILIAN)
                .build();

        ReflectionTestUtils.setField(jwtService, "jwtExpirationTime", 1L);
        String token = jwtService.generateToken(user);

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            fail("Test interrupted");
        }

        assertThrows(ExpiredJwtException.class,
                () -> ReflectionTestUtils.invokeMethod(jwtService, "extractAllClaims", token));

        assertFalse(jwtService.isTokenValid(token, user));
    }

    @Test
    void isTokenValidDifferentUserReturnsFalse() {
        User user1 = User.builder()
                .id("user1-id")
                .email("user1@example.com")
                .name("User One")
                .role(Role.PACILIAN)
                .build();

        User user2 = User.builder()
                .id("user2-id")
                .email("user2@example.com")
                .name("User Two")
                .role(Role.CAREGIVER)
                .build();

        String token = jwtService.generateToken(user1);

        boolean isValid = jwtService.isTokenValid(token, user2);

        assertFalse(isValid);
    }

    @Test
    void getExpirationTimeReturnsConfiguredValue() {
        long expirationTime = jwtService.getExpirationTime();

        assertEquals(TEST_EXPIRATION_TIME, expirationTime);
    }

    @Test
    void extractClaimReturnsClaimValue() {
        User user = User.builder()
                .id("user-id")
                .email("user@example.com")
                .name("User Name")
                .role(Role.PACILIAN)
                .build();

        String token = jwtService.generateToken(user);

        String subject = jwtService.extractClaim(token, Claims::getSubject);

        assertEquals("user@example.com", subject);
    }

    @Test
    void getRemainingTimeReturnsPositiveValue() {
        User user = User.builder()
                .id("user-id")
                .email("user@example.com")
                .name("User Name")
                .role(Role.PACILIAN)
                .build();

        String token = jwtService.generateToken(user);

        long remainingTime = jwtService.getRemainingTime(token);

        assertTrue(remainingTime > 0);
        assertTrue(remainingTime <= TEST_EXPIRATION_TIME);
    }

    @Test
    void extractAllClaimsValidTokenReturnsClaims() {
        User user = User.builder()
                .id("user-id")
                .email("user@example.com")
                .name("User Name")
                .role(Role.PACILIAN)
                .build();

        String token = jwtService.generateToken(user);

        Claims claims = ReflectionTestUtils.invokeMethod(jwtService, "extractAllClaims", token);

        assertNotNull(claims);
        assertEquals("user@example.com", claims.getSubject());
        assertEquals("user-id", claims.get("id"));
        assertEquals(Role.PACILIAN.getValue(), claims.get("role"));
    }

    @Test
    void generateTokenNonUserDetailsOnlyIncludesUsername() {
        UserDetails nonUserDetails = new org.springframework.security.core.userdetails.User(
                "generic@example.com",
                "password",
                Collections.emptyList());

        String token = jwtService.generateToken(nonUserDetails);

        assertNotNull(token);
        assertEquals("generic@example.com", jwtService.extractUsername(token));

        Claims claims = ReflectionTestUtils.invokeMethod(jwtService, "extractAllClaims", token);
        assertNull(claims.get("id"));
        assertNull(claims.get("role"));
    }

    @Test
    void generateTokenEmptyExtraClaimsOnlyIncludesUsername() {
        User user = User.builder()
                .id("user-id")
                .email("user@example.com")
                .build();

        Map<String, Object> emptyClaims = new HashMap<>();
        String token = jwtService.generateToken(emptyClaims, user);

        assertNotNull(token);
        assertEquals("user@example.com", jwtService.extractUsername(token));

        Claims claims = ReflectionTestUtils.invokeMethod(jwtService, "extractAllClaims", token);
        assertNull(claims.get("id"));
        assertNull(claims.get("role"));
    }

    @Test
    void getRemainingTimeExpiredTokenReturnsZero() {
        User user = User.builder()
                .id("user-id")
                .email("user@example.com")
                .name("User Name")
                .role(Role.PACILIAN)
                .build();

        ReflectionTestUtils.setField(jwtService, "jwtExpirationTime", 1L);
        String token = jwtService.generateToken(user);

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            fail("Test interrupted");
        }

        long remainingTime = jwtService.getRemainingTime(token);
        assertEquals(0, remainingTime);
    }

    @Test
    void extractClaimWithCustomClaimResolverReturnsClaimValue() {
        User user = User.builder()
                .id("custom-id")
                .email("user@example.com")
                .name("User Name")
                .role(Role.PACILIAN)
                .build();

        String token = jwtService.generateToken(user);
        String id = jwtService.extractClaim(token, claims -> claims.get("id", String.class));

        assertEquals("custom-id", id);
    }

    @Test
    void isTokenValidNullOrEmptyTokenReturnsFalse() {
        User user = User.builder()
                .id("user-id")
                .email("user@example.com")
                .name("User Name")
                .role(Role.PACILIAN)
                .build();

        assertThrows(Exception.class, () -> jwtService.isTokenValid(null, user));
        assertThrows(Exception.class, () -> jwtService.isTokenValid("", user));
    }

    @Test
    void isTokenValidExpiredTokenCatchesExceptionAndReturnsFalse() {
        User user = User.builder()
                .id("user-id")
                .email("user@example.com")
                .name("User Name")
                .role(Role.PACILIAN)
                .build();

        ReflectionTestUtils.setField(jwtService, "jwtExpirationTime", 1L);
        String token = jwtService.generateToken(user);

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            fail("Test interrupted");
        }

        assertThrows(ExpiredJwtException.class,
                () -> ReflectionTestUtils.invokeMethod(jwtService, "extractAllClaims", token));
        boolean isValid = jwtService.isTokenValid(token, user);
        assertFalse(isValid);
    }

    @Test
    void isTokenValidUsernameMismatchReturnsFalse() {
        User tokenUser = User.builder()
                .id("user1-id")
                .email("user1@example.com")
                .name("User One")
                .role(Role.PACILIAN)
                .build();

        String token = jwtService.generateToken(tokenUser);

        User differentUser = User.builder()
                .id("user2-id")
                .email("user2@example.com")
                .name("User Two")
                .role(Role.PACILIAN)
                .build();

        boolean isValid = jwtService.isTokenValid(token, differentUser);
        assertFalse(isValid);
        boolean isValidMatching = jwtService.isTokenValid(token, tokenUser);
        assertTrue(isValidMatching);
    }

    @Test
    void isTokenValidValidTokenAndMatchingUsernameReturnsTrue() {
        User user = User.builder()
                .id("user-id")
                .email("user@example.com")
                .name("User Name")
                .role(Role.PACILIAN)
                .build();

        ReflectionTestUtils.setField(jwtService, "jwtExpirationTime", 10000L);
        String token = jwtService.generateToken(user);

        boolean isValid = jwtService.isTokenValid(token, user);
        assertTrue(isValid);
    }

    @Test
    void isTokenValidSimplifiedImplementationBehavesCorrectly() {
        User user = User.builder()
                .id("user-id")
                .email("user@example.com")
                .name("User Name")
                .role(Role.PACILIAN)
                .build();

        String token = jwtService.generateToken(user);
        boolean isValidMatching = jwtService.isTokenValid(token, user);
        assertTrue(isValidMatching);

        User differentUser = User.builder()
                .id("different-id")
                .email("different@example.com")
                .name("Different User")
                .role(Role.PACILIAN)
                .build();
        boolean isValidNonMatching = jwtService.isTokenValid(token, differentUser);
        assertFalse(isValidNonMatching);

        ReflectionTestUtils.setField(jwtService, "jwtExpirationTime", 1L);
        String expiredToken = jwtService.generateToken(user);
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            fail("Test interrupted");
        }

        boolean isValidExpired = jwtService.isTokenValid(expiredToken, user);
        assertFalse(isValidExpired);
    }
}