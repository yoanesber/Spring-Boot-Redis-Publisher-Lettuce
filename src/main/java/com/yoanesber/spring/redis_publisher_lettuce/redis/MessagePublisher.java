package com.yoanesber.spring.redis_publisher_lettuce.redis;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.yoanesber.spring.redis_publisher_lettuce.redis.MessagePublisher;

@Component
public class MessagePublisher {
    
    private final RedisTemplate<String, Object> redisTemplate;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public MessagePublisher(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Publishes a message to a specified Redis channel.
     *
     * @param channel the Redis channel to publish the message to
     * @param message the message to be published
     */
    public void publish(String channel, Object message) {
        Assert.hasText(channel, "Channel must not be empty");
        Assert.notNull(message, "Message must not be null");

        try {
            Map<String, Object> data = new HashMap<>();
            data.put("event", channel);
            data.put("message", message);

            redisTemplate.convertAndSend(channel, data);
            logger.info("Published message to channel: {} with message: {} and data: {}", channel, message, data);
        } catch (Exception e) {
            logger.error("Error publishing message to channel: {}", channel, e);
            throw new RuntimeException("Error publishing message to channel: " + channel, e);
        }
    }
}
