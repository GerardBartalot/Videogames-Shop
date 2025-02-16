package com.example.game.service_api.controller;

import com.example.game.service_api.commons.constants.ApiPathVariables;
import com.example.game.service_api.commons.entities.Game;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(ApiPathVariables.V1_ROUTE + ApiPathVariables.GAME_ROUTE)
public interface GameApi {
    @PostMapping
    ResponseEntity<Game> saveGame(@RequestHeader("userIdRequest") String userId, @RequestBody Game game);
    @GetMapping
    ResponseEntity<Game> getGameById(@PathVariable String id);
    @PutMapping
    ResponseEntity<Game> updateGameByCriteria(@RequestParam(required = false) String id,
                                              @RequestParam(required = false) String name,
                                              @RequestBody Game game);
    @DeleteMapping
    ResponseEntity<Game> deleteGameByCriteria(
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String name);

}
