package by.brstu.rec.entities;

import by.brstu.rec.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    @Column(length = 1000)
    private String description;
    @Column(length = 200)
    private String kaggleURL;

    @Enumerated(EnumType.STRING)
    private Status status;
    private Boolean is_default;

    @OneToOne
    @JoinColumn(name = "position_id")
    private Position position;

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

    public void setIs_default(Boolean is_default) {
        this.is_default = is_default;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
