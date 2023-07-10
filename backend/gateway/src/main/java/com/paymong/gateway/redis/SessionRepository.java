package com.paymong.gateway.redis;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
public class SessionRepository {
    private final RedisTemplate<String, Session> sessionRedisTemplate;
    private final RedisTemplate<String, Access> accessRedisTemplate;

    public SessionRepository(
            @Qualifier("redisSessionTemplate") RedisTemplate<String, Session> sessionRedisTemplate,
            @Qualifier("redisAccessTemplate") RedisTemplate<String, Access> accessRedisTemplate
    ){
        this.accessRedisTemplate = accessRedisTemplate;
        this.sessionRedisTemplate = sessionRedisTemplate;
    }

    /* 저장 */
    public void sessionTokenSave(String key, Session session) {
        ValueOperations<String, Session> valueOperations = sessionRedisTemplate.opsForValue();
        valueOperations.set(key, session);
    }

    public void accessTokenSave(String key, Access access, Long expire) {
        ValueOperations<String, Access> valueOperations = accessRedisTemplate.opsForValue();
        valueOperations.set(key, access);
        Boolean isSuccess = accessRedisTemplate.expire(key, expire, TimeUnit.MILLISECONDS);
    }

    /* 조회 */
    public Optional<Session> findSessionTokenById(String key) {
        ValueOperations<String, Session> valueOperations = sessionRedisTemplate.opsForValue();
        return Optional.ofNullable(valueOperations.get(key));
    }
    public Access findAccessTokenById(String key) {
        ValueOperations<String, Access> valueOperations = accessRedisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    /* 삭제 */
    public void sessionTokenDelete(String key) {
        sessionRedisTemplate.expire(key, 0, TimeUnit.MILLISECONDS);
    }
    public void accessTokenDelete(String key) {
        accessRedisTemplate.expire(key, 0, TimeUnit.MILLISECONDS);
    }
}