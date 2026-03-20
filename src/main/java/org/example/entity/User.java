package org.example.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "APP_USERS")
public class User extends PanacheEntityBase {
    @Id
    @SequenceGenerator(name = "appUserSeq", sequenceName = "APP_USERS_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "appUserSeq")
    @Column(name = "USER_ID")
    public Long user_id;

    @Column(name = "FULL_NAME", nullable = false, length = 120)
    public String full_name;

    @Column(name = "USERNAME", unique = true, nullable = false, length = 120)
    public String username;

    @Column(name = "PASSWORD_HASH", nullable = false, length = 255)
    public String password_hash;

    @Column(name = "CREATED_AT", nullable = false)
    public LocalDateTime created_at = LocalDateTime.now();
}