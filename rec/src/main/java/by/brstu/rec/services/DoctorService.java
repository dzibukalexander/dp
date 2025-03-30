package by.brstu.rec.services;

import by.brstu.rec.entities.Doctor;
import by.brstu.rec.entities.DoctorPatientPage;
import by.brstu.rec.repositories.DoctorPatientPageRepository;
import by.brstu.rec.repositories.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private DoctorPatientPageRepository doctorPatientPageRepository;

    public void save(Doctor doctor) {
        doctorRepository.save(doctor);
    }

    public List<Doctor> findAll() {
        return doctorRepository.findAll();
    }

    public List<Doctor> findAllWithDetails() {
        return doctorRepository.findAllWithDetails();
    }

//    public List<DoctorPatientPage> getPatientPages(Doctor doctor) {
//        return doctorPatientPageRepository.findByDoctor(doctor);
//    }

}
