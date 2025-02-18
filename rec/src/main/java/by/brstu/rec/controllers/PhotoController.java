package by.brstu.rec.controllers;

import by.brstu.rec.entities.Patient;
import by.brstu.rec.services.DoctorPatientPageService;
import by.brstu.rec.services.PatientService;
import by.brstu.rec.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class PhotoController {

    @Autowired
    private DoctorPatientPageService doctorPatientPageService;
    @Autowired
    private UserService userService;

    @PostMapping("/send-photo")
    public ResponseEntity<String> sendPhoto(
            @RequestParam("file") MultipartFile file,
            @RequestParam("doctorId") Long doctorId,
            @AuthenticationPrincipal UserDetails userDetails) throws IOException {

        // Получаем текущего пользователя (пациента)
        Patient patient = userService.findByEmail(userDetails.getUsername()).getPatient();

        // Сохраняем фото и создаем запись в DoctorPatientPage
        doctorPatientPageService.savePhoto(doctorId, patient.getId(), file);

        return ResponseEntity.ok("Фото отправлено!");
    }
}
