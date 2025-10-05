package com.onixx.apolloveiculos.api.Domains.User;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import java.time.Instant;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String token;
    @Column(nullable = false)
    private Instant expiryDate;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}