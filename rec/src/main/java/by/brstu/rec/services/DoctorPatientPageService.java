package by.brstu.rec.services;

import by.brstu.rec.Utils.DoctorPatientPageId;
import by.brstu.rec.entities.DoctorPatientPage;
import by.brstu.rec.entities.Page;
import by.brstu.rec.repositories.DoctorPatientPageRepository;
import by.brstu.rec.repositories.DoctorRepository;
import by.brstu.rec.repositories.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class DoctorPatientPageService {
    @Autowired
    private PageService pageService;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorPatientPageRepository doctorPatientPageRepository;

    public void savePhoto(Long doctorId, Long patientId, MultipartFile file) throws IOException {
        Page page = pageService.uploadPage(file); // Сохраняем фото
        DoctorPatientPage doctorPatientPage = new DoctorPatientPage();
        doctorPatientPage.setDoctor(doctorRepository.findDoctorById(doctorId));
        doctorPatientPage.setPatient(patientRepository.findPatientById(patientId));
        doctorPatientPage.setPage(page);

        doctorPatientPage.setId(new DoctorPatientPageId(doctorId, patientId, page.getId()));
        doctorPatientPageRepository.save(doctorPatientPage);
    }
}
