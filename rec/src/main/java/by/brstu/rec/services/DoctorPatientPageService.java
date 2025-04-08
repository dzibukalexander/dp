package by.brstu.rec.services;

import by.brstu.rec.Utils.DoctorPatientPageId;
import by.brstu.rec.entities.DoctorPatientPage;
import by.brstu.rec.entities.Page;
import by.brstu.rec.enums.PhotoStatus;
import by.brstu.rec.repositories.DoctorPatientPageRepository;
import by.brstu.rec.repositories.DoctorRepository;
import by.brstu.rec.repositories.PatientRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

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

    public DoctorPatientPage findByRequestPageId(Long pageId) {
        return doctorPatientPageRepository.findByRequestPageId(pageId);
    }

    public DoctorPatientPage save(DoctorPatientPage doctorPatientPage) {
        return doctorPatientPageRepository.save(doctorPatientPage);
    }

    public boolean isRequestInProgress(DoctorPatientPage dpg) {
        return dpg.isInProgress();
    }

    // Общий метод для создания DoctorPatientPage
    private DoctorPatientPage createDoctorPatientPage(Long doctorId, Long patientId, Page page,
                                                      PhotoStatus status, String conclusion) {
        DoctorPatientPage exchange = new DoctorPatientPage();
        exchange.setDoctor(doctorRepository.findById(doctorId).orElseThrow());
        exchange.setPatient(patientRepository.findById(patientId).orElseThrow());
        exchange.setRequestPage(page);
        exchange.setStatus(status);
        exchange.setConclusion(conclusion);
        return exchange;
    }

    // Пациент отправляет фото доктору
    public DoctorPatientPage sendFileToDoctor(Long doctorId, Long patientId, MultipartFile photo) throws IOException {
        Page page = pageService.uploadPage(photo);
        DoctorPatientPage request = createDoctorPatientPage(doctorId,
                patientId,
                page,
                PhotoStatus.OPEN,
                null);
        doctorPatientPageRepository.save(request);

        return doctorPatientPageRepository.save(request);
    }

    // Доктор отправляет ответ пациенту
    public void sendResponseToPatient(Long dppId, String conclusion, Page responsePageForTextModel) throws IOException {
        DoctorPatientPage dpp = doctorPatientPageRepository.findById(dppId).orElseThrow();

        if(responsePageForTextModel != null) {
            dpp.setResponsePage(responsePageForTextModel);
        }

        if(dpp.getResponsePage() == null && dpp.getConclusion() == null) {
            throw new IOException("Нужен ответ!");
        }

        dpp.setConclusion(conclusion);
        dpp.setDateClosed(LocalDateTime.now());
        dpp.setStatus(PhotoStatus.CLOSED);
        doctorPatientPageRepository.save(dpp);
    }

    public DoctorPatientPage findById(Long id) {
        return doctorPatientPageRepository.findById(id).orElseThrow();
    }

    public List<DoctorPatientPage> getOpenRequestsForPatient(Long patientId) {
        return doctorPatientPageRepository.findByPatientIdAndStatus(patientId, PhotoStatus.OPEN);

    }

    public List<DoctorPatientPage> getClosedRequestsForPatient(Long patientId) {
        return doctorPatientPageRepository.findByPatientIdAndStatus(patientId, PhotoStatus.CLOSED);
    }

    public List<DoctorPatientPage> getOpenRequestsForDoctor(Long doctorId) {
        return doctorPatientPageRepository.findByDoctorIdAndStatus(doctorId, PhotoStatus.OPEN);
    }

    public List<DoctorPatientPage> getClosedRequestsForDoctor(Long doctorId) {
        return doctorPatientPageRepository.findByDoctorIdAndStatus(doctorId, PhotoStatus.CLOSED);    }
}
