package id.ac.ui.cs.advprog.beauthentication.repository;

import id.ac.ui.cs.advprog.beauthentication.enums.Role;
import id.ac.ui.cs.advprog.beauthentication.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
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
        testUser = createTestUser();
        userRepository.deleteAll();
    }

    @Nested
    class FindByEmailTests {
        @Test
        void whenUserExists_shouldReturnUser() {
            persistTestUser();

            Optional<User> found = userRepository.findByEmail(TEST_EMAIL);

            assertTrue(found.isPresent());
            assertEquals(TEST_EMAIL, found.get().getEmail());
        }

        @Test
        void whenUserDoesNotExist_shouldReturnEmpty() {
            Optional<User> found = userRepository.findByEmail("nonexistent@example.com");

            assertFalse(found.isPresent());
        }
    }

    @Nested
    class ExistsByEmailTests {
        @Test
        void whenUserExists_shouldReturnTrue() {
            persistTestUser();

            boolean exists = userRepository.existsByEmail(TEST_EMAIL);

            assertTrue(exists);
        }

        @Test
        void whenUserDoesNotExist_shouldReturnFalse() {
            boolean exists = userRepository.existsByEmail("nonexistent@example.com");

            assertFalse(exists);
        }
    }

    @Nested
    class ExistsByNikTests {
        @Test
        void whenUserExists_shouldReturnTrue() {
            persistTestUser();

            boolean exists = userRepository.existsByNik(TEST_NIK);

            assertTrue(exists);
        }

        @Test
        void whenUserDoesNotExist_shouldReturnFalse() {
            boolean exists = userRepository.existsByNik("9876543210987654");

            assertFalse(exists);
        }
    }

    @Nested
    class SaveTests {
        @Test
        void shouldGenerateId() {
            User savedUser = userRepository.save(testUser);

            assertNotNull(savedUser.getId());
        }

        @Test
        void whenDuplicateEmail_shouldFailWithConstraintViolation() {
            persistTestUser();

            User duplicateEmailUser = createUserWithDuplicateEmail();

            assertThrows(Exception.class, () -> {
                userRepository.save(duplicateEmailUser);
                entityManager.flush();
            });
        }

        @Test
        void whenDuplicateNik_shouldFailWithConstraintViolation() {
            persistTestUser();

            User duplicateNikUser = createUserWithDuplicateNik();

            assertThrows(Exception.class, () -> {
                userRepository.save(duplicateNikUser);
                entityManager.flush();
            });
        }
    }

    private User createTestUser() {
        return User.builder()
                .email(TEST_EMAIL)
                .password("password123")
                .name("Test User")
                .nik(TEST_NIK)
                .address("Test Address")
                .phoneNumber("1234567890")
                .role(Role.PACILIAN)
                .build();
    }

    private void persistTestUser() {
        entityManager.persist(testUser);
        entityManager.flush();
    }

    private User createUserWithDuplicateEmail() {
        return User.builder()
                .email(TEST_EMAIL)
                .password("password456")
                .name("Another User")
                .nik("9876543210987654")
                .address("Another Address")
                .phoneNumber("9876543210")
                .role(Role.CAREGIVER)
                .build();
    }

    private User createUserWithDuplicateNik() {
        return User.builder()
                .email("another@example.com")
                .password("password456")
                .name("Another User")
                .nik(TEST_NIK)
                .address("Another Address")
                .phoneNumber("9876543210")
                .role(Role.CAREGIVER)
                .build();
    }
}