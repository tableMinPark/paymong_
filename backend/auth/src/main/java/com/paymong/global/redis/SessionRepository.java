package com.paymong.global.redis;

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
    private final RedisTemplate<String, Refresh> refreshRedisTemplate;

    public SessionRepository(
            @Qualifier("redisSessionTemplate") RedisTemplate<String, Session> sessionRedisTemplate,
            @Qualifier("redisAccessTemplate") RedisTemplate<String, Access> accessRedisTemplate,
            @Qualifier("redisRefreshTemplate") RedisTemplate<String, Refresh> refreshRedisTemplate
    ){
        this.accessRedisTemplate = accessRedisTemplate;
        this.refreshRedisTemplate = refreshRedisTemplate;
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

    public void refreshTokenSave(String key, Refresh refresh, Long expire) {
        ValueOperations<String, Refresh> valueOperations = refreshRedisTemplate.opsForValue();
        valueOperations.set(key, refresh);
        refreshRedisTemplate.expire(key, expire, TimeUnit.MILLISECONDS);
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
    public Refresh findRefreshTokenById(String key) {
        ValueOperations<String, Refresh> valueOperations = refreshRedisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    /* 삭제 */
    public void sessionTokenDelete(String key) {
        sessionRedisTemplate.expire(key, 0, TimeUnit.MILLISECONDS);
    }
    public void accessTokenDelete(String key) {
        accessRedisTemplate.expire(key, 0, TimeUnit.MILLISECONDS);
    }
    public void refreshTokenDelete(String key) {
        refreshRedisTemplate.expire(key, 0, TimeUnit.MILLISECONDS);
    }
}