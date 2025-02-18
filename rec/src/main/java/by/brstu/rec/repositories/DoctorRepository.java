package by.brstu.rec.repositories;

import by.brstu.rec.entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    public Doctor findDoctorById(Long id);

    @Query("SELECT d FROM Doctor d " +
            "LEFT JOIN FETCH d.user u " +
            "LEFT JOIN FETCH u.avatar " +
            "LEFT JOIN FETCH d.position")
    public List<Doctor> findAllWithDetails();
}
