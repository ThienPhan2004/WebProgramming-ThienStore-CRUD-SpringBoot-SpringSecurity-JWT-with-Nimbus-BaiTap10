package vn.hcmute.jwtlogin.dto;

public record LoginResponse(String token, String tokenType, long expiresIn) {
}
