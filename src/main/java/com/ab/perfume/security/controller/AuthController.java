package com.ab.perfume.security.controller;

import com.ab.perfume.security.dto.AdminUserRequestDTO;
import com.ab.perfume.security.dto.JwtResponseDTO;
import com.ab.perfume.security.dto.LoginRequestDTO;
import com.ab.perfume.security.entity.AdminUser;
import com.ab.perfume.security.enums.RoleName;
import com.ab.perfume.security.jwt.JwtProvider;
import com.ab.perfume.security.repository.AdminUserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtProvider jwtProvider;

    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO request
    ) {

        Authentication authentication = authenticationManager.authenticate(

                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        String token = jwtProvider.generateToken(authentication);

        return ResponseEntity.ok(
                new JwtResponseDTO(token)
        );
    }


    @PostMapping("/register-admin")
    public ResponseEntity<?> registerAdmin(
            @Valid @RequestBody AdminUserRequestDTO request
    ) {
        if (adminUserRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body("El usuario ya existe");
        }

        if (adminUserRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("El correo ya existe");
        }

        AdminUser admin = AdminUser.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .enabled(true)
                .role(RoleName.ROLE_ADMIN)
                .build();

        adminUserRepository.save(admin);

        return ResponseEntity.status(HttpStatus.CREATED).body("Admin creado correctamente");
    }

}
