package com.victor.email.consumer;

import com.victor.email.dto.EmailDto;
import com.victor.email.enums.EmailStatus;
import com.victor.email.model.EmailModel;
import com.victor.email.repository.EmailRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EmailConsumer {

    private final EmailRepository emailRepository;

    public EmailConsumer(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }

    @RabbitListener(queues = "${broker.queue.email.name}")
    public void listenEmailQueue(@Payload EmailDto emailDto){
        EmailModel emailModel = new EmailModel();
        emailModel.setUserId(emailDto.id());
        emailModel.setEmailTo(emailDto.emailTo());
        emailModel.setEmailSubject(emailDto.subject());
        emailModel.setEmailBody(emailDto.body());
        emailModel.setStatusEmail(EmailStatus.PENDING);
        emailModel.setSendDateEmail(LocalDateTime.now());

        emailRepository.save(emailModel);
    }
}
