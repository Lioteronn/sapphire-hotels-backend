package com.lioteron.sapphirehotels.controllers.auth;

import com.lioteron.sapphirehotels.model.User;
import com.lioteron.sapphirehotels.model.auth.LoginRequest;
import com.lioteron.sapphirehotels.model.auth.RegisterRequest;
import com.lioteron.sapphirehotels.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;

    AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<String> registerUser(RegisterRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setPassword(request.getPassword());
        user.setPhone(request.getPhone());
        userRepository.save(user);

        if (userRepository.findById(user.getId()).isPresent()) {
            return ResponseEntity.ok("User registered successfully.");
        } else {
            return ResponseEntity.badRequest().body("Something went wrong, please check your information.");
        }
    }

    public ResponseEntity<?> login(LoginRequest request, HttpSession session) {
        String email = request.getEmail();
        String password = request.getPassword();

        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(java.util.Map.of("message", "Usuario no encontrado"));
        }

        if (!user.get().getPassword().equals(password)) {
            return ResponseEntity
                    .badRequest()
                    .body(java.util.Map.of("message", "Contraseña incorrecta"));
        }

        session.setAttribute("User", user.get());

        return ResponseEntity.ok(user.get());
    }

    public ResponseEntity<String> logout(HttpSession session) {
        if (session.getAttribute("User") != null) {
            session.invalidate();
        }

        return ResponseEntity.ok().build();
    }

}
