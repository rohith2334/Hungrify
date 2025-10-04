package com.app.hungrify.common.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(max = 20)
    private String username;
    @NotBlank
    @Size(max = 120)
    private String password;
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    private ERole roles;
    public Users(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
    private String profileImage;

    private String firstName;
    private String lastName;
    private boolean verified;
    private boolean active;
    private Date createdAt;
    private Date updatedAt;


}
