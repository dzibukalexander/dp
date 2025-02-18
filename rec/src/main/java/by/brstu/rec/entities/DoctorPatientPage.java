package by.brstu.rec.entities;

import by.brstu.rec.Utils.DoctorPatientPageId;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class DoctorPatientPage {
    @EmbeddedId
    private DoctorPatientPageId id;

    @ManyToOne
    @MapsId("doctorId") // Связь с полем doctorId в составном ключе
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @ManyToOne
    @MapsId("patientId") // Связь с полем patientId в составном ключе
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @MapsId("pageId") // Связь с полем pageId в составном ключе
    @JoinColumn(name = "page_id")
    private Page page;

    @Nullable
    private String conclusion;

    // Время отправки фотографии
    private LocalDateTime dateCreated = LocalDateTime.now();

    // Новое поле для хранения отформатированной даты
    @Transient // Поле не сохраняется в базе данных
    private String formattedDateCreated;

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

    public DoctorPatientPageId getId() {
        return id;
    }

    public void setId(DoctorPatientPageId id) {
        this.id = id;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }
}