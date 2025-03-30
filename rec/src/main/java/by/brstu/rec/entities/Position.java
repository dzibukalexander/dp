package by.brstu.rec.entities;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Data
@Table(name = "positions")
public class Position {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Transient
    private AIAlgo aiAlgo;

    public Position() {};

    public Position(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName(){
        return name;
    }

    public AIAlgo getAiAlgo() {
        return aiAlgo;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setAiAlgo(AIAlgo aiAlgo) {
        this.aiAlgo = aiAlgo;
    }
}

