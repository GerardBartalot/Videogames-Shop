package com.example.game.service_api.repositories;

import com.example.game.service_api.commons.entities.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {
    Optional<Game> findByName(String name);
    List<Game> findByPlatforms(String platforms);
    List<Game> findByReleaseYear(Integer releaseYear);
    List<Game> findByCompany(String company);
    List<Game> findByRating(Double rating);
    List<Game> findByPrice(Double price);
    List<Game> findByStock(Integer stock);
    List<Game> findByAddedAt(Date addedAt);

}