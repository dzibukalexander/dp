package by.brstu.rec.controllers;

import by.brstu.rec.Utils.AIAlgoRegistrationDto;
import by.brstu.rec.Utils.ModelPredictionException;
import by.brstu.rec.Utils.PredictionResult;
import by.brstu.rec.entities.AIAlgo;
import by.brstu.rec.enums.Status;
import by.brstu.rec.services.AIAlgoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/ml")
public class AIAlgoUpdater {
    @Autowired
    private AIAlgoService aiAlgoService;

    @PostMapping("/predict/{modelId}")
    public ResponseEntity<PredictionResult> predict(
            @PathVariable Long modelId,
            @RequestParam("file") MultipartFile file) throws ModelPredictionException, IOException {

        return ResponseEntity.ok(aiAlgoService.predict(modelId, file));
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerModel(@RequestBody AIAlgoRegistrationDto ail) {
        AIAlgo model = new AIAlgo();
        model.setName(ail.getName());
        model.setDescription(ail.getDescription());
        model.setInputType(ail.getInputType());
        model.setOutputType(ail.getOutputType());
        model.setEndpoint(ail.getEndpoint());
        model.setKaggleURL(ail.getKaggleURL());
        model.setStatus(Status.ACTIVE);

        // updating logic

        aiAlgoService.save(model);
        return ResponseEntity.ok("Model is satisfied the conditions");
    }
}
