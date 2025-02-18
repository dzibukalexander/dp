package by.brstu.rec.Utils;

import java.io.Serializable;

public class DoctorPatientPageId implements Serializable {
    private Long doctorId;
    private Long patientId;
    private Long pageId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DoctorPatientPageId that = (DoctorPatientPageId) o;

        if (!doctorId.equals(that.doctorId)) return false;
        if (!patientId.equals(that.patientId)) return false;
        return pageId.equals(that.pageId);
    }

    @Override
    public int hashCode() {
        int result = doctorId.hashCode();
        result = 31 * result + patientId.hashCode();
        result = 31 * result + pageId.hashCode();
        return result;
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

    public DoctorPatientPageId() {}

    public DoctorPatientPageId(Long doctorId, Long patientId, Long pageId) {
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.pageId = pageId;
    }
}