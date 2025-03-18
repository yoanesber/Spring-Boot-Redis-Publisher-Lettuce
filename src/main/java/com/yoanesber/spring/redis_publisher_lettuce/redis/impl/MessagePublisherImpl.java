package com.yoanesber.spring.redis_publisher_lettuce.redis.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.yoanesber.spring.redis_publisher_lettuce.redis.MessagePublisher;

@Component
public class MessagePublisherImpl implements MessagePublisher {
    
    private final RedisTemplate<String, Object> redisTemplate;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public MessagePublisherImpl(RedisTemplate<String, Object> redisTemplate) {
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
