package by.brstu.rec.repositories;

import by.brstu.rec.entities.AIAlgo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AIAlgoRepository extends JpaRepository<AIAlgo, Long> {
    public AIAlgo findById(long id);
}

