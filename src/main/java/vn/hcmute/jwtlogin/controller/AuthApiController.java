package vn.hcmute.jwtlogin.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.hcmute.jwtlogin.dto.LoginRequest;
import vn.hcmute.jwtlogin.dto.LoginResponse;
import vn.hcmute.jwtlogin.dto.RegisterRequest;
import vn.hcmute.jwtlogin.dto.UserResponse;
import vn.hcmute.jwtlogin.entity.User;
import vn.hcmute.jwtlogin.filter.JwtAuthenticationFilter;
import vn.hcmute.jwtlogin.mapper.UserMapper;
import vn.hcmute.jwtlogin.service.AuthenticationService;
import vn.hcmute.jwtlogin.service.JwtService;

import java.time.Duration;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthApiController {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    private final UserMapper userMapper;

    @Value("${security.jwt.cookie-secure:false}")
    private boolean cookieSecure;

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(@Valid @RequestBody RegisterRequest request) {
        User user = authenticationService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toResponse(user));
    }

    /** Trả JWT trong body (cho Postman/mobile) và đồng thời set cookie HttpOnly (cho trình duyệt). */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        User user = authenticationService.authenticate(request);
        String token = jwtService.generateToken(user);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE,
                        buildCookie(token, Duration.ofMillis(jwtService.getExpirationMs())).toString())
                .body(new LoginResponse(token, "Bearer", jwtService.getExpirationMs()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, buildCookie("", Duration.ZERO).toString())
                .build();
    }

    private ResponseCookie buildCookie(String value, Duration maxAge) {
        return ResponseCookie.from(JwtAuthenticationFilter.COOKIE_NAME, value)
                .httpOnly(true)
                .secure(cookieSecure)
                .sameSite("Strict")
                .path("/")
                .maxAge(maxAge)
                .build();
    }
}
