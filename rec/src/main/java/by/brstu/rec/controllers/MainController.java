package by.brstu.rec.controllers;

import by.brstu.rec.entities.AIAlgo;
import by.brstu.rec.entities.Doctor;
import by.brstu.rec.entities.DoctorPatientPage;
import by.brstu.rec.entities.User;
import by.brstu.rec.enums.PhotoStatus;
import by.brstu.rec.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    private UserService userService;
    @Autowired
    private DoctorService doctorService;
    @Autowired
    private DoctorPatientPageService doctorPatientPageService;
    @Autowired
    private AIAlgoService aiAlgoService;

    @GetMapping("/")
    public String homePage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null) return "redirect:/login";

        User user = userService.findByEmail(userDetails.getUsername());
        model.addAttribute("id", user.getId());
        boolean isDoctor = user.getRoles().stream().anyMatch(role -> role.name().equals("DOCTOR"));
        model.addAttribute("isDoctor", isDoctor);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        List<DoctorPatientPage> openRequests;
        List<DoctorPatientPage> closedRequests;

        if (isDoctor) {
            Doctor doctor = user.getDoctor();
            model.addAttribute("model", aiAlgoService.findByPosition(doctor.getPosition()));
            openRequests = doctorPatientPageService.getOpenRequestsForDoctor(doctor.getId());
            closedRequests = doctorPatientPageService.getClosedRequestsForDoctor(doctor.getId());
        } else {
            model.addAttribute("doctors", doctorService.findAll());
            openRequests = doctorPatientPageService.getOpenRequestsForPatient(user.getPatient().getId());
            closedRequests = doctorPatientPageService.getClosedRequestsForPatient(user.getPatient().getId());
        }

        formatRequestDates(openRequests, formatter);
        formatRequestDates(closedRequests, formatter);

        model.addAttribute("openRequests", openRequests);
        model.addAttribute("closedRequests", closedRequests);

        return "index";
    }

    private void formatRequestDates(List<DoctorPatientPage> requests, DateTimeFormatter formatter) {
        requests.forEach(r -> r.setFormattedDateCreated(r.getDateCreated().format(formatter)));
        requests.forEach(r -> r.setFormattedDateClosed(r.getDateClosed().format(formatter)));
    }
}