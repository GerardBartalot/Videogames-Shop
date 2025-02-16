package com.example.game.service_api.commons.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "games_list")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, unique = true, name = "id")
    private Long id;

    @Column(nullable = false, unique = true, name = "name")
    private String name;

    @Column(name = "platforms")
    private String platforms;

    @Column(name = "release year")
    private Integer releaseYear;

    @Column(name = "company")
    private String company;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "price")
    private Double price;

    @Column(nullable = false, name = "stock")
    private Integer stock;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "added at", updatable = false, nullable = false)
    private Date addedAt;

}