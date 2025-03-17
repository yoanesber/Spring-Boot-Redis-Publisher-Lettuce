package com.yoanesber.spring.redis_publisher_lettuce.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;


@Component
public class RedisPublisher {
    
    private final RedisTemplate<String, Object> redisTemplate;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public RedisPublisher(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void publishMessage(String channel, Object message) {
        Assert.hasText(channel, "Channel must not be empty");
        Assert.notNull(message, "Message must not be null");

        try {
            redisTemplate.convertAndSend(channel, message);
            logger.info("Published message to channel: {} with message: {}", channel, message);
        } catch (Exception e) {
            logger.error("Error publishing message to channel: {}", channel, e);
            throw new RuntimeException("Error publishing message to channel: " + channel, e);
        }
    }
}
