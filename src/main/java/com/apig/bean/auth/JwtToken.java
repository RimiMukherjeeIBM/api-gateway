package com.apig.bean.auth;

import com.apig.model.AuthTokenResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
public class JwtToken {

    private final HashMap<String, AuthTokenResponse> tokenCache= new HashMap<>();
    /*@Value("${clientIdSecret}")*/
    private HashMap<String, String> clientDetailsMap;

    private long getRemainingTimeToExpire(AuthTokenResponse authTokenResponse){
        return (authTokenResponse.getReIssuedAt().toInstant()
                        .plusMillis(authTokenResponse.getExpiration())).minusMillis(Instant.now().toEpochMilli()).toEpochMilli();
    }

    public AuthTokenResponse buildJwtToken(String clientId, String clientSecret) {
        AuthTokenResponse authTokenResponse = new AuthTokenResponse();
        long expiration;
        if (tokenCache.containsKey(clientId) && (expiration = getRemainingTimeToExpire(authTokenResponse = tokenCache.get(clientId))) > 0) {
            authTokenResponse.setReIssuedAt(Date.from(Instant.now()));
            authTokenResponse.setExpiration(expiration);
            return authTokenResponse;
        }

        if (clientDetailsMap.containsKey(clientId) && clientDetailsMap.get(clientId).equals(clientSecret)) {
            // Key is hardcoded here for simplicity.
            // Ideally this will get loaded from env configuration/secret vault
            String secret = "asdfSFS34wfsdfsdfSDSD32dfsddDDerQSNCK34SOWEK5354fdgdf4";

            Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret),
                    SignatureAlgorithm.HS256.getJcaName());
            final Map<String, Object> headerParams = new HashMap<>();
            headerParams.put("X-IBM-Client-Id", clientId);
            headerParams.put("X-IBM-Client-Secret", clientSecret);
            Instant now = Instant.now();
            Instant expiredAt = now.plusMillis(3000);
            String jwtToken = Jwts.builder()
                    /*.claim("name", "Jane Doe")
                    .claim("email", "jane@example.com")*/
                    .setHeaderParams(headerParams)
                    .setId(UUID.randomUUID().toString())
                    .setIssuedAt(Date.from(now))
                    .setExpiration(Date.from(expiredAt))
                    .signWith(hmacKey)
                    .compact();

            authTokenResponse.setBearerToken(jwtToken);
            authTokenResponse.setIssuedAt(Date.from(now));
            authTokenResponse.setReIssuedAt(Date.from(now));
            authTokenResponse.setExpiration(expiredAt.minusMillis(now.toEpochMilli()).toEpochMilli());
            tokenCache.put(clientId, authTokenResponse);
            return authTokenResponse;
        }else{
            return null;
        }
    }
}
