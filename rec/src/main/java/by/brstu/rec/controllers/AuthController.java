package by.brstu.rec.controllers;

import by.brstu.rec.entities.Position;
import by.brstu.rec.entities.User;
import by.brstu.rec.enums.Role;
import by.brstu.rec.services.CustomUserDetailsService;
import by.brstu.rec.services.PositionService;
import by.brstu.rec.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private PositionService positionService;

    @GetMapping("/register")
    public String registerPage(Model model) {

        List<String> roles = Arrays.stream(Role.values())
                .filter(role -> role != Role.ADMIN)
                .map(Role::getDisplayName) // Требуется метод getDisplayName() в enum
                .collect(Collectors.toList());

        List<Position> positions = positionService.findAll();

        model.addAttribute("roles", roles);
        model.addAttribute("positions", positions);
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @RequestParam String firstName,
            @RequestParam String name,
            @RequestParam String secondName,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam Role role,
            @RequestParam(required = false) Long positionId // Позиция только для доктора
            ) {

        Set<Role> roles = Collections.singleton(role);

        Position position = null;
        if (role == Role.DOCTOR && positionId != null) {
            position = positionService.findById(positionId);
        }

        userService.registerUser(firstName, name, secondName, email, password, roles, position);

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(@AuthenticationPrincipal UserDetailsService userDetails) {
        if (userDetails == null) {
            return "login";
        } else {
            return "redirect:/";
        }
    }
}