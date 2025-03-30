package by.brstu.rec.controllers;

import by.brstu.rec.entities.Position;
import by.brstu.rec.services.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/positions")
//@PreAuthorize("hasAuthority('ADMIN')")
public class PositionController {

    @Autowired
    private PositionService positionService;

    @GetMapping
    public String positions(Model model) {
        model.addAttribute("positions", positionService.findAll());
        return "admin/positions";
    }

    @PostMapping
    public String createPosition(@RequestParam String name) {
        Position position = new Position();
        position.setName(name);
        positionService.save(position);
        return "redirect:/admin/positions";
    }

    @PostMapping("/del/{id}")
    public String deletePosition(@PathVariable Long id) {
        positionService.delete(id);
        return "redirect:/admin/positions";
    }
}