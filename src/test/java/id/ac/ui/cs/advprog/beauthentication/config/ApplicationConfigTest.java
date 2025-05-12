package id.ac.ui.cs.advprog.beauthentication.config;

import id.ac.ui.cs.advprog.beauthentication.model.User;
import id.ac.ui.cs.advprog.beauthentication.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ApplicationConfigTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationConfiguration authenticationConfiguration;

    @InjectMocks
    private ApplicationConfig applicationConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    class UserDetailsServiceTests {
        @Test
        void userFound_ReturnsUser() {
            User mockUser = mock(User.class);
            when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(mockUser));
            
            UserDetailsService userDetailsService = applicationConfig.userDetailsService();
            Object result = userDetailsService.loadUserByUsername("test@example.com");
            
            assertNotNull(userDetailsService);
            assertEquals(mockUser, result);
            verify(userRepository).findByEmail("test@example.com");
        }

        @Test
        void userNotFound_ThrowsException() {
            when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
            
            UserDetailsService userDetailsService = applicationConfig.userDetailsService();
            
            assertThrows(UsernameNotFoundException.class,
                    () -> userDetailsService.loadUserByUsername("nonexistent@example.com"));
            verify(userRepository).findByEmail("nonexistent@example.com");
        }
    }

    @Nested
    class AuthenticationTests {
        @Test
        void authenticationProvider_ReturnsProvider() {
            DaoAuthenticationProvider authProvider = (DaoAuthenticationProvider) applicationConfig.authenticationProvider();
            
            assertNotNull(authProvider);
        }

        @Test
        void authenticationManager_ReturnsManager() throws Exception {
            AuthenticationManager mockAuthManager = mock(AuthenticationManager.class);
            when(authenticationConfiguration.getAuthenticationManager()).thenReturn(mockAuthManager);
            
            AuthenticationManager result = applicationConfig.authenticationManager(authenticationConfiguration);
            
            assertNotNull(result);
            assertEquals(mockAuthManager, result);
            verify(authenticationConfiguration).getAuthenticationManager();
        }
    }

    @Nested
    class PasswordEncoderTests {
        @Test
        void returnsCorrectBCryptEncoder() {
            var encoder = applicationConfig.passwordEncoder();
            
            assertNotNull(encoder);
            assertTrue(encoder instanceof BCryptPasswordEncoder);
        }
    }
}