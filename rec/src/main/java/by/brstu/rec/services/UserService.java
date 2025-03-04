package by.brstu.rec.services;

import by.brstu.rec.entities.*;
import by.brstu.rec.enums.Role;
import by.brstu.rec.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PositionRepository positionRepository;

    public void registerUser(String firstName, String name, String secondName,
                             String email, String password, Set<Role> roles,
                             Position position) {
        User user = new User();
        user.setFirstName(firstName);
        user.setName(name);
        user.setSecondName(secondName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(roles);
        user.setActive(true);

        userRepository.save(user);

        if (roles.contains(Role.DOCTOR)) {
            Doctor doctor = new Doctor();
            doctor.setUser(user);

            // Создание или поиск существующей позиции
//            Position position = positionService.findByName(positionName);
//            if(position == null) {
//                Position newPosition = new Position();
//                newPosition.setName(positionName);
//                position = positionService.save(newPosition);
//            }
//            doctor.setPosition(position);

            doctor.setPosition(position);
            doctorRepository.save(doctor);
            user.setDoctor(doctor); // Связываем User с Doctor
        } else if (roles.contains(Role.PATIENT)) {
            Patient patient = new Patient();
            patient.setUser(user);
            patientRepository.save(patient);
            user.setPatient(patient); // Связываем User с Patient
        }

        userRepository.save(user); // Обновляем User с связями
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public void updateDoctorPosition(User user, Long positionId) {
        if (user.getDoctor() != null) {
            Doctor doctor = user.getDoctor();
            if (positionId != null) {
                Position position = positionRepository.findById(positionId)
                        .orElseThrow(() -> new RuntimeException("Position not found"));
                doctor.setPosition(position);
            } else {
                doctor.setPosition(null);
            }
            doctor.setUser(user);
            doctorRepository.save(doctor);
            user.setDoctor(doctor);
        }
    }

    public void updateUserRoles(User user, List<String> roles) {
        Set<Role> currentRoles = new HashSet<>(user.getRoles());
        currentRoles.removeIf(role -> role == Role.ADMIN);

        if (roles != null) {
            for (String role : roles) {
                currentRoles.add(Role.valueOf(role));
            }
        }
        // Обновляем роли пользователя
        user.getRoles().clear();
        user.getRoles().addAll(currentRoles);
    }
}