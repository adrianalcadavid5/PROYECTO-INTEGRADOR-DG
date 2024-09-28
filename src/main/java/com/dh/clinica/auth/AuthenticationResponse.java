package com.dh.clinica.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data// getter y setter juntos
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
    private String token;
}
