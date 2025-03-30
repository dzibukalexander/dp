package by.brstu.rec.controllers;

import by.brstu.rec.Utils.DoctorPatientPageDTO;
import by.brstu.rec.entities.Page;
import by.brstu.rec.entities.Patient;
import by.brstu.rec.services.DoctorPatientPageService;
import by.brstu.rec.services.PageService;
import by.brstu.rec.services.PatientService;
import by.brstu.rec.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    @Autowired
    private PageService pageService;

//    @PostMapping("/send-photo")
//    public ResponseEntity<String> sendPhoto(
//            @RequestParam("file") MultipartFile file,
//            @RequestParam("doctorId") Long doctorId,
//            @AuthenticationPrincipal UserDetails userDetails) throws IOException {
//
//        // Получаем текущего пользователя (пациента)
//        Patient patient = userService.findByEmail(userDetails.getUsername()).getPatient();
//
//        // Сохраняем фото и создаем запись в DoctorPatientPage
//        doctorPatientPageService.savePhoto(doctorId, patient.getId(), file);
//
//        return ResponseEntity.ok("Фото отправлено!");
//    }
    @GetMapping("/photo/{id}")
    public ResponseEntity<byte[]> getPhoto(@PathVariable Long id) {
        Page page = pageService.findById(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(page.getContentType()))
                .body(page.getBytes());
    }

    @PostMapping("/send-photo")
    public String sendPhoto(
            @RequestParam("file") MultipartFile file,
            @RequestParam("doctorId") Long doctorId,
            @AuthenticationPrincipal UserDetails userDetails) throws IOException {

        // Получаем текущего пользователя (пациента)
        Patient patient = userService.findByEmail(userDetails.getUsername()).getPatient();
        // Сохраняем фото и создаем запись в DoctorPatientPage
        doctorPatientPageService.sendFileToDoctor(doctorId, patient.getId(), file);

        return "redirect:/";
    }

    @PostMapping("/response")
    public String respondToPatient(@RequestParam Long id,
                                   @RequestParam(required = false) String conclusion,
                                   @RequestParam(required = false) MultipartFile responseFile,
                                   @AuthenticationPrincipal UserDetails userDetails) throws IOException {
      //  DoctorPatientPageDTO dto = new DoctorPatientPageDTO(ptdId);
        doctorPatientPageService.sendResponseToPatient(id, conclusion, responseFile);
        return "redirect:/";
    }
}
