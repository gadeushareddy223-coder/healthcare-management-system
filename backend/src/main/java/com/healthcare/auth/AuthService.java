package com.healthcare.auth;

import com.healthcare.user.User;
import com.healthcare.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse login(AuthRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() ->
                        new RuntimeException("Invalid username or password"));

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPasswordHash())) {

            throw new RuntimeException("Invalid username or password");
        }

        String token = jwtService.generateToken(
                user.getUsername(),
                user.getRole());

        return new AuthResponse(
                token,
                user.getUsername(),
                user.getRole());
    }

    public AuthResponse register(AuthRegisterRequest request) {

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();

        user.setUsername(request.getUsername());
        user.setPasswordHash(
                passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setEnabled(true);

        User savedUser = userRepository.save(user);

        String token = jwtService.generateToken(
                savedUser.getUsername(),
                savedUser.getRole());

        return new AuthResponse(
                token,
                savedUser.getUsername(),
                savedUser.getRole());
    }
}