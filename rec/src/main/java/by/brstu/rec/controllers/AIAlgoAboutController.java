package by.brstu.rec.controllers;

import by.brstu.rec.services.AIAlgoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/about")
public class AIAlgoAboutController {
    @Autowired
    private AIAlgoService aiAlgoService;

    @GetMapping("/model/{modelId}")
    public String modelPage(@PathVariable Long modelId, Model model) {
        model.addAttribute("model", aiAlgoService.findById(modelId));
        return "about_model";
    }

    //    @GetMapping("/model")
//    public String modelPage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
//        User user = userService.findByEmail(userDetails.getUsername());
//        boolean isDoctor = user.getRoles().stream().anyMatch(role -> role.name().equals("DOCTOR"));
//
//        if (isDoctor) {
//            Doctor doctor = user.getDoctor();
//            AIAlgo model = aiAlgoService.findByPosition(doctor.getPosition());
//            model.addAttribute("model", model);
//        }
//
//        return "model";
//    }
}
