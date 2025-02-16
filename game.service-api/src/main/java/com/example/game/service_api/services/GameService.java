package com.example.game.service_api.services;

import com.example.game.service_api.commons.entities.Game;
import com.example.game.service_api.commons.entities.PurchasedGame;

import java.util.Date;
import java.util.List;

public interface GameService {
    Game createGame(Game gameRequest);

    List<Game> getAllGames();

    Object getGame(Long id, String name, String platforms,
                   Integer releaseYear, String company, Double rating, Double price, Integer stock, Date addedAt);

    Object updateGame(Long id, String name, String platforms,
                      Integer releaseYear, String company, Double rating, Double price, Integer stock, Date addedAt,
                      Game game);

    Object deleteGame(Long id, String name, String platforms, Integer releaseYear, String company, Double rating,
                      Double price, Integer stock, Date addedAt);

    // Nuevo: Compra de juegos
    PurchasedGame buyGame(Long gameId, Long userId, String userEmail);

    // Nuevo: Obtener los juegos comprados por un usuario
    List<PurchasedGame> getPurchasedGamesByUser(Long userId);
}