package id.ac.ui.cs.advprog.beauthentication.repository;

import id.ac.ui.cs.advprog.beauthentication.enums.Role;
import id.ac.ui.cs.advprog.beauthentication.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User testUser;
    private final String TEST_EMAIL = "test@example.com";
    private final String TEST_NIK = "1234567890123456";

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .email(TEST_EMAIL)
                .password("password123")
                .name("Test User")
                .nik(TEST_NIK)
                .address("Test Address")
                .phoneNumber("1234567890")
                .role(Role.PACILIAN)
                .build();
        
        userRepository.deleteAll();
    }

    @Test
    void testFindByEmailWhenUserExistsShouldReturnUser() {
        entityManager.persist(testUser);
        entityManager.flush();
        
        Optional<User> found = userRepository.findByEmail(TEST_EMAIL);
        
        assertTrue(found.isPresent());
        assertEquals(TEST_EMAIL, found.get().getEmail());
    }
    
    @Test
    void testFindByEmailWhenUserDoesNotExistShouldReturnEmpty() {
        Optional<User> found = userRepository.findByEmail("nonexistent@example.com");
        
        assertFalse(found.isPresent());
    }
    
    @Test
    void testExistsByEmailWhenUserExistsShouldReturnTrue() {
        entityManager.persist(testUser);
        entityManager.flush();
        
        boolean exists = userRepository.existsByEmail(TEST_EMAIL);
        
        assertTrue(exists);
    }
    
    @Test
    void testExistsByEmailWhenUserDoesNotExistShouldReturnFalse() {
        boolean exists = userRepository.existsByEmail("nonexistent@example.com");
        
        assertFalse(exists);
    }
    
    @Test
    void testExistsByNikWhenUserExistsShouldReturnTrue() {
        entityManager.persist(testUser);
        entityManager.flush();
        
        boolean exists = userRepository.existsByNik(TEST_NIK);
        
        assertTrue(exists);
    }
    
    @Test
    void testExistsByNikWhenUserDoesNotExistShouldReturnFalse() {
        boolean exists = userRepository.existsByNik("9876543210987654");
        
        assertFalse(exists);
    }
    
    @Test
    void testSaveShouldGenerateId() {
        User savedUser = userRepository.save(testUser);
        
        assertNotNull(savedUser.getId());
    }
    
    @Test
    void testSaveWhenDuplicateEmailShouldFailWithConstraintViolation() {
        entityManager.persist(testUser);
        entityManager.flush();
        
        User duplicateEmailUser = User.builder()
                .email(TEST_EMAIL) 
                .password("password456")
                .name("Another User")
                .nik("9876543210987654") 
                .address("Another Address")
                .phoneNumber("9876543210")
                .role(Role.CAREGIVER)
                .build();
        
        assertThrows(Exception.class, () -> {
            userRepository.save(duplicateEmailUser);
            entityManager.flush();
        });
    }
    
    @Test
    void testSaveWhenDuplicateNikShouldFailWithConstraintViolation() {
        entityManager.persist(testUser);
        entityManager.flush();
        
        User duplicateNikUser = User.builder()
                .email("another@example.com")
                .password("password456")
                .name("Another User")
                .nik(TEST_NIK)
                .address("Another Address")
                .phoneNumber("9876543210")
                .role(Role.CAREGIVER)
                .build();
        
        assertThrows(Exception.class, () -> {
            userRepository.save(duplicateNikUser);
            entityManager.flush();
        });
    }
}