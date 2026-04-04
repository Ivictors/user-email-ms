package com.victor.user.repository;

import com.victor.user.model.UserModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private UserModel user;

    @BeforeEach
    void setUp() {
        user = new UserModel();
        user.setUsername("Victor");
        user.setEmail("victor@test.com");
    }

    @Test
    @DisplayName("Should save user successfully")
    void save_Success() {
        UserModel saved = userRepository.save(user);

        assertThat(saved).isNotNull();
        assertThat(saved.getUserId()).isNotNull();
        assertThat(saved.getUsername()).isEqualTo("Victor");
        assertThat(saved.getEmail()).isEqualTo("victor@test.com");
    }

    @Test
    @DisplayName("Should find user by email")
    void findByEmail_Found() {
        userRepository.save(user);

        Optional<UserModel> found = userRepository.findByEmail("victor@test.com");

        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("victor@test.com");
    }

    @Test
    @DisplayName("Should return empty when email not found")
    void findByEmail_NotFound() {
        Optional<UserModel> found = userRepository.findByEmail("nonexistent@email.com");

        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("Should find user by id")
    void findById_Found() {
        UserModel saved = userRepository.save(user);

        Optional<UserModel> found = userRepository.findById(saved.getUserId());

        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("Victor");
    }

    @Test
    @DisplayName("Should return empty when id not found")
    void findById_NotFound() {
        Optional<UserModel> found = userRepository.findById(UUID.randomUUID());

        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("Should delete user successfully")
    void delete_Success() {
        UserModel saved = userRepository.save(user);
        UUID id = saved.getUserId();

        userRepository.deleteById(id);

        Optional<UserModel> deleted = userRepository.findById(id);
        assertThat(deleted).isEmpty();
    }
}
