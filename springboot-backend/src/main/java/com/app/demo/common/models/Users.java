package com.app.demo.common.models;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "users")
public class Users {
    @Id
    private String id;
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

    private Set<ERole> roles;
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
