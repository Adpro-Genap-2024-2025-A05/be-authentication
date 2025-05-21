package id.ac.ui.cs.advprog.beauthentication.repository;

import id.ac.ui.cs.advprog.beauthentication.model.ConsultationHistory;
import id.ac.ui.cs.advprog.beauthentication.model.Pacilian;
import id.ac.ui.cs.advprog.beauthentication.model.Caregiver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConsultationHistoryRepository extends JpaRepository<ConsultationHistory, String> {
    List<ConsultationHistory> findByPacilian(Pacilian pacilian);
    List<ConsultationHistory> findByCaregiver(Caregiver caregiver);
}