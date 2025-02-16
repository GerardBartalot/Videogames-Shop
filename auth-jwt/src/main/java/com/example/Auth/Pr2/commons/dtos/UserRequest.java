package com.example.Auth.Pr2.commons.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {
    @NonNull
    private String email;
    private String password;

}
