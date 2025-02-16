package com.example.game.service_api.repositories;

import com.example.game.service_api.commons.entities.PurchasedGame;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PurchasedGameRepository extends JpaRepository<PurchasedGame, Long> {
    List<PurchasedGame> findByUserId(Long userId);
    boolean existsByGameIdAndUserId(Long gameId, Long userId);
}