package com.victor.user.service;

import com.victor.user.dto.UserDto;
import com.victor.user.model.UserModel;
import com.victor.user.producers.UserProducer;
import com.victor.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserProducer userProducer;

    @InjectMocks
    private UserService userService;

    private UserModel user;
    private UserDto userDto;
    private UUID uuid;

    @BeforeEach
    void setUp() {
        uuid = UUID.randomUUID();
        
        user = new UserModel();
        user.setUserId(uuid);
        user.setUsername("Victor");
        user.setEmail("victor@test.com");

        userDto = new UserDto("Victor", "victor@test.com");
    }

    @Test
    @DisplayName("Should find user by id successfully")
    void findById_Success() {
        when(userRepository.findById(uuid)).thenReturn(Optional.of(user));

        UserModel result = userService.findById(uuid);

        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(uuid);
        assertThat(result.getUsername()).isEqualTo("Victor");
        assertThat(result.getEmail()).isEqualTo("victor@test.com");

        verify(userRepository).findById(uuid);
    }

    @Test
    @DisplayName("Should throw exception when user not found")
    void findById_NotFound() {
        when(userRepository.findById(uuid)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            userService.findById(uuid);
        });

        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        verify(userRepository).findById(uuid);
    }

    @Test
    @DisplayName("Should save user successfully")
    void save_Success() {
        when(userRepository.findByEmail(userDto.email())).thenReturn(Optional.empty());
        when(userRepository.save(any(UserModel.class))).thenReturn(user);

        userService.save(userDto);

        verify(userRepository).findByEmail(userDto.email());
        verify(userRepository).save(any(UserModel.class));
        verify(userProducer).publishMessageEmail(any(UserModel.class));
    }

    @Test
    @DisplayName("Should throw exception when email already exists")
    void save_EmailAlreadyExists() {
        when(userRepository.findByEmail(userDto.email())).thenReturn(Optional.of(user));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            userService.save(userDto);
        });

        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        verify(userRepository).findByEmail(userDto.email());
        verify(userRepository, never()).save(any());
        verify(userProducer, never()).publishMessageEmail(any());
    }
}
