package by.brstu.rec.controllers;

import by.brstu.rec.services.AIAlgoService;
import by.brstu.rec.services.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/ai-algos")
public class AIAlgoController {

    @Autowired
    private AIAlgoService aiAlgoService;

    @Autowired
    private PositionService positionService;

    @GetMapping
    public String listAIAlgos(Model model) {
        model.addAttribute("aiAlgos", aiAlgoService.findAll());
        model.addAttribute("positions", positionService.findAll());
        return "admin/ai_algos";
    }

    @PostMapping("/{aiAlgoId}/link-position/{positionId}")
    public String linkPosition(@PathVariable Long aiAlgoId, @PathVariable Long positionId) {
        aiAlgoService.linkPosition(aiAlgoId, positionId);
        return "redirect:/admin/ai-algos";
    }

    @PostMapping("/new")
    public String newAIAlgo() {
        return "admin/ai_algos";
    }
}