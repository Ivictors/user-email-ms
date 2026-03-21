package com.victor.email.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.json.JsonMapper;

@Configuration
public class RabbitMq {

    @Value("${broker.queue.email.name}")
    private String queueName;

    @Bean
    public Queue queue(){
        return new Queue(queueName,true);
    }
    @Bean
    public JacksonJsonMessageConverter messageConverter(){
        JsonMapper jsonMapper = JsonMapper.builder().build();
        return new JacksonJsonMessageConverter(jsonMapper);
    }
}
