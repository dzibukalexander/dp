package by.brstu.rec.repositories;

import by.brstu.rec.entities.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {
    Position findByName(String positionName);
    Optional<Position> findById(Long id);
}