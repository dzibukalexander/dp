package by.brstu.rec.controllers;

import by.brstu.rec.entities.Doctor;
import by.brstu.rec.entities.DoctorPatientPage;
import by.brstu.rec.entities.User;
import by.brstu.rec.services.DoctorService;
import by.brstu.rec.services.UserService;
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

    @GetMapping("/")
    public String homePage(
            @AuthenticationPrincipal UserDetails userDetails,
            Model model) {
        // no move from unauth: sc
        if (userDetails == null) {
            return "redirect:/login";
        }

        User user = userService.findByEmail(userDetails.getUsername());
        model.addAttribute("id", user.getId());

        // Определяем роль пользователя
        boolean isDoctor = user.getRoles().stream()
                .anyMatch(role -> role.name().equals("DOCTOR"));
        model.addAttribute("isDoctor", isDoctor);

        if (isDoctor) {
            List<DoctorPatientPage> patientPages = doctorService.getPatientPages(user.getDoctor());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
            for (DoctorPatientPage patientPage : patientPages) {
                String formattedDate = patientPage.getDateCreated().format(formatter);
                patientPage.setFormattedDateCreated(formattedDate);
            }
            model.addAttribute("patientPages", patientPages);
        } else {
            List<Doctor> doctors = doctorService.findAll();
            model.addAttribute("doctors", doctors);
        }

        return "index";
    }
}