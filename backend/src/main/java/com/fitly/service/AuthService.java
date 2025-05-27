package com.fitly.service;

import com.fitly.dto.AuthResponse;
import com.fitly.dto.LoginRequest;
import com.fitly.dto.RegisterRequest;
import com.fitly.exception.BadRequestException;
import com.fitly.model.Role;
import com.fitly.model.User;
import com.fitly.repository.UserRepository;
import com.fitly.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public AuthResponse register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new BadRequestException("E-mail je již zaregistrován.");
        }

        User user = new User();
        user.setEmail(registerRequest.getEmail());

        String username = registerRequest.getEmail().split("@")[0];
        user.setUsername(username);

        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(Role.USER);

        userRepository.save(user);

        String token = jwtProvider.generateTokenFromEmail(user.getEmail());

        return new AuthResponse(token, "Uživatel usěšně zaregistrován");
    }


    public AuthResponse login(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtProvider.generateToken(authentication);

            return new AuthResponse(token, "Přihlášení proběhlo úspěšně");
        } catch (Exception e) {
            System.err.println("Login failed for email: " + loginRequest.getEmail() + " Reason: " + e.getMessage());
            throw new BadRequestException("Neplatný email nebo heslo");
        }
    }
}

