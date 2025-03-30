package by.brstu.rec.entities;

import by.brstu.rec.Utils.DoctorPatientPageId;
import by.brstu.rec.enums.PhotoStatus;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class DoctorPatientPage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    //@MapsId("doctorId") // Связь с полем doctorId в составном ключе
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @ManyToOne(optional = false)
    //@MapsId("patientId") // Связь с полем patientId в составном ключе
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne(optional = false)
    //@MapsId("pageId") // Связь с полем pageId в составном ключе
    @JoinColumn(name = "page_id")
    private Page page;

    @Nullable
    private String conclusion;

    @Enumerated(EnumType.STRING)
    private PhotoStatus status = PhotoStatus.OPEN;

    // Время отправки фотографии
    private LocalDateTime dateCreated = LocalDateTime.now();

    // Новое поле для хранения отформатированной даты
    @Transient // Поле не сохраняется в базе данных
    private String formattedDateCreated;

    // Ответ на этот запрос (если есть)
    @OneToOne(mappedBy = "originalRequest", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private DoctorPatientPage response;

    // Ссылка на исходный запрос (для ответов)
    @OneToOne
    @JoinColumn(name = "original_request_id")
    private DoctorPatientPage originalRequest;

    public DoctorPatientPage getResponse() {
        return response;
    }

    public void setResponse(DoctorPatientPage response) {
        this.response = response;
    }

    public DoctorPatientPage getOriginalRequest() {
        return originalRequest;
    }

    public void setOriginalRequest(DoctorPatientPage originalRequest) {
        this.originalRequest = originalRequest;
    }

    public String getFormattedDateCreated() {
        return formattedDateCreated;
    }

    public void setFormattedDateCreated(String formattedDateCreated) {
        this.formattedDateCreated = formattedDateCreated;
    }


    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Nullable
    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(@Nullable String conclusion) {
        this.conclusion = conclusion;
    }

    public PhotoStatus getStatus() {
        return status;
    }

    public void setStatus(PhotoStatus status) {
        this.status = status;
    }
}