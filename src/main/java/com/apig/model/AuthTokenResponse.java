package com.apig.model;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AuthTokenResponse {
    private String bearerToken;
    private Date issuedAt;
    private Date reIssuedAt;
    private long expiration;
}
