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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    
    @Nested
    class FindByIdTests {
        @Test
        void whenUserExists_shouldReturnUser() {
            User persistedUser = persistTestUser();

            Optional<User> found = userRepository.findById(persistedUser.getId());

            assertTrue(found.isPresent());
            assertEquals(persistedUser.getId(), found.get().getId());
            assertEquals(TEST_EMAIL, found.get().getEmail());
        }

        @Test
        void whenUserDoesNotExist_shouldReturnEmpty() {
            Optional<User> found = userRepository.findById(UUID.randomUUID().toString());

            assertFalse(found.isPresent());
        }
    }

    @Nested
    class FindAllTests {
        @Test
        void whenUsersExist_shouldReturnAllUsers() {
            persistTestUser();
            User anotherUser = User.builder()
                    .email("another@example.com")
                    .password("password456")
                    .name("Another User")
                    .nik("9876543210987654")
                    .address("Another Address")
                    .phoneNumber("9876543210")
                    .role(Role.CAREGIVER)
                    .build();
            entityManager.persist(anotherUser);
            entityManager.flush();

            List<User> allUsers = userRepository.findAll();

            assertEquals(2, allUsers.size());
        }

        @Test
        void whenNoUsers_shouldReturnEmptyList() {
            List<User> allUsers = userRepository.findAll();

            assertTrue(allUsers.isEmpty());
        }
    }

    @Nested
    class DeleteTests {
        @Test
        void deleteById_whenUserExists_shouldDeleteUser() {
            User persistedUser = persistTestUser();

            userRepository.deleteById(persistedUser.getId());
            entityManager.flush();
            entityManager.clear();

            Optional<User> found = userRepository.findById(persistedUser.getId());
            assertFalse(found.isPresent());
        }

        @Test
        void delete_whenUserExists_shouldDeleteUser() {
            User persistedUser = persistTestUser();

            userRepository.delete(persistedUser);
            entityManager.flush();
            entityManager.clear();

            Optional<User> found = userRepository.findById(persistedUser.getId());
            assertFalse(found.isPresent());
        }
    }
    
    @Nested
    class CountAndExistsTests {
        @Test
        void count_shouldReturnCorrectCount() {
            assertEquals(0, userRepository.count());
            
            persistTestUser();
            assertEquals(1, userRepository.count());

            User anotherUser = User.builder()
                    .email("another@example.com")
                    .password("password456")
                    .name("Another User")
                    .nik("9876543210987654")
                    .address("Another Address")
                    .phoneNumber("9876543210")
                    .role(Role.CAREGIVER)
                    .build();
            entityManager.persist(anotherUser);
            entityManager.flush();

            assertEquals(2, userRepository.count());
        }

        @Test
        void existsById_whenUserExists_shouldReturnTrue() {
            User persistedUser = persistTestUser();

            boolean exists = userRepository.existsById(persistedUser.getId());

            assertTrue(exists);
        }

        @Test
        void existsById_whenUserDoesNotExist_shouldReturnFalse() {
            boolean exists = userRepository.existsById(UUID.randomUUID().toString());

            assertFalse(exists);
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

    private User persistTestUser() {
        entityManager.persist(testUser);
        entityManager.flush();
        return testUser;
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