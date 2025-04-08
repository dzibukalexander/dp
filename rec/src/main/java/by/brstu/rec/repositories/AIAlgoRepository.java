package by.brstu.rec.repositories;

import by.brstu.rec.entities.AIAlgo;
import by.brstu.rec.entities.Position;
import by.brstu.rec.enums.ModelIO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AIAlgoRepository extends JpaRepository<AIAlgo, Long> {
    AIAlgo findById(long id);
    AIAlgo findByPositionId(Long positionId);

    AIAlgo findByPosition(Position position);
    Optional<AIAlgo> findByName(String name);
    List<AIAlgo> findByInputType(ModelIO inputType);
}

