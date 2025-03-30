package by.brstu.rec.controllers;

import by.brstu.rec.entities.AIAlgo;
import by.brstu.rec.enums.Status;
import by.brstu.rec.services.AIAlgoService;
import by.brstu.rec.services.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/new")
    public String newAIAlgo(Model model) {
        //model.addAttribute("aiAlgo", new AIAlgo());
        model.addAttribute("positions", positionService.findAll());
        return "admin/add_model";
    }

    @PostMapping("/new")
    public String addAIAlgo(@RequestParam String name,
                            @RequestParam(required = false) String description,
                            @RequestParam(required = false) String kaggleURL,
                            @RequestParam String status,
                            @RequestParam(required = false) Boolean is_default,
                            @RequestParam(required = false) Long positionId) {
        AIAlgo aiAlgo = new AIAlgo();
        aiAlgo.setName(name);
        aiAlgo.setDescription(description);
        aiAlgo.setKaggleURL(kaggleURL);
        aiAlgo.setStatus(Status.valueOf(status));
        aiAlgo.setIs_default(is_default != null && is_default);

        aiAlgoService.save(aiAlgo);
        if (positionId != null) {
            aiAlgoService.linkPosition(aiAlgo.getId(), positionId);
        }

        return "redirect:/admin/ai-algos";
    }


    @PostMapping("/del/{id}")
    public String deleteAIalgo(@PathVariable Long id) {
        aiAlgoService.deleteById(id);
        return "redirect:/admin/ai-algos";
    }
}