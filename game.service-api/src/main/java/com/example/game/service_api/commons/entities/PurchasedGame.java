package com.example.game.service_api.commons.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "purchased_games")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchasedGame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long gameId;

    @Column(nullable = false)
    private String gameName;

    @Column(nullable = false)
    private Double gamePrice;

    @Column(nullable = false)
    private String gamePlatforms;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String userEmail;
}
