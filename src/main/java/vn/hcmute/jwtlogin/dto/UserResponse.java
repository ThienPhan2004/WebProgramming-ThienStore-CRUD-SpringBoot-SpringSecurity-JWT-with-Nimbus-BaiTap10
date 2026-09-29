package vn.hcmute.jwtlogin.dto;

import vn.hcmute.jwtlogin.entity.Role;

import java.time.LocalDateTime;

/** Không chứa password để tránh lộ hash ra API. */
public record UserResponse(Long id, String fullName, String email, String images, Role role, LocalDateTime createdAt) {
}
