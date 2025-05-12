package id.ac.ui.cs.advprog.beauthentication.repository;

import id.ac.ui.cs.advprog.beauthentication.enums.Role;
import id.ac.ui.cs.advprog.beauthentication.model.Pacilian;
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

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class PacilianRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PacilianRepository pacilianRepository;
    
    private Pacilian testPacilian;
    private final String TEST_EMAIL = "patient@example.com";
    private final String TEST_NIK = "1111222233334444";
    private final String MEDICAL_HISTORY = "Diagnosed with hypertension in 2022";

    @BeforeEach
    void setUp() {
        testPacilian = createTestPacilian();
        pacilianRepository.deleteAll();
    }

    @Nested
    class SaveOperationTests {
        @Test
        void saveShouldPersistPacilian() {
            Pacilian savedPacilian = pacilianRepository.save(testPacilian);
            
            assertNotNull(savedPacilian.getId());
            assertEquals(TEST_EMAIL, savedPacilian.getEmail());
            assertEquals(TEST_NIK, savedPacilian.getNik());
            assertEquals(MEDICAL_HISTORY, savedPacilian.getMedicalHistory());
            assertEquals(Role.PACILIAN, savedPacilian.getRole());
        }
        
        @Test
        void roleSettingShouldDefaultToPacilian() {
            Pacilian pacilianWithoutRole = createPacilianWithoutRole();
            
            Pacilian savedPacilian = pacilianRepository.save(pacilianWithoutRole);
            entityManager.flush();
            entityManager.clear();
            
            Optional<Pacilian> retrievedPacilian = pacilianRepository.findById(savedPacilian.getId());
            
            assertTrue(retrievedPacilian.isPresent());
            assertEquals(Role.PACILIAN, retrievedPacilian.get().getRole());
        }
    }
    
    @Nested
    class FindOperationTests {
        @Test
        void findByIdWhenPacilianExistsShouldReturnPacilian() {
            Pacilian persistedPacilian = persistTestPacilian();
            
            Optional<Pacilian> found = pacilianRepository.findById(persistedPacilian.getId());
            
            assertTrue(found.isPresent());
            assertEquals(TEST_EMAIL, found.get().getEmail());
            assertEquals(MEDICAL_HISTORY, found.get().getMedicalHistory());
        }
        
        @Test
        void findByIdWhenPacilianDoesNotExistShouldReturnEmpty() {
            Optional<Pacilian> found = pacilianRepository.findById("non-existent-id");
            
            assertFalse(found.isPresent());
        }
        
        @Test
        void findAllWhenMultiplePaciliansExistShouldReturnAllPacilians() {
            persistTestPacilian();
            persistAnotherPacilian();
            
            List<Pacilian> pacilians = pacilianRepository.findAll();
            
            assertEquals(2, pacilians.size());
        }
    }
    
    @Nested
    class DeleteOperationTests {
        @Test
        void deleteShouldRemovePacilian() {
            Pacilian persistedPacilian = persistTestPacilian();
            
            pacilianRepository.delete(persistedPacilian);
            entityManager.flush();
            
            Optional<Pacilian> found = pacilianRepository.findById(persistedPacilian.getId());
            assertFalse(found.isPresent());
        }
    }
    
    @Nested
    class SpecialFieldsTests {
        @Test
        void medicalHistoryFieldShouldPersistLargeText() {
            String longMedicalHistory = createLongMedicalHistory();
            
            testPacilian.setMedicalHistory(longMedicalHistory);
            
            Pacilian savedPacilian = pacilianRepository.save(testPacilian);
            entityManager.flush();
            entityManager.clear();
            
            Optional<Pacilian> retrievedPacilian = pacilianRepository.findById(savedPacilian.getId());
            
            assertTrue(retrievedPacilian.isPresent());
            assertEquals(longMedicalHistory, retrievedPacilian.get().getMedicalHistory());
        }
    }
    
    private Pacilian createTestPacilian() {
        return Pacilian.builder()
                .email(TEST_EMAIL)
                .password("secure123")
                .name("Test Patient")
                .nik(TEST_NIK)
                .address("123 Patient St.")
                .phoneNumber("0811234567")
                .role(Role.PACILIAN)
                .medicalHistory(MEDICAL_HISTORY)
                .build();
    }
    
    private Pacilian persistTestPacilian() {
        Pacilian persistedPacilian = entityManager.persist(testPacilian);
        entityManager.flush();
        return persistedPacilian;
    }
    
    private Pacilian createPacilianWithoutRole() {
        return Pacilian.builder()
                .email("noRole@example.com")
                .password("password123")
                .name("No Role Patient")
                .nik("9999888877776666")
                .address("789 No Role St.")
                .phoneNumber("0855443322")
                .medicalHistory("Healthy")
                .build();
    }
    
    private void persistAnotherPacilian() {
        Pacilian anotherPacilian = Pacilian.builder()
                .email("another@example.com")
                .password("password789")
                .name("Another Patient")
                .nik("5555666677778888")
                .address("456 Patient Ave.")
                .phoneNumber("0877654321")
                .role(Role.PACILIAN)
                .medicalHistory("Allergic to penicillin")
                .build();
        
        entityManager.persist(anotherPacilian);
        entityManager.flush();
    }
    
    private String createLongMedicalHistory() {
        return "Patient has a long history of various conditions including: " +
                "1. Medical Record Panjang 1\n" +
                "2. Medical Record Panjang 2\n" +
                "3. Medical Record Panjang 3\n" +
                "4. Medical Record Panjang 4\n" +
                "5. Medical Record Panjang 5\n" +
                "Regular check up ...\n";
    }
}