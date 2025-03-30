package by.brstu.rec.Utils;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class DoctorPatientPageId implements Serializable {


    private Long doctorId;
    private Long patientId;
    private Long pageId;

    public DoctorPatientPageId() {}

    public DoctorPatientPageId(Long doctorId, Long patientId, Long pageId) {
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.pageId = pageId;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public Long getPageId() {
        return pageId;
    }

    public void setPageId(Long pageId) {
        this.pageId = pageId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DoctorPatientPageId that)) return false;
        return Objects.equals(doctorId, that.doctorId) && Objects.equals(patientId, that.patientId) && Objects.equals(pageId, that.pageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(doctorId, patientId, pageId);
    }
}