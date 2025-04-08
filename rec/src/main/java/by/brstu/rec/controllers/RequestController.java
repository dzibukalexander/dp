package by.brstu.rec.controllers;

import by.brstu.rec.entities.AIAlgo;
import by.brstu.rec.entities.DoctorPatientPage;
import by.brstu.rec.entities.Page;
import by.brstu.rec.entities.User;
import by.brstu.rec.services.AIAlgoService;
import by.brstu.rec.services.DoctorPatientPageService;
import by.brstu.rec.services.PageService;
import by.brstu.rec.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/request")
public class RequestController {

    @Autowired
    private DoctorPatientPageService doctorPatientPageService;
    @Autowired
    private AIAlgoService aiAlgoService;
    @Autowired
    private UserService userService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private PageService pageService;

    @GetMapping("/{requestKey}")
    public String getRequestDetails(
            @PathVariable Long requestKey,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model) {

        DoctorPatientPage request = doctorPatientPageService.findById(requestKey);

        User user = userService.findByEmail(userDetails.getUsername());
        boolean isDoctor = user.getRoles().stream()
                .anyMatch(role -> role.name().equals("DOCTOR"));

        // Получаем модель на основе специализации доктора
        AIAlgo algo = null;
        if (isDoctor && user.getDoctor() != null && user.getDoctor().getPosition() != null) {
            algo = aiAlgoService.findByPosition(user.getDoctor().getPosition());
        }

        model.addAttribute("request", request);
        model.addAttribute("model", algo);
        model.addAttribute("isDoctor", isDoctor);
        model.addAttribute("requestKey", requestKey); // Сохраняем ключ для формы

        return "request-details";
    }

    @PostMapping("/{requestKey}/respond")
    public String respondToRequest(
            @PathVariable Long requestKey,
            @RequestParam(required = false) String conclusion,
            @RequestParam(required = false) MultipartFile responsePhoto,
            @AuthenticationPrincipal UserDetails userDetails) throws IOException {

        Page page = null;
        if(responsePhoto != null) {
            page = pageService.uploadPage(responsePhoto);
        }
        doctorPatientPageService.sendResponseToPatient(requestKey, conclusion, page);

        return "redirect:/request/" + requestKey;
    }

    @PutMapping("/{requestId}/status")
    public ResponseEntity<Void> updateRequestStatus(
            @PathVariable Long requestId,
            @RequestBody Map<String, Boolean> status) {

        DoctorPatientPage request = doctorPatientPageService.findById(requestId);
        request.setInProgress(status.get("inProgress"));
        doctorPatientPageService.save(request);

        // Отправляем обновление через WebSocket
        messagingTemplate.convertAndSend(
                "/topic/request/" + requestId + "/status",
                Map.of("inProgress", request.isInProgress())
        );

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<Map<String, Boolean>> getRequestStatus(@PathVariable Long id) {
        DoctorPatientPage request = doctorPatientPageService.findById(id);
        return ResponseEntity.ok(Map.of("inProgress", request.isInProgress()));
    }
}