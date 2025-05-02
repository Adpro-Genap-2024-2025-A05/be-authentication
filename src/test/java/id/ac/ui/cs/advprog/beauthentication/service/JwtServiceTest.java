package id.ac.ui.cs.advprog.beauthentication.service;

import id.ac.ui.cs.advprog.beauthentication.enums.Role;
import id.ac.ui.cs.advprog.beauthentication.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
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
    private final String TEST_USER_ID = "user-id";
    private final String TEST_EMAIL = "user@example.com";
    private final String TEST_NAME = "User Name";

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secretKey", TEST_SECRET_KEY);
        ReflectionTestUtils.setField(jwtService, "jwtExpirationTime", TEST_EXPIRATION_TIME);
    }

    @Nested
    class TokenGenerationTests {
        @Test
        void generateTokenUserDetailsReturnsValidToken() {
            User user = createTestUser(TEST_USER_ID, TEST_EMAIL, TEST_NAME, Role.PACILIAN);
            String token = jwtService.generateToken(user);

            assertNotNull(token);
            assertEquals(TEST_EMAIL, jwtService.extractUsername(token));

            Claims claims = extractAllClaims(token);
            assertEquals(TEST_USER_ID, claims.get("id"));
            assertEquals(Role.PACILIAN.getValue(), claims.get("role"));
        }

        @Test
        void generateTokenWithExtraClaimsReturnsValidToken() {
            User user = createTestUser(TEST_USER_ID, TEST_EMAIL, TEST_NAME, Role.PACILIAN);
            Map<String, Object> extraClaims = new HashMap<>();
            extraClaims.put("custom", "value");

            String token = jwtService.generateToken(extraClaims, user);

            assertNotNull(token);
            assertEquals(TEST_EMAIL, jwtService.extractUsername(token));

            Claims claims = extractAllClaims(token);
            assertEquals("value", claims.get("custom"));
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

            Claims claims = extractAllClaims(token);
            assertNull(claims.get("id"));
            assertNull(claims.get("role"));
        }

        @Test
        void generateTokenEmptyExtraClaimsOnlyIncludesUsername() {
            User user = createTestUser(TEST_USER_ID, TEST_EMAIL, null, null);
            Map<String, Object> emptyClaims = new HashMap<>();
            
            String token = jwtService.generateToken(emptyClaims, user);

            assertNotNull(token);
            assertEquals(TEST_EMAIL, jwtService.extractUsername(token));

            Claims claims = extractAllClaims(token);
            assertNull(claims.get("id"));
            assertNull(claims.get("role"));
        }
    }

    @Nested
    class TokenValidationTests {
        @Test
        void isTokenValidValidTokenReturnsTrue() {
            User user = createTestUser(TEST_USER_ID, TEST_EMAIL, TEST_NAME, Role.PACILIAN);
            String token = jwtService.generateToken(user);

            boolean isValid = jwtService.isTokenValid(token, user);

            assertTrue(isValid);
        }

        @Test
        void isTokenValidExpiredTokenThrowsExpiredJwtException() {
            User user = createTestUser(TEST_USER_ID, TEST_EMAIL, TEST_NAME, Role.PACILIAN);
            String token = createExpiredToken(user);

            assertThrows(ExpiredJwtException.class, () -> extractAllClaims(token));
            assertFalse(jwtService.isTokenValid(token, user));
        }

        @Test
        void isTokenValidDifferentUserReturnsFalse() {
            User user1 = createTestUser("user1-id", "user1@example.com", "User One", Role.PACILIAN);
            User user2 = createTestUser("user2-id", "user2@example.com", "User Two", Role.CAREGIVER);

            String token = jwtService.generateToken(user1);
            boolean isValid = jwtService.isTokenValid(token, user2);

            assertFalse(isValid);
        }

        @Test
        void isTokenValidNullOrEmptyTokenReturnsFalse() {
            User user = createTestUser(TEST_USER_ID, TEST_EMAIL, TEST_NAME, Role.PACILIAN);

            assertThrows(Exception.class, () -> jwtService.isTokenValid(null, user));
            assertThrows(Exception.class, () -> jwtService.isTokenValid("", user));
        }

        @Test
        void isTokenValidUsernameMismatchReturnsFalse() {
            User tokenUser = createTestUser("user1-id", "user1@example.com", "User One", Role.PACILIAN);
            User differentUser = createTestUser("user2-id", "user2@example.com", "User Two", Role.PACILIAN);

            String token = jwtService.generateToken(tokenUser);

            boolean isValidNonMatching = jwtService.isTokenValid(token, differentUser);
            assertFalse(isValidNonMatching);
            
            boolean isValidMatching = jwtService.isTokenValid(token, tokenUser);
            assertTrue(isValidMatching);
        }

        @Test
        void isTokenValidValidTokenAndMatchingUsernameReturnsTrue() {
            User user = createTestUser(TEST_USER_ID, TEST_EMAIL, TEST_NAME, Role.PACILIAN);
            
            ReflectionTestUtils.setField(jwtService, "jwtExpirationTime", 10000L);
            String token = jwtService.generateToken(user);

            boolean isValid = jwtService.isTokenValid(token, user);
            assertTrue(isValid);
        }

        @Test
        void isTokenValidSimplifiedImplementationBehavesCorrectly() {
            User user = createTestUser(TEST_USER_ID, TEST_EMAIL, TEST_NAME, Role.PACILIAN);
            User differentUser = createTestUser("different-id", "different@example.com", "Different User", Role.PACILIAN);

            String token = jwtService.generateToken(user);
            
            boolean isValidMatching = jwtService.isTokenValid(token, user);
            assertTrue(isValidMatching);
            
            boolean isValidNonMatching = jwtService.isTokenValid(token, differentUser);
            assertFalse(isValidNonMatching);
            
            String expiredToken = createExpiredToken(user);
            boolean isValidExpired = jwtService.isTokenValid(expiredToken, user);
            assertFalse(isValidExpired);
        }
    }

    @Nested
    class ClaimExtractionTests {
        @Test
        void extractClaimReturnsClaimValue() {
            User user = createTestUser(TEST_USER_ID, TEST_EMAIL, TEST_NAME, Role.PACILIAN);
            String token = jwtService.generateToken(user);

            String subject = jwtService.extractClaim(token, Claims::getSubject);

            assertEquals(TEST_EMAIL, subject);
        }

        @Test
        void extractAllClaimsValidTokenReturnsClaims() {
            User user = createTestUser(TEST_USER_ID, TEST_EMAIL, TEST_NAME, Role.PACILIAN);
            String token = jwtService.generateToken(user);

            Claims claims = extractAllClaims(token);

            assertNotNull(claims);
            assertEquals(TEST_EMAIL, claims.getSubject());
            assertEquals(TEST_USER_ID, claims.get("id"));
            assertEquals(Role.PACILIAN.getValue(), claims.get("role"));
        }

        @Test
        void extractClaimWithCustomClaimResolverReturnsClaimValue() {
            User user = createTestUser("custom-id", TEST_EMAIL, TEST_NAME, Role.PACILIAN);
            String token = jwtService.generateToken(user);
            
            String id = jwtService.extractClaim(token, claims -> claims.get("id", String.class));

            assertEquals("custom-id", id);
        }
    }

    @Nested
    class ExpirationTests {
        @Test
        void getExpirationTimeReturnsConfiguredValue() {
            long expirationTime = jwtService.getExpirationTime();

            assertEquals(TEST_EXPIRATION_TIME, expirationTime);
        }

        @Test
        void getRemainingTimeReturnsPositiveValue() {
            User user = createTestUser(TEST_USER_ID, TEST_EMAIL, TEST_NAME, Role.PACILIAN);
            String token = jwtService.generateToken(user);

            long remainingTime = jwtService.getRemainingTime(token);

            assertTrue(remainingTime > 0);
            assertTrue(remainingTime <= TEST_EXPIRATION_TIME);
        }

        @Test
        void getRemainingTimeExpiredTokenReturnsZero() {
            User user = createTestUser(TEST_USER_ID, TEST_EMAIL, TEST_NAME, Role.PACILIAN);
            String token = createExpiredToken(user);

            long remainingTime = jwtService.getRemainingTime(token);
            assertEquals(0, remainingTime);
        }
    }

    private User createTestUser(String id, String email, String name, Role role) {
        return User.builder()
                .id(id)
                .email(email)
                .name(name)
                .role(role)
                .build();
    }

    private String createExpiredToken(User user) {
        ReflectionTestUtils.setField(jwtService, "jwtExpirationTime", 1L);
        String token = jwtService.generateToken(user);

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            fail("Test interrupted");
        }

        return token;
    }

    private Claims extractAllClaims(String token) {
        return ReflectionTestUtils.invokeMethod(jwtService, "extractAllClaims", token);
    }
}