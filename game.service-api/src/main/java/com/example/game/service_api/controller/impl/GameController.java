package com.example.game.service_api.controller.impl;

import com.example.game.service_api.commons.dto.GamePostRequest;
import com.example.game.service_api.commons.dto.GamePutRequest;
import com.example.game.service_api.commons.entities.Game;
import com.example.game.service_api.commons.entities.PurchasedGame;
import org.modelmapper.ModelMapper;
import com.example.game.service_api.controller.GameApi;
import com.example.game.service_api.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
public class GameController implements GameApi {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ResponseEntity<Game> createGame(GamePostRequest gamePostRequest) {
        Game game = modelMapper.map(gamePostRequest, Game.class);
        Game gameCreated = this.gameService.createGame(game);
        return ResponseEntity.ok(gameCreated);
    }

    @Override
    public ResponseEntity<List<Game>> getAllGames() {
        return ResponseEntity.ok(this.gameService.getAllGames());
    }

    @Override
    public ResponseEntity<Object> getGame(Long id, String name,
                                          String platforms, Integer releaseYear, String company, Double rating,
                                          Double price, Integer stock,
                                          Date addedAt) {
        return ResponseEntity.ok(this.gameService.getGame(id, name, platforms,
                                                        releaseYear, company, rating, price, stock, addedAt));
    }

    @Override
    public ResponseEntity<Object> updateGame(Long id, String name,
                                             String platforms, Integer releaseYear, String company,
                                             Double rating, Double price, Integer stock, Date addedAt,
                                             GamePutRequest gamePutRequest) {
        Game game = modelMapper.map(gamePutRequest, Game.class);

        return ResponseEntity.ok(this.gameService.updateGame(id, name, platforms, releaseYear, company,
                                                                rating, price, stock, addedAt, game
        ));
    }

    @Override
    public ResponseEntity<Object> deleteGame(Long id, String name,
                                             String platforms, Integer releaseYear, String company,
                                             Double rating, Double price, Integer stock, Date addedAt) {
        Game deletedGame = (Game) gameService.deleteGame(id, name, platforms, releaseYear, company, rating,
                                                            price, stock, addedAt);
        return ResponseEntity.ok(deletedGame);
    }

    @Override
    public ResponseEntity<List<PurchasedGame>> getPurchasedGamesByUser(Long userId) {
        return ResponseEntity.ok(gameService.getPurchasedGamesByUser(userId));
    }

    @Override
    public ResponseEntity<PurchasedGame> buyGame(Long gameId, Long userId, String userEmail) {
        return ResponseEntity.ok(gameService.buyGame(gameId, userId, userEmail));
    }


}