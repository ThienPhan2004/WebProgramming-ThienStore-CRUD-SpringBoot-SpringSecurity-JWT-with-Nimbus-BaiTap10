package vn.hcmute.jwtlogin.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Họ tên không được để trống") @Size(max = 100) String fullName,
        @NotBlank(message = "Email không được để trống") @Email(message = "Email không hợp lệ") @Size(max = 100) String email,
        @NotBlank(message = "Mật khẩu không được để trống") @Size(min = 6, max = 72, message = "Mật khẩu từ 6 đến 72 ký tự") String password) {
}
