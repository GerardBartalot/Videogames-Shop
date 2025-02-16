package com.example.game.service_api.commons.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GamePutRequest {
    private String name;
    private String platforms;
    private Integer releaseYear;
    private String company;
    private Double rating;
    private Double price;
    private Integer stock;
}