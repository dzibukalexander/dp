package by.brstu.rec.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Page {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "originalFileName")
    private String originalFileName;
    @Column(name = "size")
    private Long size;
    @Column(name = "contentType")
    private String contentType;
    @Lob
    private byte[] bytes;


    @OneToMany(mappedBy = "requestPage", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DoctorPatientPage> requestPages;

    @OneToMany(mappedBy = "responsePage", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DoctorPatientPage> responsePages;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getContentType() {
        return contentType;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }


    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    // Getters and Setters
}