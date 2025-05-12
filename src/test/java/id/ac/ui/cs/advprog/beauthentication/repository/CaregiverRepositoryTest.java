package id.ac.ui.cs.advprog.beauthentication.repository;

import id.ac.ui.cs.advprog.beauthentication.enums.Role;
import id.ac.ui.cs.advprog.beauthentication.model.Caregiver;
import id.ac.ui.cs.advprog.beauthentication.model.WorkingSchedule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.DayOfWeek;
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
    private final String TEST_SPECIALITY = "Cardiologist";
    private final String TEST_WORK_ADDRESS = "123 Hospital St.";

    @BeforeEach
    void setUp() {
        testCaregiver = createTestCaregiver();
        caregiverRepository.deleteAll();
    }

    @Nested
    class SaveOperationTests {
        @Test
        void saveShouldPersistCaregiverWithWorkingSchedules() {
            Caregiver savedCaregiver = caregiverRepository.save(testCaregiver);
            entityManager.flush();
            entityManager.clear();
            
            Optional<Caregiver> retrievedCaregiver = caregiverRepository.findById(savedCaregiver.getId());
            
            assertTrue(retrievedCaregiver.isPresent());
            assertEquals(TEST_EMAIL, retrievedCaregiver.get().getEmail());
            assertEquals(TEST_SPECIALITY, retrievedCaregiver.get().getSpeciality());
            assertEquals(TEST_WORK_ADDRESS, retrievedCaregiver.get().getWorkAddress());
            assertEquals(2, retrievedCaregiver.get().getWorkingSchedules().size());
            
            List<WorkingSchedule> schedules = retrievedCaregiver.get().getWorkingSchedules();
            assertTrue(schedules.stream().anyMatch(s -> s.getDayOfWeek() == DayOfWeek.MONDAY));
            assertTrue(schedules.stream().anyMatch(s -> s.getDayOfWeek() == DayOfWeek.WEDNESDAY));
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
        void deleteShouldRemoveCaregiverAndWorkingSchedules() {
            Caregiver persistedCaregiver = persistTestCaregiver();
            
            String scheduleId = persistedCaregiver.getWorkingSchedules().get(0).getId();
            
            caregiverRepository.delete(persistedCaregiver);
            entityManager.flush();
            
            Optional<Caregiver> foundCaregiver = caregiverRepository.findById(persistedCaregiver.getId());
            assertFalse(foundCaregiver.isPresent());
            
            List<?> scheduleResults = entityManager.getEntityManager()
                    .createQuery("SELECT w FROM WorkingSchedule w WHERE w.id = :id")
                    .setParameter("id", scheduleId)
                    .getResultList();
            
            assertTrue(scheduleResults.isEmpty());
        }
    }
    
    @Nested
    class WorkingScheduleManagementTests {
        @Test
        void shouldAddAndRemoveSchedules() {
            Caregiver savedCaregiver = caregiverRepository.save(testCaregiver);
            entityManager.flush();
            
            WorkingSchedule fridaySchedule = createWorkingSchedule(DayOfWeek.FRIDAY);
            
            savedCaregiver.addWorkingSchedule(fridaySchedule);
            caregiverRepository.save(savedCaregiver);
            entityManager.flush();
            entityManager.clear();
            
            Optional<Caregiver> afterAddition = caregiverRepository.findById(savedCaregiver.getId());
            assertTrue(afterAddition.isPresent());
            assertEquals(3, afterAddition.get().getWorkingSchedules().size());
            assertTrue(afterAddition.get().getWorkingSchedules().stream()
                    .anyMatch(s -> s.getDayOfWeek() == DayOfWeek.FRIDAY));
            
            Caregiver caregiverToModify = afterAddition.get();
            WorkingSchedule scheduleToRemove = caregiverToModify.getWorkingSchedules().stream()
                    .filter(s -> s.getDayOfWeek() == DayOfWeek.MONDAY)
                    .findFirst()
                    .orElseThrow();
            
            caregiverToModify.removeWorkingSchedule(scheduleToRemove);
            caregiverRepository.save(caregiverToModify);
            entityManager.flush();
            entityManager.clear();
            
            Optional<Caregiver> afterRemoval = caregiverRepository.findById(savedCaregiver.getId());
            assertTrue(afterRemoval.isPresent());
            assertEquals(2, afterRemoval.get().getWorkingSchedules().size());
            assertFalse(afterRemoval.get().getWorkingSchedules().stream()
                    .anyMatch(s -> s.getDayOfWeek() == DayOfWeek.MONDAY));
        }
    }
    
    private Caregiver createTestCaregiver() {
        Caregiver caregiver = Caregiver.builder()
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
        
        caregiver.addWorkingSchedule(createWorkingSchedule(DayOfWeek.MONDAY));
        caregiver.addWorkingSchedule(createWorkingSchedule(DayOfWeek.WEDNESDAY));
        
        return caregiver;
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
                .speciality("Neurologist")
                .role(Role.CAREGIVER)
                .build();
        
        anotherCaregiver.addWorkingSchedule(createWorkingSchedule(DayOfWeek.TUESDAY));
        
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
                .speciality("General Practitioner")
                .build();
    }
    
    private WorkingSchedule createWorkingSchedule(DayOfWeek dayOfWeek) {
        return WorkingSchedule.builder()
                .dayOfWeek(dayOfWeek)
                .build();
    }
}