package by.brstu.rec.services;

import by.brstu.rec.entities.AIAlgo;
import by.brstu.rec.entities.Position;
import by.brstu.rec.repositories.AIAlgoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AIAlgoService {
    @Autowired
    private AIAlgoRepository aiAlgoRepository;

    @Autowired
    private PositionService positionService;

    public AIAlgo findById(long id){
        return aiAlgoRepository.findById(id);
    }

    public List<AIAlgo> findAll(){
        return aiAlgoRepository.findAll();
    }

    public void linkPosition(Long aiAlgoId, Long positionId) {
        AIAlgo aiAlgo = aiAlgoRepository.findById(aiAlgoId).orElseThrow();
        Position position = positionService.findById(positionId);
        aiAlgo.setPosition(position);
        aiAlgoRepository.save(aiAlgo);
    }
}
