package by.brstu.rec.controllers;

import by.brstu.rec.Utils.AIAlgoRegistrationDto;
import by.brstu.rec.Utils.FileUtils;
import by.brstu.rec.Utils.ModelPredictionException;
import by.brstu.rec.Utils.PredictionResult;
import by.brstu.rec.entities.AIAlgo;
import by.brstu.rec.entities.DoctorPatientPage;
import by.brstu.rec.entities.Page;
import by.brstu.rec.enums.ModelIO;
import by.brstu.rec.enums.Status;
import by.brstu.rec.services.AIAlgoService;
import by.brstu.rec.services.DoctorPatientPageService;
import by.brstu.rec.services.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@RestController
@RequestMapping("/api/ml")
public class AIAlgoUpdater {
    @Autowired
    private AIAlgoService aiAlgoService;
    @Autowired
    private PageService pageService;
    @Autowired
    private DoctorPatientPageService doctorPatientPageService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @PostMapping("/predict/{modelId}")
    public ResponseEntity<PredictionResult> predict(
            @PathVariable Long modelId,
            @RequestParam("request") Long requestId) throws ModelPredictionException, IOException {

        // Получаем запрос по ID файла
        DoctorPatientPage request = doctorPatientPageService.findById(requestId);

        try {
            // Устанавливаем статус и уведомляем клиентов
            setRequestInProgress(request, true);

            //byte[] bytes = pageService.getPhotoBytes(request);
            PredictionResult result = aiAlgoService.predict(modelId, request.getRequestPage().getBytes());

            if(result != null) {
                if(result.getResult() != null) {
                    request.setConclusion(result.getResult());
                }

                if(result.getPhotoData() != null) {
                    String filename = "responseToReq_" + requestId + ".jpeg";
                    MultipartFile multipartFile = FileUtils.createMultipartFile(
                            FileUtils.byteArrToPrimitive(result.getPhotoData()),
                            filename,
                            "image/jpeg"
                    );

                    Page responsePage = pageService.uploadPage(multipartFile);
                    request.setResponsePage(responsePage);
                }

                doctorPatientPageService.save(request);
            }

            // Отправляем обновление через WebSocket
            messagingTemplate.convertAndSend(
                    "/topic/request/" + requestId + "/response",
                    result
            );

            return ResponseEntity.ok(result);
        } finally {
            // Сбрасываем статус и уведомляем клиентов
            setRequestInProgress(request, false);
        }
    }


    @PostMapping("/register")
    public ResponseEntity<String> registerModel(@RequestBody AIAlgoRegistrationDto ail) {
        AIAlgo model = new AIAlgo();
        model.setName(ail.getName());
        model.setDescription(ail.getDescription());
        model.setInputType(ModelIO.valueOf(ail.getInputType()));
        model.setOutputType(ModelIO.valueOf(ail.getOutputType()));
        model.setEndpoint(ail.getEndpoint());
        model.setKaggleURL(ail.getKaggleURL());
        model.setStatus(Status.ACTIVE);

        // updating logic

        aiAlgoService.save(model);
        return ResponseEntity.ok("Model is satisfied the conditions");
    }

    private void setRequestInProgress(DoctorPatientPage request, boolean inProgress) {
        request.setInProgress(inProgress);
        doctorPatientPageService.save(request);

        // Отправляем обновление через WebSocket
        messagingTemplate.convertAndSend(
                "/topic/request/" + request.getId() + "/status",
                Map.of("inProgress", inProgress)
        );
    }
}
