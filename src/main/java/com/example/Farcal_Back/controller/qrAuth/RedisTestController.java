package com.example.Farcal_Back.controller.qrAuth;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/redis")
public class RedisTestController {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisTestController(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> testRedis() {
        try {
            // Test de connexion simple
            String testKey = "test:connection";
            redisTemplate.opsForValue().set(testKey, "connected");
            String value = (String) redisTemplate.opsForValue().get(testKey);
            redisTemplate.delete(testKey);

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Redis connection successful",
                    "testValue", value
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "status", "error",
                    "message", "Redis connection failed",
                    "error", e.getMessage()
            ));
        }
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> redisInfo() {
        try {
            String ping = redisTemplate.getConnectionFactory().getConnection().ping();
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "ping", ping,
                    "host", redisTemplate.getConnectionFactory().getConnection().getNativeConnection().toString()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }
}