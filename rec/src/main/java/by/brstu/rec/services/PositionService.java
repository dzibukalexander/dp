package by.brstu.rec.services;

import by.brstu.rec.entities.AIAlgo;
import by.brstu.rec.entities.Position;
import by.brstu.rec.repositories.AIAlgoRepository;
import by.brstu.rec.repositories.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PositionService {

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private AIAlgoRepository aiAlgoRepository;

    public Position findByName(String positionName) {
        return positionRepository.findByName(positionName);
    }

    public Position findById(Long id) {
        return positionRepository.findById(id).orElse(null);
    }

    public Position save(Position position) {
        return positionRepository.save(position);
    }

    public List<Position> findAll() {
        List<Position> positions = positionRepository.findAll();

        // Заполняем поле aiAlgo для каждой позиции
        positions.forEach(position -> {
            AIAlgo aiAlgo = aiAlgoRepository.findByPositionId(position.getId());
            position.setAiAlgo(aiAlgo);
        });

        return positions;
    }

    public void delete(Long id) {
        positionRepository.deleteById(id);
    }
}
