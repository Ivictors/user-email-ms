package com.victor.email.consumer;

import com.victor.email.dto.EmailDto;
import com.victor.email.enums.EmailStatus;
import com.victor.email.model.EmailModel;
import com.victor.email.repository.EmailRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.*;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@ActiveProfiles("test")
class EmailConsumerIntegrationTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private EmailRepository emailRepository;

    private EmailDto emailDto;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        emailDto = new EmailDto(userId, "Test Subject", "test@email.com", "Test body");
    }

    @Test
    @DisplayName("Should consume message from RabbitMQ and save to database")
    void shouldConsumeMessageAndSaveToDatabase() {
        rabbitTemplate.convertAndSend("default.email", emailDto);

        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            EmailModel savedEmail = emailRepository.findAll().stream()
                    .filter(e -> e.getEmailTo().equals("test@email.com"))
                    .findFirst()
                    .orElse(null);

            assertThat(savedEmail).isNotNull();
            assertThat(savedEmail.getEmailTo()).isEqualTo("test@email.com");
            assertThat(savedEmail.getEmailSubject()).isEqualTo("Test Subject");
            assertThat(savedEmail.getEmailBody()).isEqualTo("Test body");
            assertThat(savedEmail.getStatusEmail()).isEqualTo(EmailStatus.PENDING);
            assertThat(savedEmail.getUserId()).isEqualTo(userId);
        });
    }

    @Test
    @DisplayName("Should set send date when consuming message")
    void shouldSetSendDateWhenConsumingMessage() {
        rabbitTemplate.convertAndSend("default.email", emailDto);

        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            EmailModel savedEmail = emailRepository.findAll().stream()
                    .filter(e -> e.getEmailTo().equals("test@email.com"))
                    .findFirst()
                    .orElse(null);

            assertThat(savedEmail).isNotNull();
            assertThat(savedEmail.getSendDateEmail()).isNotNull();
        });
    }
}
