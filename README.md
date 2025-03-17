# Order Payment Service with Redis

## 🚀 Overview
This project implements an **Order Payment Service** that integrates with `Redis` to publish payment status messages. It leverages `Spring Boot`, `Redis with Lettuce`, and a `structured event-driven approach` to notify other services about successful or failed payment transactions.

---

## 📌 What is Lettuce?

`Lettuce` is a **scalable**, **high-performance**, and **non-blocking** Redis client for Java. It is widely used in Spring Boot applications for **caching**, **message queuing**, and **distributed data processing**. It is also a **reactive and thread-safe** Redis client built on `Netty`. It supports both **synchronous and asynchronous (reactive)** interactions with Redis.

### 🔥 Key Features of Lettuce
- ✅ Supports Asynchronous & Reactive APIs (using CompletableFuture, RxJava, and Project Reactor)
- ✅ Thread-Safe Connections (multiple threads can use a single connection)
- ✅ Automatic Reconnection & Cluster Support
- ✅ Built on Netty (High Performance, Non-Blocking IO)
- ✅ Supports Redis Pub/Sub & Redis Streams

This project uses `LettuceConnectionFactory` to configure Redis connections with shared **ClientResources**. This connection factory is used by `RedisTemplate` to interact with Redis, enabling efficient message publishing and retrieval. The `RedisPublisher` service is responsible for publishing messages to the appropriate Redis channels (**PAYMENT_SUCCESS** and **PAYMENT_FAILED**).

---

## Order Payment Service

The **Order Payment Service** provides **REST APIs** to create and process payment transactions. The main flow involves:
1. Validating the Request - Ensuring the order exists, amount is valid, etc.
2. Calling Payment Gateway - Fetching transaction details (e.g., transactionId, paymentStatus).
3. Persisting Order Payment - Storing payment transaction details in the database.
4. Publishing Payment Status:
    - If paymentStatus = SUCCESS, publish to PAYMENT_SUCCESS channel.
    - If an exception occurs during processing, publish to PAYMENT_FAILED channel with an error message.

The `processPayment` method determines the flow based on paymentMethod (**CREDIT_CARD**, **PAYPAL**, **BANK_TRANSFER**), handling specific conditions for each type.

---

## ✨Tech Stack
The technology used in this project are:
- `Spring Boot Starter Web` – Provides essential components for building RESTful APIs.
- `Redis with Lettuce` – Message publishing
---

## 📋 Project Structure
The project is organized into the following package structure:
```bash
order-payment-service/
│── src/main/java/com/yoanesber/spring/redis_publisher_lettuce/
│   ├── config/                # Configuration classes for Redis
│   ├── controller/            # REST controllers handling API requests
│   ├── dto/                   # Data Transfer Objects for requests and responses
│   ├── entity/                # Entity classes representing Order Payment data
│   ├── service/               # Business logic layer
│   │   ├── impl/              # Implementation of services
│   ├── redis/                 # Redis-related classes (RedisPublisher)
```
---

## 📂 Environment Configuration
Configuration values are stored in `.env.development` and referenced in `application.properties`.

Example `.env.development` file content:
```properties
# application
APP_PORT=8081
SPRING_PROFILES_ACTIVE=development

#redis
REDIS_HOST=localhost
REDIS_PORT=6379
REDIS_USERNAME=default
REDIS_PASSWORD=mypassword
REDIS_TIMEOUT=5
REDIS_CONNECT_TIMEOUT=3
REDIS_LETTUCE_SHUTDOWN_TIMEOUT=10
```

Example `application.properties` file content:
```properties
# application
spring.application.name=redis-publisher-lettuce
server.port=${APP_PORT}
spring.profiles.active=${SPRING_PROFILES_ACTIVE}

# redis
spring.data.redis.host=${REDIS_HOST}
spring.data.redis.port=${REDIS_PORT}
spring.data.redis.username=${REDIS_USERNAME}
spring.data.redis.password=${REDIS_PASSWORD}
spring.data.redis.timeout=${REDIS_TIMEOUT}
spring.data.redis.connect-timeout=${REDIS_CONNECT_TIMEOUT}
spring.data.redis.lettuce.shutdown-timeout=${REDIS_LETTUCE_SHUTDOWN_TIMEOUT}
```
---

## 🛠 Installation & Setup
A step by step series of examples that tell you how to get a development env running.
1. Clone the repository
```bash
git clone https://github.com/yoanesber/Spring-Boot-Redis-Lettuce.git
```

2. Ensure Redis is installed and running:
```bash
redis-server
```

3. Set up Redis user and password in `.env.development`
```bash
redis-server
```

4. (Optional) If you want to add a specific user with access to a specific channel, you can run the following command in Redis CLI:
```bash
ACL SETUSER your_user +CHANNEL~your_channel on >your_password
```

5. Build and run the application
```bash
mvn spring-boot:run
```

6. Use API endpoints to test payment processing.

---

## 🔗 API Endpoints
### Order Service
`POST http://localhost:8081/api/v1/order-payment` - Create a new order payment and trigger payment processing

**Body Request - CREDIT_CARD**
```json
{
    "orderId":"ORD123456789",
    "amount":"199.99",
    "currency":"USD",
    "paymentMethod":"CREDIT_CARD",
    "cardNumber":"1234 5678 9012 3456",
    "cardExpiry":"31/12",
    "cardCvv":"123"
}
```

**Body Response - CREDIT_CARD**
```json
{
    "statusCode": 201,
    "timestamp": "2025-03-17T08:28:53.539884900Z",
    "message": "Order payment created successfully",
    "data": {
        "orderId": "ORD123456789",
        "transactionId": "TXN1742200133539",
        "paymentStatus": "SUCCESS",
        "amount": 199.99,
        "currency": "USD",
        "paymentMethod": "CREDIT_CARD",
        "createdAt": "2025-03-17T08:28:53.539884900Z"
    }
}
```

**Body Request - PAYPAL**
```json
{
    "orderId":"ORD123456789",
    "amount":"199.99",
    "currency":"USD",
    "paymentMethod":"PAYPAL",
    "paypalEmail":"my@email.com"
}
```

**Body Response - PAYPAL**
```json
{
    "statusCode": 201,
    "timestamp": "2025-03-17T08:28:39.010745500Z",
    "message": "Order payment created successfully",
    "data": {
        "orderId": "ORD123456789",
        "transactionId": "TXN1742200119003",
        "paymentStatus": "SUCCESS",
        "amount": 199.99,
        "currency": "USD",
        "paymentMethod": "PAYPAL",
        "createdAt": "2025-03-17T08:28:39.003556100Z"
    }
}
```

**Body Request - BANK_TRANSFER**
```json
{
    "orderId":"ORD123456789",
    "amount":"199.99",
    "currency":"USD",
    "paymentMethod":"BANK_TRANSFER",
    "bankAccount":"1234567890",
    "bankName":"Bank of Indonesia"
}
```

**Body Response - BANK_TRANSFER**
```json
{
    "statusCode": 201,
    "timestamp": "2025-03-17T08:28:29.806174300Z",
    "message": "Order payment created successfully",
    "data": {
        "orderId": "ORD123456789",
        "transactionId": "TXN1742200108505",
        "paymentStatus": "SUCCESS",
        "amount": 199.99,
        "currency": "USD",
        "paymentMethod": "BANK_TRANSFER",
        "createdAt": "2025-03-17T08:28:28.505517700Z"
    }
}
```

**Invalid Request**
```json
{
    "orderId":"ORD123456789",
    "amount":"199.99",
    "currency":"USD",
    "paymentMethod":"CREDITCARD",
    "cardNumber":"1234 5678 9012 3456",
    "cardExpiry":"31/12",
    "cardCvv":"123"
}
```

**Response**
```json
{
    "statusCode": 500,
    "timestamp": "2025-03-17T09:55:11.906453600Z",
    "message": "Invalid payment method: CREDITCARD",
    "data": null
}
```