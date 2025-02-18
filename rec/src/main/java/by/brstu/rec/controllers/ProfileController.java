package by.brstu.rec.controllers;

import by.brstu.rec.entities.Page;
import by.brstu.rec.entities.Position;
import by.brstu.rec.entities.User;
import by.brstu.rec.enums.Role;
import by.brstu.rec.services.DoctorService;
import by.brstu.rec.services.PageService;
import by.brstu.rec.services.PositionService;
import by.brstu.rec.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ProfileController {

    @Autowired
    private UserService userService;
    @Autowired
    private PageService pageService;
    @Autowired
    private PositionService positionService;
    @Autowired
    private DoctorService doctorService;

    @GetMapping("/profile")
    public String profilePage(@RequestParam Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);

        // Преобразуем роли в строки
        List<String> roles = user.getRoles().stream()
                .map(Enum::name)
                .collect(Collectors.toList());
        model.addAttribute("roles", roles);

        if (user.getRoles().contains(Role.DOCTOR)) {
            model.addAttribute("position", user.getDoctor().getPosition().getName());
        }

        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(
            @RequestParam Long id, // Добавленный параметр
            @RequestParam String firstName,
            @RequestParam String name,
            @RequestParam String secondName,
            @RequestParam(required = false) String positionName,
            @RequestParam(required = false) MultipartFile avatar
    ) throws IOException {
        User user = userService.findById(id); // Используем email из запроса

        // Обновление данных пользователя
        user.setFirstName(firstName);
        user.setName(name);
        user.setSecondName(secondName);

        // Обновление позиции для доктора
        if (user.getRoles().contains(Role.DOCTOR) && positionName != null) {
            Position position = positionService.findByName(positionName);
            if (position == null) {
                position = new Position();
                position.setName(positionName);
                positionService.save(position);
            }
            user.getDoctor().setPosition(position);
            doctorService.save(user.getDoctor());
        }

        // Загрузка аватара
        if (avatar != null && !avatar.isEmpty()) {
            Page avatarPage = pageService.uploadPage(avatar);
            user.setAvatar(avatarPage);
        }

        userService.save(user);
        return "redirect:/profile?id=" + user.getId();
    }
}