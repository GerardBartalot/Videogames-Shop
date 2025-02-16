package com.example.eligibility_micro.common;

import lombok.*;

@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GameEligibleEvent {
    private Integer id;
    private String name;
    private Integer userId;
    private boolean isEligible;
}
