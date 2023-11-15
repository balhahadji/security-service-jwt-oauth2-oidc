package tn.mycompany.securityservice.dto;

public record LoginRequest(
        String grantType,
        String username,
        String password,
        boolean withRefreshToken,
        String refreshToken
        ) {
}
