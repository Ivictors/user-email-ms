package com.victor.user.service;

import com.victor.user.dto.UserDto;
import com.victor.user.model.UserModel;
import com.victor.user.producers.UserProducer;
import com.victor.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserProducer userProducer;

    public UserService(UserRepository userRepository, UserProducer userProducer) {
        this.userRepository = userRepository;
        this.userProducer = userProducer;
    }

    public UserModel findById(UUID uuid) {
        return userRepository.findById(uuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT));
    }

    @Transactional
    public void save(UserDto userDto) {
        if (userRepository.findByEmail(userDto.email()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        var newUser = new UserModel();
        newUser.setUsername(userDto.username());
        newUser.setEmail(userDto.email());

        userRepository.save(newUser);

        userProducer.publishMessageEmail(newUser);
    }
}
