package by.brstu.rec.entities;

import by.brstu.rec.enums.ModelIO;
import by.brstu.rec.enums.Status;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.web.bind.annotation.RequestParam;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ai_algo")
public class AIAlgo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 120)
    private String name;

    @Column(nullable = false)
    private ModelIO inputType;

    @Column(nullable = false)
    private ModelIO outputType;

    @Column(nullable = false)
    private String endpoint; // /brainTumorModel/predict/

    @Column(length = 1000)
    private String description;

    @Column(length = 200, nullable = true)
    private String kaggleURL;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    @Column(nullable = false, name = "is_default")
    private Boolean isDefault = false;

    @OneToOne
    @JoinColumn(name = "position_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Position position;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getKaggleURL() {
        return kaggleURL;
    }

    public Status getStatus() {
        return status;
    }

    public ModelIO getInputType() {
        return inputType;
    }

    public ModelIO getOutputType() {
        return outputType;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public Position getPosition() {
        return position;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setKaggleURL(String kaggleURL) {
        this.kaggleURL = kaggleURL;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setInputType(ModelIO inputType) {
        this.inputType = inputType;
    }

    public void setOutputType(ModelIO outputType) {
        this.outputType = outputType;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
