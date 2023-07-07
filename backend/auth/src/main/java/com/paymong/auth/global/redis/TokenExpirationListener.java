package com.paymong.auth.global.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TokenExpirationListener implements MessageListener {
    @Override
    public void onMessage(Message message, byte[] pattern) {
        String token = new String(message.getBody());
        log.info("TokenExpirationListener : Token delete Success : {}", token);
    }
}
