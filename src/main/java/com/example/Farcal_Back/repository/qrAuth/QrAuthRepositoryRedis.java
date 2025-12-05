package com.example.Farcal_Back.repository.qrAuth;

import com.example.Farcal_Back.model.qrAuth.QrAuthToken;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class QrAuthRepositoryRedis {

    private final RedisTemplate<String, Object> redis;

    private String key(String id) {
        return "qr:" + id;
    }

    public void save(QrAuthToken token, long ttlSeconds) {
        redis.opsForValue().set(key(token.getId()), token, ttlSeconds, TimeUnit.SECONDS);
    }

    public QrAuthToken find(String id) {
        return (QrAuthToken) redis.opsForValue().get(key(id));
    }
}


