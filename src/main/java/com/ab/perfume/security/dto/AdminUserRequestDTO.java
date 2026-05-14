package com.ab.perfume.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminUserRequestDTO {

        @NotBlank(message = "El usuario es obligatorio")
        private String username;

        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "El correo no tiene un formato válido")
        private String email;

        @NotBlank(message = "La contraseña es obligatoria")
        private String password;

        @NotBlank(message = "El nombre es obligatorio")
        private String name;

}
