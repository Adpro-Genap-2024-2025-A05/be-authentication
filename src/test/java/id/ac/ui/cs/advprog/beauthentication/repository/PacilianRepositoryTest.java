package id.ac.ui.cs.advprog.beauthentication.repository;

import id.ac.ui.cs.advprog.beauthentication.enums.Role;
import id.ac.ui.cs.advprog.beauthentication.model.Pacilian;
import org.junit.jupiter.api.BeforeEach;
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
        testPacilian = Pacilian.builder()
                .email(TEST_EMAIL)
                .password("secure123")
                .name("Test Patient")
                .nik(TEST_NIK)
                .address("123 Patient St.")
                .phoneNumber("0811234567")
                .role(Role.PACILIAN)
                .medicalHistory(MEDICAL_HISTORY)
                .build();
        
        pacilianRepository.deleteAll();
    }

    @Test
    void testSaveShouldPersistPacilian() {
        Pacilian savedPacilian = pacilianRepository.save(testPacilian);
        
        assertNotNull(savedPacilian.getId());
        assertEquals(TEST_EMAIL, savedPacilian.getEmail());
        assertEquals(TEST_NIK, savedPacilian.getNik());
        assertEquals(MEDICAL_HISTORY, savedPacilian.getMedicalHistory());
        assertEquals(Role.PACILIAN, savedPacilian.getRole());
    }
    
    @Test
    void testFindByIdWhenPacilianExistsShouldReturnPacilian() {
        Pacilian persistedPacilian = entityManager.persist(testPacilian);
        entityManager.flush();
        
        Optional<Pacilian> found = pacilianRepository.findById(persistedPacilian.getId());
        
        assertTrue(found.isPresent());
        assertEquals(TEST_EMAIL, found.get().getEmail());
        assertEquals(MEDICAL_HISTORY, found.get().getMedicalHistory());
    }
    
    @Test
    void testFindByIdWhenPacilianDoesNotExistShouldReturnEmpty() {
        Optional<Pacilian> found = pacilianRepository.findById("non-existent-id");
        
        assertFalse(found.isPresent());
    }
    
    @Test
    void testFindAllWhenMultiplePaciliansExistShouldReturnAllPacilians() {
        entityManager.persist(testPacilian);
        
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
        
        List<Pacilian> pacilians = pacilianRepository.findAll();
        
        assertEquals(2, pacilians.size());
    }
    
    @Test
    void testDeleteShouldRemovePacilian() {
        Pacilian persistedPacilian = entityManager.persist(testPacilian);
        entityManager.flush();
        
        pacilianRepository.delete(persistedPacilian);
        entityManager.flush();
        
        Optional<Pacilian> found = pacilianRepository.findById(persistedPacilian.getId());
        assertFalse(found.isPresent());
    }
    
    @Test
    void testRoleSettingShouldDefaultToPacilian() {
        Pacilian pacilianWithoutRole = Pacilian.builder()
                .email("noRole@example.com")
                .password("password123")
                .name("No Role Patient")
                .nik("9999888877776666")
                .address("789 No Role St.")
                .phoneNumber("0855443322")
                .medicalHistory("Healthy")
                .build();
        
        Pacilian savedPacilian = pacilianRepository.save(pacilianWithoutRole);
        entityManager.flush();
        entityManager.clear();
        
        Optional<Pacilian> retrievedPacilian = pacilianRepository.findById(savedPacilian.getId());
        
        assertTrue(retrievedPacilian.isPresent());
        assertEquals(Role.PACILIAN, retrievedPacilian.get().getRole());
    }
    
    @Test
    void testMedicalHistoryFieldShouldPersistLargeText() {
        String longMedicalHistory = "Patient has a long history of various conditions including: " +
                "1. Hypertension diagnosed in 2015\n" +
                "2. Type 2 Diabetes diagnosed in 2018\n" +
                "3. Underwent knee replacement surgery in 2020\n" +
                "4. Currently on medication for cholesterol management\n" +
                "5. Family history of heart disease\n" +
                "Regular check-ups show stable condition with current medication regimen.";
        
        testPacilian.setMedicalHistory(longMedicalHistory);
        
        Pacilian savedPacilian = pacilianRepository.save(testPacilian);
        entityManager.flush();
        entityManager.clear();
        
        Optional<Pacilian> retrievedPacilian = pacilianRepository.findById(savedPacilian.getId());
        
        assertTrue(retrievedPacilian.isPresent());
        assertEquals(longMedicalHistory, retrievedPacilian.get().getMedicalHistory());
    }
}