package com.example.sessiondemo.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.SessionRepository;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.session.data.redis.RedisSessionRepository;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;

import java.time.Duration;

//@Configuration
//@EnableRedisHttpSession
//public class SessionConfig extends AbstractHttpSessionApplicationInitializer {
//
//    @Bean
//    public JedisConnectionFactory jedisConnectionFactory() {
//        return new JedisConnectionFactory();
//    }
//
//    @Bean("redisEmpTemplate")
//    public RedisTemplate<String, Object> redisTemplate() {
//        RedisTemplate<String, Object> template = new RedisTemplate<>();
//        template.setConnectionFactory(jedisConnectionFactory());
//        Jackson2JsonRedisSerializer<Object> jsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
//        template.setKeySerializer(new StringRedisSerializer());
//        //template.setHashKeySerializer(new StringRedisSerializer());
//
//        template.setValueSerializer(jsonRedisSerializer);
//        template.setHashKeySerializer(jsonRedisSerializer);
//        //template.setConnectionFactory(connectionFactory);
//        return template;
//    }
//}


@EnableSpringHttpSession
public class SessionConfig {

    private final RedisConnectionFactory redisConnectionFactory;

    public SessionConfig(ObjectProvider<RedisConnectionFactory> redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory.getIfAvailable();
    }

//    @Bean
//    public RedisOperations<String, Object> sessionRedisOperations() {
//        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(this.redisConnectionFactory);
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
//        return redisTemplate;
//    }

//    @Bean
//    public RedisSessionRepository sessionRepository(RedisOperations<String, Object> sessionRedisOperations) {
//        RedisSessionRepository sessionRepository = new RedisSessionRepository(sessionRedisOperations);
//        sessionRepository.setDefaultMaxInactiveInterval(Duration.ofMinutes(2));
//        return sessionRepository;

    @Bean
    public RedisOperations<Object, Object> sessionRedisOperations() {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(this.redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }

    @Bean
    public SessionRepository sessionRepository(RedisOperations<Object, Object> sessionRedisOperations) {
        RedisIndexedSessionRepository sessionRepository = new RedisIndexedSessionRepository(sessionRedisOperations);
        sessionRepository.setDefaultMaxInactiveInterval(180);
        return sessionRepository;
    }

}