package com.example.ecommerce.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.queue.sales}")
    private String salesQueue;

    @Value("${rabbitmq.queue.promotions}")
    private String promotionsQueue;

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.routing.sales}")
    private String salesRoutingKey;

    @Value("${rabbitmq.routing.promotions}")
    private String promotionsRoutingKey;

    // Queues
    @Bean
    public Queue salesQueue() {
        return new Queue(salesQueue, true);
    }

    @Bean
    public Queue promotionsQueue() {
        return new Queue(promotionsQueue, true);
    }

    // Exchange
    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(exchange);
    }

    // Bindings
    @Bean
    public Binding salesBinding() {
        return BindingBuilder
                .bind(salesQueue())
                .to(exchange())
                .with(salesRoutingKey);
    }

    @Bean
    public Binding promotionsBinding() {
        return BindingBuilder
                .bind(promotionsQueue())
                .to(exchange())
                .with(promotionsRoutingKey);
    }

    // Message converter
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // RabbitTemplate
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}