package vn.hcmute.jwtlogin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.hcmute.jwtlogin.dto.LoginRequest;
import vn.hcmute.jwtlogin.dto.RegisterRequest;
import vn.hcmute.jwtlogin.entity.Role;
import vn.hcmute.jwtlogin.entity.User;
import vn.hcmute.jwtlogin.exception.EmailAlreadyExistsException;
import vn.hcmute.jwtlogin.mapper.UserMapper;
import vn.hcmute.jwtlogin.repository.UserRepository;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    @Transactional
    public User signup(RegisterRequest request) {
        String email = normalize(request.email());
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email);
        }
        User user = userMapper.toEntity(request);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.USER);
        return userRepository.save(user);
    }

    public User authenticate(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(normalize(request.email()), request.password()));
        return (User) authentication.getPrincipal();
    }

    private String normalize(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
