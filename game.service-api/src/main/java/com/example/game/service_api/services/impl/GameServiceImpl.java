package com.example.game.service_api.services.impl;

import com.example.game.service_api.commons.entities.Game;
import com.example.game.service_api.commons.entities.PurchasedGame;
import com.example.game.service_api.commons.exceptions.GameException;
import com.example.game.service_api.repositories.GameRepository;
import com.example.game.service_api.repositories.PurchasedGameRepository;
import com.example.game.service_api.services.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final PurchasedGameRepository purchasedGameRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public GameServiceImpl(GameRepository gameRepository, PurchasedGameRepository purchasedGameRepository, KafkaTemplate<String, Object> kafkaTemplate) {
        this.gameRepository = gameRepository;
        this.purchasedGameRepository = purchasedGameRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public Game createGame(Game gameRequest) {
        if (gameRepository.findByName(gameRequest.getName()).isPresent()) {
            throw new GameException(HttpStatus.CONFLICT, "This game already exists");
        } else {
            Game savedGame = gameRepository.save(gameRequest);
            kafkaTemplate.send("event.game-created", savedGame);
            return savedGame;
        }
    }

    @Override
    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    @Override
    public Object getGame(Long id, String name, String platforms, Integer releaseYear, String company,
                          Double rating, Double price, Integer stock, Date addedAt) {
        if (id != null) {
            try {
                return gameRepository.findById(id)
                        .orElseThrow(() -> new GameException(HttpStatus.NOT_FOUND, "No games found with this id"));
            } catch (NumberFormatException e) {
                throw new GameException(HttpStatus.BAD_REQUEST, "Invalid game ID format");
            }
        }

        if (name != null) {
            return gameRepository.findByName(name)
                    .orElseThrow(() -> new GameException(HttpStatus.NOT_FOUND, "No games found with this name"));
        }

        // Si no se pasa ni ID ni Name, aplicar filtros
        if (platforms == null && releaseYear == null && company == null && rating == null && price == null
                && stock == null && addedAt == null) {
            throw new GameException(HttpStatus.BAD_REQUEST, "At least one filter must be provided");
        }

        List<Game> games = gameRepository.findAll();

        if (platforms != null && !platforms.isEmpty()) {
            List<Game> filtered = gameRepository.findByPlatforms(platforms);

            if (filtered.isEmpty()) {
                filtered = games.stream()
                        .filter(game -> Arrays.asList(game.getPlatforms().split(", ")).contains(platforms))
                        .collect(Collectors.toList());
            }

            if (filtered.isEmpty()) {
                throw new GameException(HttpStatus.NOT_FOUND,("No games found with this platform"));
            } else {
                games = filtered;
            }
        }

        if (releaseYear != null) {
            List<Game> filtered = gameRepository.findByReleaseYear(releaseYear);
            if (filtered.isEmpty()) {
                throw new GameException(HttpStatus.NOT_FOUND,("No games found with this release year"));
            } else {
                games.retainAll(filtered);
            }
        }

        if (company != null) {
            List<Game> filtered = gameRepository.findByCompany(company);
            if (filtered.isEmpty()) {
                throw new GameException(HttpStatus.NOT_FOUND,("No games found with this company"));
            } else {
                games.retainAll(filtered);
            }
        }

        if (rating != null) {
            List<Game> filtered = gameRepository.findByRating(rating);
            if (filtered.isEmpty()) {
                throw new GameException(HttpStatus.NOT_FOUND,("No games found with this rating"));
            } else {
                games.retainAll(filtered);
            }
        }

        if (price != null) {
            List<Game> filtered = gameRepository.findByPrice(price);
            if (filtered.isEmpty()) {
                throw new GameException(HttpStatus.NOT_FOUND,("No games found with this rating"));
            } else {
                games.retainAll(filtered);
            }
        }

        if (stock != null) {
            List<Game> filtered = gameRepository.findByStock(stock);
            if (filtered.isEmpty()) {
                throw new GameException(HttpStatus.NOT_FOUND,("No games found with this rating"));
            } else {
                games.retainAll(filtered);
            }
        }

        if (addedAt != null) {
            List<Game> filtered = gameRepository.findByAddedAt(addedAt);
            if (filtered.isEmpty()) {
                throw new GameException(HttpStatus.NOT_FOUND,("No games found with this update date"));
            } else {
                games.retainAll(filtered);
            }
        }

        if (games.isEmpty()) {
            throw new GameException(HttpStatus.NOT_FOUND, "No games found with the given criteria");
        }

        return games;
    }

    @Override
    public Object updateGame(Long id, String name, String platforms, Integer releaseYear, String company,
                             Double rating, Double price, Integer stock, Date addedAt, Game game) {

        Object result;

        // Si se pasa id o name, buscamos solo un juego.
        if (id != null || name != null) {
            result = getGame(id, name, null, null, null, null, null,
                    null, null);

            if (result instanceof Game existingGame) {
                updateOnlyProvidedFields(existingGame, platforms, releaseYear, company,
                        rating, price, stock, addedAt, game);
                return gameRepository.save(existingGame);
            } else {
                throw new GameException(HttpStatus.NOT_FOUND, "No game found with the given id or name.");
            }
        }

        // Si no se pasó id o name, buscamos múltiples juegos usando otros filtros.
        result = getGame(null, null, platforms, releaseYear, company, rating,
                price, stock, addedAt);

        if (result instanceof List) {
            List<Game> gamesToUpdate = (List<Game>) result;

            if (gamesToUpdate.isEmpty()) {
                throw new GameException(HttpStatus.NOT_FOUND, "No games found with the given filters.");
            }

            for (Game existingGame : gamesToUpdate) {
                updateOnlyProvidedFields(existingGame, platforms, releaseYear, company,
                        rating, price, stock, addedAt, game);
            }

            return gameRepository.saveAll(gamesToUpdate);
        }

        throw new GameException(HttpStatus.NOT_FOUND, "No games found to update.");
    }

    private void updateOnlyProvidedFields(Game existingGame, String platforms, Integer releaseYear,
                                          String company, Double rating, Double price, Integer stock,
                                          Date addedAt, Game game) {
        boolean updated = false;

        if (game.getName() != null) {
            existingGame.setName(game.getName());
            updated = true;
        }

        if (game.getPlatforms() != null) {
            existingGame.setPlatforms(game.getPlatforms());
            updated = true;
        }

        if (game.getReleaseYear() != null) {
            existingGame.setReleaseYear(game.getReleaseYear());
            updated = true;
        }

        if (game.getCompany() != null) {
            existingGame.setCompany(game.getCompany());
            updated = true;
        }

        if (game.getRating() != null) {
            existingGame.setRating(game.getRating());
            updated = true;
        }

        if (game.getPrice() != null) {
            existingGame.setPrice(game.getPrice());
            updated = true;
        }

        if (game.getStock() != null) {
            existingGame.setStock(game.getStock());
            updated = true;
        }

        if (game.getAddedAt() != null) {
            existingGame.setAddedAt(game.getAddedAt());
            updated = true;
        }
    }

    @Override
    public Object deleteGame(Long id, String name, String platforms, Integer releaseYear, String company,
                             Double rating, Double price, Integer stock, Date addedAt) {

        Object result = getGame(id, name, platforms, releaseYear, company, rating, price, stock, addedAt);

        if (result instanceof Game gameToDelete) {
            gameRepository.delete(gameToDelete);
            return gameToDelete;
        } else {
            List<Game> gamesToDelete = (List<Game>) result;
            if (gamesToDelete.isEmpty()) {
                throw new GameException(HttpStatus.NOT_FOUND, "No games found to delete.");
            }
            gameRepository.deleteAll(gamesToDelete);
            return gamesToDelete;
        }
    }

    @Override
    public List<PurchasedGame> getPurchasedGamesByUser(Long userId) {
        // Verificar que el userId sea válido
        if (userId == null || userId <= 0) {
            throw new GameException(HttpStatus.BAD_REQUEST, "Invalid user ID");
        }

        // Obtener los juegos comprados por el usuario
        List<PurchasedGame> purchasedGames = purchasedGameRepository.findByUserId(userId);

        // Verificar si el usuario tiene juegos comprados
        if (purchasedGames.isEmpty()) {
            throw new GameException(HttpStatus.NOT_FOUND, "No purchased games found for this user");
        }

        return purchasedGames;
    }

    @Override
    public PurchasedGame buyGame(Long gameId, Long userId, String userEmail) {
        // Verificar que los parámetros sean válidos
        if (gameId == null || gameId <= 0) {
            throw new GameException(HttpStatus.BAD_REQUEST, "Invalid game ID");
        }
        if (userId == null || userId <= 0) {
            throw new GameException(HttpStatus.BAD_REQUEST, "Invalid user ID");
        }
        if (userEmail == null || userEmail.trim().isEmpty()) {
            throw new GameException(HttpStatus.BAD_REQUEST, "User email cannot be empty");
        }

        // Verificar si el juego existe
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new GameException(HttpStatus.NOT_FOUND, "Game not found"));

        // Verificar si el usuario ya ha comprado el juego
        if (purchasedGameRepository.existsByGameIdAndUserId(gameId, userId)) {
            throw new GameException(HttpStatus.CONFLICT, "User already owns this game");
        }

        // Crear un nuevo registro de compra en `purchasedGames`
        PurchasedGame purchasedGame = PurchasedGame.builder()
                .gameId(game.getId())
                .gameName(game.getName())
                .gamePrice(game.getPrice())
                .gamePlatforms(game.getPlatforms())
                .userId(userId)
                .userEmail(userEmail)
                .build();

        // Guardar la compra en la base de datos
        purchasedGameRepository.save(purchasedGame);

        // Emitir evento de compra exitosa en Kafka
        kafkaTemplate.send("event.payment-success", purchasedGame);

        return purchasedGame;
    }


}