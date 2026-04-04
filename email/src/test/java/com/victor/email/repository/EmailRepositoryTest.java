package com.victor.email.repository;

import com.victor.email.model.EmailModel;
import com.victor.email.enums.EmailStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class EmailRepositoryTest {

    @Autowired
    private EmailRepository emailRepository;

    private EmailModel email;

    @BeforeEach
    void setUp() {
        email = new EmailModel();
        email.setUserId(UUID.randomUUID());
        email.setEmailFrom("sender@test.com");
        email.setEmailTo("recipient@test.com");
        email.setEmailSubject("Test Subject");
        email.setEmailBody("Test message content");
        email.setStatusEmail(EmailStatus.PENDING);
        email.setSendDateEmail(LocalDateTime.now());
    }

    @Test
    @DisplayName("Should save email successfully")
    void save_Success() {
        EmailModel saved = emailRepository.save(email);

        assertThat(saved).isNotNull();
        assertThat(saved.getEmailId()).isNotNull();
        assertThat(saved.getEmailTo()).isEqualTo("recipient@test.com");
        assertThat(saved.getStatusEmail()).isEqualTo(EmailStatus.PENDING);
    }

    @Test
    @DisplayName("Should find email by id")
    void findById_Found() {
        EmailModel saved = emailRepository.save(email);

        Optional<EmailModel> found = emailRepository.findById(saved.getEmailId());

        assertThat(found).isPresent();
        assertThat(found.get().getEmailTo()).isEqualTo("recipient@test.com");
    }

    @Test
    @DisplayName("Should return empty when id not found")
    void findById_NotFound() {
        Optional<EmailModel> found = emailRepository.findById(UUID.randomUUID());

        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("Should update email status")
    void update_Status() {
        EmailModel saved = emailRepository.save(email);
        saved.setStatusEmail(EmailStatus.SENT);

        EmailModel updated = emailRepository.save(saved);

        assertThat(updated.getStatusEmail()).isEqualTo(EmailStatus.SENT);
    }

    @Test
    @DisplayName("Should delete email successfully")
    void delete_Success() {
        EmailModel saved = emailRepository.save(email);
        UUID id = saved.getEmailId();

        emailRepository.deleteById(id);

        Optional<EmailModel> deleted = emailRepository.findById(id);
        assertThat(deleted).isEmpty();
    }

    @Test
    @DisplayName("Should count emails by status")
    void count_ByStatus() {
        emailRepository.save(email);

        EmailModel email2 = new EmailModel();
        email2.setUserId(UUID.randomUUID());
        email2.setEmailFrom("sender2@test.com");
        email2.setEmailTo("recipient2@test.com");
        email2.setEmailSubject("Test Subject 2");
        email2.setEmailBody("Test message 2");
        email2.setStatusEmail(EmailStatus.SENT);
        email2.setSendDateEmail(LocalDateTime.now());
        emailRepository.save(email2);

        long count = emailRepository.count();

        assertThat(count).isGreaterThanOrEqualTo(2);
    }
}
