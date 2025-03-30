package by.brstu.rec.controllers;

import by.brstu.rec.Utils.DoctorPatientPageDTO;
import by.brstu.rec.Utils.DoctorPatientPageId;
import by.brstu.rec.entities.DoctorPatientPage;
import by.brstu.rec.entities.User;
import by.brstu.rec.services.DoctorPatientPageService;
import by.brstu.rec.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/request")
public class RequestController {

    @Autowired
    private DoctorPatientPageService doctorPatientPageService;
    @Autowired
    private UserService userService;

    @GetMapping("/{requestKey}")
    public String getRequestDetails(
            @PathVariable Long requestKey,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model) {

        DoctorPatientPage request = doctorPatientPageService.findById(requestKey);

        User user = userService.findByEmail(userDetails.getUsername());
        boolean isDoctor = user.getRoles().stream()
                .anyMatch(role -> role.name().equals("DOCTOR"));

        model.addAttribute("request", request);
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

       // DoctorPatientPageDTO dto = new DoctorPatientPageDTO(requestKey);
        doctorPatientPageService.sendResponseToPatient(requestKey, conclusion, responsePhoto);

        return "redirect:/request/" + requestKey;
    }
}