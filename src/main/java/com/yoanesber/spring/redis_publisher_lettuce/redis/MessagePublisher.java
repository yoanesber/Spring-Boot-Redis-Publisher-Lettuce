package com.yoanesber.spring.redis_publisher_lettuce.redis;

public interface MessagePublisher {
    // Publish a message to a channel
    void publishMessage(String channel, Object message);
}
