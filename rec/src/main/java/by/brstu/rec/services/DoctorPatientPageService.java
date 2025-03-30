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

    public void savePhoto(Long doctorId, Long patientId, MultipartFile file) throws IOException {
        Page page = pageService.uploadPage(file); // Сохраняем фото
        DoctorPatientPage doctorPatientPage = new DoctorPatientPage();
        doctorPatientPage.setDoctor(doctorRepository.findDoctorById(doctorId));
        doctorPatientPage.setPatient(patientRepository.findPatientById(patientId));
        doctorPatientPage.setPage(page);

       // doctorPatientPage.setId(new DoctorPatientPageId(doctorId, patientId, page.getId()));
        doctorPatientPageRepository.save(doctorPatientPage);
    }


    // Общий метод для создания DoctorPatientPage
    private DoctorPatientPage createDoctorPatientPage(Long doctorId, Long patientId, Page page,
                                                      PhotoStatus status, String conclusion) {

        // Создаем ID перед сохранением
        DoctorPatientPageId id = new DoctorPatientPageId(doctorId, patientId, page.getId());

        DoctorPatientPage exchange = new DoctorPatientPage();
        //exchange.setId(id);
        exchange.setDoctor(doctorRepository.findById(doctorId).orElseThrow());
        exchange.setPatient(patientRepository.findById(patientId).orElseThrow());
        exchange.setPage(page);
        exchange.setStatus(status);
        exchange.setConclusion(conclusion);
        return exchange;
    }

    // Пациент отправляет фото доктору
    public DoctorPatientPage sendFileToDoctor(Long doctorId, Long patientId, MultipartFile photo) throws IOException {
        Page page = pageService.uploadPage(photo);
        DoctorPatientPage exchange = createDoctorPatientPage(doctorId,
                patientId,
                page,
                PhotoStatus.OPEN,
                null);
        return doctorPatientPageRepository.save(exchange);
    }

    // Доктор отправляет ответ пациенту
    public void sendResponseToPatient(Long ptdId, String conclusion, MultipartFile responsePhoto) throws IOException {
        DoctorPatientPage originalExchange = doctorPatientPageRepository.findById(ptdId)
                .orElseThrow(() -> new EntityNotFoundException("Request not found"));

        Page responsePage = pageService.uploadPage(responsePhoto);

        // Создаем запись для ответа
        DoctorPatientPage responseExchange = new DoctorPatientPage();
        responseExchange.setDoctor(originalExchange.getDoctor());
        responseExchange.setPatient(originalExchange.getPatient());
        responseExchange.setPage(responsePage);
        responseExchange.setStatus(PhotoStatus.CLOSED);
        responseExchange.setConclusion(conclusion);
        responseExchange.setOriginalRequest(originalExchange); // Устанавливаем связь с исходным запросом

        // Сохраняем ответ
        doctorPatientPageRepository.save(responseExchange);

        // Обновляем исходный запрос
        originalExchange.setStatus(PhotoStatus.CLOSED);
        originalExchange.setResponse(responseExchange); // Устанавливаем обратную связь
        doctorPatientPageRepository.save(originalExchange);
    }

    public DoctorPatientPage findById(Long id) {
        return doctorPatientPageRepository.findById(id).orElseThrow();
    }

    public List<DoctorPatientPage> getOpenRequestsForPatient(Long patientId) {
        return doctorPatientPageRepository.findByPatientIdAndStatusAndResponseIsNull(patientId, PhotoStatus.OPEN);

    }

    public List<DoctorPatientPage> getClosedRequestsForPatient(Long patientId) {
        return doctorPatientPageRepository.findByPatientIdAndStatusAndResponseIsNotNull(patientId, PhotoStatus.CLOSED);
    }

    public List<DoctorPatientPage> getOpenRequestsForDoctor(Long doctorId) {
        return doctorPatientPageRepository.findByDoctorIdAndStatusAndResponseIsNull(doctorId, PhotoStatus.OPEN);
    }

    public List<DoctorPatientPage> getClosedRequestsForDoctor(Long doctorId) {
        return doctorPatientPageRepository.findByDoctorIdAndStatusAndResponseIsNotNull(doctorId, PhotoStatus.CLOSED);    }
}
