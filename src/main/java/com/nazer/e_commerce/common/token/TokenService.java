package com.nazer.e_commerce.common.token;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.nazer.e_commerce.auth.Dto.LoginResponseDto;
import com.nazer.e_commerce.users.enums.UserRoles;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;


@Service
public class TokenService {

    @Value("${app.jwt.jwtSecretKey}")
    private String secretKey;

    @Value("${app.jwt.accessTokenExpiration}")
    private Long accessTokenExpiration;

    @Value("${app.jwt.refreshTokenExpiration}")
    private Long refreshTokenExpiration;

    private SecretKey key;

    @PostConstruct
    public void init(){
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }


    // Access Token
    public String generateAccessToken(TokenPayload payload) {
        System.out.println("[TokenService] generateAccessToken() called for user: " + payload.getId().toHexString());
        String token = buildToken(payload, accessTokenExpiration);
        System.out.println("[TokenService] Access token generated successfully");
        return token;
    }

    // Refresh Token
    public String generateRefreshToken(TokenPayload payload) {
        System.out.println("[TokenService] generateRefreshToken() called for user: " + payload.getId().toHexString());
        String token = buildToken(payload, refreshTokenExpiration);
        System.out.println("[TokenService] Refresh token generated successfully");
        return token;
    }

    private String buildToken(TokenPayload payload , Long expiration){
        try {
            Map<String, Object> claims = new HashMap<>();
            claims.put("role", payload.getRole());

            String token = Jwts.builder()
                .setSubject(payload.getId().toHexString())
                .addClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
            return token;
        } catch (Exception e) {
            // TODO: handle exception
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    public TokenPayload decodeToken(String token){

        try {
            Claims claims = Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJwt(token)
                .getBody();
            return TokenPayload.builder()
                .id(new ObjectId(claims.getSubject()))
                .role(UserRoles.valueOf(claims.get("role", String.class)))
                .build();
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    public LoginResponseDto generateTokens(TokenPayload payload){
        String accessToken = generateAccessToken(payload);
        String refreshToken = generateRefreshToken(payload);
        return new LoginResponseDto(accessToken, refreshToken);
    }
}
 

    