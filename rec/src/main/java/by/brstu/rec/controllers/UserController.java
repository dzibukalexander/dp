package by.brstu.rec.controllers;

import by.brstu.rec.entities.Doctor;
import by.brstu.rec.entities.Position;
import by.brstu.rec.entities.User;
import by.brstu.rec.enums.Role;
import by.brstu.rec.services.DoctorService;
import by.brstu.rec.services.PositionService;
import by.brstu.rec.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/users")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private PositionService positionService;

    @GetMapping
    public String userList(Model model) {
        model.addAttribute("users", userService.findAll());

        return "/admin/userList";
    }

    @GetMapping("{user}")
    public String userEditForm(@PathVariable User user, Model model) {
        List<Role> roles = new ArrayList<>();
        roles.add(Role.ADMIN);
        if(user.getRoles().contains(Role.DOCTOR)) {
            roles.add(Role.DOCTOR);
        } else
            roles.add(Role.PATIENT);

        model.addAttribute("user", user);
        model.addAttribute("roles", roles);

        boolean userIsDoctor = user.getRoles().contains(Role.DOCTOR);
        model.addAttribute("userIsDoctor", userIsDoctor);

        if(userIsDoctor) {
            model.addAttribute("positions", positionService.findAll());
        }

        return "/admin/userEdit";
    }

    @PostMapping
    public String userSave(
            @RequestParam(required = false) List<String> roles, // Принимаем роли как список
            @RequestParam("userId") User user,
            @RequestParam(value = "positionId", required = false) Long positionId
    ) {
        userService.updateUserRoles(user, roles);
        userService.updateDoctorPosition(user, positionId);
        userService.save(user);
        return "redirect:/admin/users";
    }

    @PostMapping("/del/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return "redirect:/admin/users";
    }
}