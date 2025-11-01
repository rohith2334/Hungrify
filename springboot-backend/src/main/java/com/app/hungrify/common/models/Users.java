package com.app.hungrify.common.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.Date;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Users {

    public Users(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @NotBlank
    @Size(max = 20)
    @Column(nullable = false, length = 100, unique = true)
    private String username;
    @NotBlank
    @Size(max = 120)
    @Column(nullable = false, length = 255)
    private String password;
    @NotBlank
    @Size(max = 50)
    @Email
    @Column(length = 255, unique = true)
    private String email;
    private ERole roles;
    @Column(length = 255)
    private String profileImage;
    @Column(length = 100)
    private String firstName;
    @Column(length = 100)
    private String lastName;
    // full_name is generated in DB; we still map it for read-only convenience
    @Column(length = 200, insertable = false, updatable = false)
    private String fullName;
    @Column(nullable = false)
    private Boolean verified = false;
    @Column(nullable = false)
    private Boolean active = true;
    @Column(length = 32, unique = true)
    private String phone;
    @Lob
    private String address;
    private Instant lastLogin;
    @Column(columnDefinition = "json")
    private String profileJson;
    @CreationTimestamp
    private Instant createdAt;
    @UpdateTimestamp
    private Instant updatedAt;
}
