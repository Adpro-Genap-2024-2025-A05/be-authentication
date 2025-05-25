package id.ac.ui.cs.advprog.beauthentication.repository;

import id.ac.ui.cs.advprog.beauthentication.enums.Role;
import id.ac.ui.cs.advprog.beauthentication.enums.Speciality; 
import id.ac.ui.cs.advprog.beauthentication.model.Caregiver;
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
class CaregiverRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CaregiverRepository caregiverRepository;
    
    private Caregiver testCaregiver;
    private final String TEST_EMAIL = "doctor@example.com";
    private final String TEST_NIK = "9876543210987654";
    private final Speciality TEST_SPECIALITY = Speciality.DOKTER_UMUM; 
    private final String TEST_WORK_ADDRESS = "123 Hospital St.";

    @BeforeEach
    void setUp() {
        testCaregiver = createTestCaregiver();
        caregiverRepository.deleteAll();
    }

    @Nested
    class SaveOperationTests {
        @Test
        void saveShouldPersistCaregiver() {
            Caregiver savedCaregiver = caregiverRepository.save(testCaregiver);
            entityManager.flush();
            entityManager.clear();
            
            Optional<Caregiver> retrievedCaregiver = caregiverRepository.findById(savedCaregiver.getId());
            
            assertTrue(retrievedCaregiver.isPresent());
            assertEquals(TEST_EMAIL, retrievedCaregiver.get().getEmail());
            assertEquals(TEST_SPECIALITY, retrievedCaregiver.get().getSpeciality()); 
            assertEquals(TEST_WORK_ADDRESS, retrievedCaregiver.get().getWorkAddress());
        }
        
        @Test
        void roleSettingShouldDefaultToCaregiverRole() {
            Caregiver caregiverWithoutRole = createCaregiverWithoutRole();
            
            Caregiver savedCaregiver = caregiverRepository.save(caregiverWithoutRole);
            entityManager.flush();
            entityManager.clear();
            
            Optional<Caregiver> retrievedCaregiver = caregiverRepository.findById(savedCaregiver.getId());
            
            assertTrue(retrievedCaregiver.isPresent());
            assertEquals(Role.CAREGIVER, retrievedCaregiver.get().getRole());
        }
    }
    
    @Nested
    class FindOperationTests {
        @Test
        void findByIdWhenCaregiverExistsShouldReturnCaregiver() {
            Caregiver persistedCaregiver = persistTestCaregiver();
            
            Optional<Caregiver> found = caregiverRepository.findById(persistedCaregiver.getId());
            
            assertTrue(found.isPresent());
            assertEquals(TEST_EMAIL, found.get().getEmail());
            assertEquals(TEST_SPECIALITY, found.get().getSpeciality()); 
        }
        
        @Test
        void findByIdWhenCaregiverDoesNotExist_shouldReturnEmpty() {
            Optional<Caregiver> found = caregiverRepository.findById("non-existent-id");
            
            assertFalse(found.isPresent());
        }
        
        @Test
        void findAllWhenMultipleCaregiversExistShouldReturnAllCaregivers() {
            persistTestCaregiver();
            persistAnotherCaregiver();
            
            List<Caregiver> caregivers = caregiverRepository.findAll();
            
            assertEquals(2, caregivers.size());
        }
    }
    
    @Nested
    class DeleteOperationTests {
        @Test
        void deleteShouldRemoveCaregiver() {
            Caregiver persistedCaregiver = persistTestCaregiver();
            
            caregiverRepository.delete(persistedCaregiver);
            entityManager.flush();
            
            Optional<Caregiver> foundCaregiver = caregiverRepository.findById(persistedCaregiver.getId());
            assertFalse(foundCaregiver.isPresent());
        }
    }
    
    private Caregiver createTestCaregiver() {
        return Caregiver.builder()
                .email(TEST_EMAIL)
                .password("doctorPass123")
                .name("Dr. Test")
                .nik(TEST_NIK)
                .address(TEST_WORK_ADDRESS)
                .workAddress(TEST_WORK_ADDRESS)
                .phoneNumber("0811222333")
                .speciality(TEST_SPECIALITY) 
                .role(Role.CAREGIVER)
                .build();
    }
    
    private Caregiver persistTestCaregiver() {
        Caregiver persistedCaregiver = entityManager.persist(testCaregiver);
        entityManager.flush();
        return persistedCaregiver;
    }
    
    private void persistAnotherCaregiver() {
        Caregiver anotherCaregiver = Caregiver.builder()
                .email("another.doctor@example.com")
                .password("anotherPass456")
                .name("Dr. Another")
                .nik("1122334455667788")
                .address("456 Hospital Ave.")
                .workAddress("456 Hospital Ave.")
                .phoneNumber("0899887766")
                .speciality(Speciality.SPESIALIS_ANAK) 
                .role(Role.CAREGIVER)
                .build();
        
        entityManager.persist(anotherCaregiver);
        entityManager.flush();
    }
    
    private Caregiver createCaregiverWithoutRole() {
        return Caregiver.builder()
                .email("noRole@example.com")
                .password("password123")
                .name("No Role Caregiver")
                .nik("5556667778889999")
                .address("789 No Role St.")
                .workAddress("789 No Role St.")
                .phoneNumber("0866778899")
                .speciality(Speciality.SPESIALIS_PENYAKIT_DALAM) 
                .build();
    }
}