package by.brstu.rec.repositories;

import by.brstu.rec.entities.AIAlgo;
import by.brstu.rec.entities.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {
    Position findByName(String positionName);
    Optional<Position> findById(Long id);

    List<Position> findPositionsByNameNot(String positionName);

    @Query("SELECT p FROM Position p WHERE p.id NOT IN (SELECT a.position.id FROM AIAlgo a WHERE a.position IS NOT NULL)")
    List<Position> findPositionsWithoutAlgo();
}