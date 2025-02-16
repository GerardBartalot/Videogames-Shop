package com.example.game.service_api.controller;

import com.example.game.service_api.commons.constants.ApiPathVariables;
import com.example.game.service_api.commons.dto.GamePostRequest;
import com.example.game.service_api.commons.dto.GamePutRequest;
import com.example.game.service_api.commons.entities.Game;
import com.example.game.service_api.commons.entities.PurchasedGame;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RequestMapping(ApiPathVariables.API_ROUTE + ApiPathVariables.GAME_ROUTE)
public interface GameApi {

    @PostMapping ("/create")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<Game> createGame(
            @RequestBody GamePostRequest gamePostRequest);


    @GetMapping("/all")
    ResponseEntity<List<Game>> getAllGames();

    @GetMapping("/details")
    ResponseEntity<Object> getGame(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String platforms,
            @RequestParam(required = false) Integer releaseYear,
            @RequestParam(required = false) String company,
            @RequestParam(required = false) Double rating,
            @RequestParam(required = false) Double price,
            @RequestParam(required = false) Integer stock,
            @RequestParam(required = false) Date addedAt);

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<Object> updateGame(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String platforms,
            @RequestParam(required = false) Integer releaseYear,
            @RequestParam(required = false) String company,
            @RequestParam(required = false) Double rating,
            @RequestParam(required = false) Double price,
            @RequestParam(required = false) Integer stock,
            @RequestParam(required = false) Date addedAt,
            @RequestBody (required = false) GamePutRequest gamePutRequest);

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<Object> deleteGame(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String platforms,
            @RequestParam(required = false) Integer releaseYear,
            @RequestParam(required = false) String company,
            @RequestParam(required = false) Double rating,
            @RequestParam(required = false) Double price,
            @RequestParam(required = false) Integer stock,
            @RequestParam(required = false) Date addedAt);

    @GetMapping("/purchased")
    @PreAuthorize("hasRole('USER')")
    ResponseEntity<List<PurchasedGame>> getPurchasedGamesByUser(
            @RequestHeader("userIdRequest") Long userId);

    @PostMapping("/buy/{gameId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PurchasedGame> buyGame(
            @PathVariable Long gameId,
            @RequestHeader("userIdRequest") Long userId,
            @RequestHeader("userEmailRequest") String userEmail);
}