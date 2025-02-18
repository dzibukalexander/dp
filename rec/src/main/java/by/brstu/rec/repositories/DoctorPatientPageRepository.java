package by.brstu.rec.repositories;

import by.brstu.rec.entities.Doctor;
import by.brstu.rec.entities.DoctorPatientPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorPatientPageRepository extends JpaRepository<DoctorPatientPage, Long> {
    List<DoctorPatientPage> findByDoctor(Doctor doctor);
}
