package com.app.hungrify.main.models;


import com.app.hungrify.common.models.Users;
import com.app.hungrify.main.util.JsonMapConverter;
import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.Map;

@Entity
@Table(name = "admin",
        uniqueConstraints = {@UniqueConstraint(name = "uk_admin_username", columnNames = {"username"})})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Admin {

    @Id
    @Column(name = "admin_id")
    private Long adminId; // 1:1 mapping to users.user_id

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "admin_id", foreignKey = @ForeignKey(name = "fk_admin_user"))
    private Users user;

    @Column(nullable = false, length = 100, unique = true)
    private String username;

    @Column(length = 200)
    private String fullName;

    @Column(length = 255)
    private String email;

    @Column(length = 32)
    private String phone;

    @Column(columnDefinition = "json")
    @Convert(converter = JsonMapConverter.class)
    private Map<String, Object> profileJson;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}
