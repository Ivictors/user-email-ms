package com.victor.user.producers;

import com.victor.user.dto.EmailDto;
import com.victor.user.model.UserModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserProducer {
    private final RabbitTemplate rabbitTemplate;

    @Value(value = "${broker.queue.email.name}")
    private String routingkey;

    public UserProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishMessageEmail(UserModel userModel) {
        var emailDto = new EmailDto();

        emailDto.setEmailTo(userModel.getEmail());
        emailDto.setSubject("Register user with sucess!");
        emailDto.setBody(userModel.getUsername() + "Welcome");

        rabbitTemplate.convertAndSend("", routingkey, emailDto);
    }
}
