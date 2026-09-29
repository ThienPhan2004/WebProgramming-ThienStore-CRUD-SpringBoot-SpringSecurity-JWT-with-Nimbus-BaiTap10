package vn.hcmute.jwtlogin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.hcmute.jwtlogin.entity.User;
import vn.hcmute.jwtlogin.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> allUsers() {
        return userRepository.findAll();
    }
}
