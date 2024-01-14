package org.florense.outbound.adapter.postgre.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Entity(name = "User")
@Table(name = "user_manager")
public class UserEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "loja_name")
    private String name;

    @Column(name = "user_id_ml")
    private String userIdML;

    @Column(name = "access_code")
    private String accessCode;
    @Column(name = "refresh_token")
    private String refreshToken;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
