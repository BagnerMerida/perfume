package com.ab.perfume.security.entity;

import com.ab.perfume.security.enums.RoleName;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "admin_users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true, length=50)
    private String username;

    @Column(nullable=false)
    private String password;

    @Column(nullable=false, length=100)
    private String name;

    @Column(nullable = false, unique = true, length = 120)
    private String email;

    @Column(nullable=false)
    private Boolean enabled;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=30)
    private RoleName role;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (enabled == null) enabled = true;
        if (role == null) role = RoleName.ROLE_ADMIN;
        createdAt = LocalDateTime.now();
    }

}
