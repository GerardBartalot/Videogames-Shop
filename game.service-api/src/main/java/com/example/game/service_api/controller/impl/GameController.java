package com.example.game.service_api.controller.impl;

import com.example.game.service_api.commons.entities.Game;
import com.example.game.service_api.controller.GameApi;
import com.example.game.service_api.services.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameController implements GameApi {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public ResponseEntity<Game> saveGame(@RequestHeader("userIdRequest") String userId, @RequestBody Game game) {
        System.out.println(userId);

        Game gameCreated = this.gameService.saveGame(game);
        return ResponseEntity.ok(gameCreated);
    }

    @Override
    public ResponseEntity<Game> getGameById(String id) {
        return ResponseEntity.ok(this.gameService.getGameById(id));
    }

    @Override
    public ResponseEntity<Game> updateGameByCriteria(String id, String name, Game game) {
        Game updatedGame = gameService.updateGameByCriteria(id, name, game);
        return ResponseEntity.ok(updatedGame);
    }

    @Override
    public ResponseEntity<Game> deleteGameByCriteria(String id, String name) {
        Game deletedGame = gameService.deleteGameByCriteria(id, name);
        return ResponseEntity.ok(deletedGame);
    }
}

