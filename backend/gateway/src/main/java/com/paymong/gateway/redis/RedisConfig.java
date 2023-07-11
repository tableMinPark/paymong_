package com.paymong.gateway.redis;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {
    @Value("${spring.redis.session.host}")
    private String sessionHost;

    @Value("${spring.redis.session.port}")
    private Integer sessionPort;

    @Value("${spring.redis.session.password}")
    private String sessionPassword;

    @Value("${spring.redis.listener_pattern}")
    private String listenerPattern;

    @Bean
    public Jackson2JsonRedisSerializer<Session> sessionObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper()
                .findAndRegisterModules()
                .enable(SerializationFeature.INDENT_OUTPUT)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .registerModules(new JavaTimeModule());

        Jackson2JsonRedisSerializer<Session> serializer = new Jackson2JsonRedisSerializer<>(Session.class);
        serializer.setObjectMapper(objectMapper);
        return serializer;
    }

    @Bean
    public Jackson2JsonRedisSerializer<Access> accessObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper()
                .findAndRegisterModules()
                .enable(SerializationFeature.INDENT_OUTPUT)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .registerModules(new JavaTimeModule());

        Jackson2JsonRedisSerializer<Access> serializer = new Jackson2JsonRedisSerializer<>(Access.class);
        serializer.setObjectMapper(objectMapper);
        return serializer;
    }

    @Bean
    public RedisTemplate<?, ?> redisSessionTemplate(
            @Qualifier("redisSessionConnectionFactory") RedisConnectionFactory redisConnectionFactory,
            @Qualifier("sessionObjectMapper") Jackson2JsonRedisSerializer<Session> serializer) {
        return getRedisTemplate(redisConnectionFactory, serializer);
    }

    @Bean
    public RedisTemplate<?, ?> redisAccessTemplate(
            @Qualifier("redisSessionConnectionFactory") RedisConnectionFactory redisConnectionFactory,
            @Qualifier("accessObjectMapper") Jackson2JsonRedisSerializer<Access> serializer) {
        return getRedisTemplate(redisConnectionFactory, serializer);
    }

    @Bean
    public RedisConnectionFactory redisSessionConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(sessionHost);
        redisStandaloneConfiguration.setPort(sessionPort);
        redisStandaloneConfiguration.setPassword(sessionPassword);
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory redisConnectionFactory, TokenExpirationListener tokenExpirationListener) {
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(redisConnectionFactory);
        redisMessageListenerContainer.addMessageListener(tokenExpirationListener, new PatternTopic(listenerPattern));
        return redisMessageListenerContainer;
    }

    private static RedisTemplate<?, ?> getRedisTemplate(RedisConnectionFactory redisConnectionFactory, Jackson2JsonRedisSerializer<?> serializer) {
        RedisTemplate<?, ?> redisSessionTemplate = new RedisTemplate<>();
        redisSessionTemplate.setKeySerializer(new StringRedisSerializer());
        redisSessionTemplate.setValueSerializer(serializer);
        redisSessionTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisSessionTemplate.setHashValueSerializer(serializer);
        redisSessionTemplate.setConnectionFactory(redisConnectionFactory);
        return redisSessionTemplate;
    }
}
