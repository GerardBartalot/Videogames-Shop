package com.example.eligibility_micro.processors;

import com.example.eligibility_micro.common.GameCreatedEvent;
import com.example.eligibility_micro.common.GameEligibleEvent;
import com.example.eligibility_micro.services.GameEligibleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@Slf4j
public class EligibilityGameProcessor {
    private final GameEligibleService gameEligibleService;

    public EligibilityGameProcessor(GameEligibleService gameEligibleService) {
        this.gameEligibleService = gameEligibleService;
    }

    public Flux<GameEligibleEvent> process(Flux<GameCreatedEvent> gameCreatedEventFlux) {
        return gameCreatedEventFlux.doOnNext(given -> log.info("Entry event: {}", given))
                .flatMap(gameEligibleService::eligibilityGame)
                .onErrorContinue(this::handleError);
    }

    private void handleError(Throwable throwable, Object object) {
        log.error("Error processing event: {}", object, throwable);
    }
}

