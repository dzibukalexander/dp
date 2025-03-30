package by.brstu.rec.services;

import by.brstu.rec.Utils.ModelPredictionException;
import by.brstu.rec.Utils.PredictionResult;
import by.brstu.rec.entities.AIAlgo;
import by.brstu.rec.entities.Doctor;
import by.brstu.rec.entities.Position;
import by.brstu.rec.repositories.AIAlgoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class AIAlgoService {
    @Autowired
    private AIAlgoRepository aiAlgoRepository;
    @Autowired
    private PositionService positionService;

    private final RestTemplate restTemplate = new RestTemplate();

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

    public void save(AIAlgo aiAlgo){
        aiAlgoRepository.save(aiAlgo);
    }

    public void deleteById(long id){
        aiAlgoRepository.deleteById(id);
    }

    // Новые методы для работы с ML-моделями
    public PredictionResult predict(Long modelId, MultipartFile file) throws IOException, ModelPredictionException {
        AIAlgo model = findById(modelId);
        return callModel(model, file.getBytes());
    }

    public PredictionResult predict(Long modelId, byte[] fileData) throws ModelPredictionException {
        AIAlgo model = findById(modelId);
        return callModel(model, fileData);
    }

    private PredictionResult callModel(AIAlgo model, byte[] fileData) throws ModelPredictionException {
        String url = "http://localhost:" + model.getEndpoint();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new ByteArrayResource(fileData) {
            @Override
            public String getFilename() {
                return "input.data";
            }
        });

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<PredictionResult> response = restTemplate.postForEntity(
                    url,
                    requestEntity,
                    PredictionResult.class);

            return response.getBody();
        } catch (Exception e) {
            throw new ModelPredictionException("Error calling model " + model.getName());
        }
    }
}
