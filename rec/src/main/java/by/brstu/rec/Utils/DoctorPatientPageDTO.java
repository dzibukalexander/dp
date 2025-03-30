package by.brstu.rec.Utils;

public class DoctorPatientPageDTO {
    private final DoctorPatientPageId id;

    public DoctorPatientPageDTO(String key) {
        String[] parts = key.split("_");
        this.id = new DoctorPatientPageId(
                Long.parseLong(parts[0]),
                Long.parseLong(parts[1]),
                Long.parseLong(parts[2])
        );
    }

    public DoctorPatientPageId getId() {
        return id;
    }

    public static String toString(DoctorPatientPageId id) {
        return id.getDoctorId() + "_" + id.getPatientId() + "_" + id.getPageId();
    }
}