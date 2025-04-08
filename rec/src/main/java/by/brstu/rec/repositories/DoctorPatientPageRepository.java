package by.brstu.rec.repositories;

import by.brstu.rec.Utils.DoctorPatientPageId;
import by.brstu.rec.entities.Doctor;
import by.brstu.rec.entities.DoctorPatientPage;
import by.brstu.rec.enums.PhotoStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorPatientPageRepository extends JpaRepository<DoctorPatientPage, Long> {
//    List<DoctorPatientPage> findByDoctorIdAndStatus(Long doctorId, PhotoStatus status);
//    List<DoctorPatientPage> findByPatientIdOrderByDateCreatedDesc(Long patientId);
//    List<DoctorPatientPage> findByPatientIdAndStatusOrderByDateCreatedDesc(Long patientId, PhotoStatus photoStatus);
//    List<DoctorPatientPage> findByDoctorIdAndStatusOrderByDateCreatedDesc(Long doctorId, PhotoStatus photoStatus);

    DoctorPatientPage findByRequestPageId(Long pageId);

    // Для доктора: найти все запросы с/без ответов
    List<DoctorPatientPage> findByDoctorIdAndStatus(Long doctorId, PhotoStatus status);
    // Для пациента: найти все запросы с/без ответов
    List<DoctorPatientPage> findByPatientIdAndStatus(Long patientId, PhotoStatus status);

}
