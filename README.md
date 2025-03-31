# Order Payment Service with Redis

## 📖 Overview
This project implements an **Order Payment Service** that integrates with **Redis** to **publish payment status messages**. It leverages **Spring Boot**, **Redis with Lettuce**, and a **structured event-driven approach** to notify other services about successful or failed payment transactions.  

### 🗂️ What is Redis?
**Redis (Remote Dictionary Server)** is an **in-memory, key-value data store known for its speed, flexibility, and scalability**. It is widely used for **caching, real-time messaging, session management, leaderboards, and job queuing**. It is often referred to as a **data structures server**. What this means is that Redis provides access to mutable data structures via a set of commands, which are sent using a server-client model with TCP sockets and a simple protocol. So different processes can query and modify the same data structures in a shared way.  

#### 🚀 Key Features of Redis
- **Blazing Fast** – Data is stored in memory, making reads/writes extremely fast.
- **Supports Data Structures** – Strings, Lists, Sets, Hashes, Sorted Sets, Streams, Pub/Sub, etc.
- **Persistence** – Data can be persisted to disk using RDB (snapshotting) or AOF (append-only file).
- **Scalability** – Supports clustering and replication for high availability.
- **Pub/Sub Messaging** – Used for real-time notifications and distributed systems.

#### 💼 Common Business Use Cases for Redis Message Types
Redis **Publish/Subscribe (Pub/Sub)** is often used in business applications for **real-time event-driven messaging**. Here are some common message types:  

1. Order Processing System (E-commerce)  
Message Types:  
    - ORDER_PLACED – When a customer places an order.
    - PAYMENT_SUCCESS – Payment is successful.
    - PAYMENT_FAILED – Payment is declined.
    - ORDER_SHIPPED – Order has been shipped.
    - ORDER_DELIVERED – Order is delivered to the customer.

    **Use Case**: Notify users in real-time about their order status.  

2. Payment Processing & Fraud Detection (Fintech)  
Message Types:  
    - PAYMENT_INITIATED – When a user initiates a payment.
    - PAYMENT_AUTHORIZED – When payment is authorized.
    - PAYMENT_REJECTED – Payment failed due to insufficient funds or fraud detection.
    - PAYMENT_REFUNDED – When a refund is issued.
    - FRAUD_ALERT – When fraud is detected.

    **Use Case**:  
    - Notify customers of payment transactions.
    - Alert fraud detection systems to take action.

3. Notification & Alerts System  
Message Types:  
    - NEW_MESSAGE – A user receives a new message in a chat app.
    - NEW_COMMENT – Someone comments on a post.
    - FRIEND_REQUEST – A new friend request is received.
    - PROMOTIONAL_OFFER – A business sends discounts to customers.
    - SYSTEM_ALERT – A critical error occurs (e.g., server downtime alert).

    **Use Case**:  
    - Real-time user notifications for chat apps like WhatsApp or Slack.
    - Push notifications for social media updates.


4. Real-Time Stock Market & Trading Platforms (Finance)  
Message Types:  
    - STOCK_PRICE_UPDATE – Stock price updates every second.
    - TRADE_EXECUTED – A stock trade is completed.
    - MARKET_NEWS_UPDATE – News impacting the stock market.
    - PORTFOLIO_ALERT – Portfolio value changes significantly.

    **Use Case**: Stock trading apps like Robinhood use Redis for real-time price updates.  

5. IoT & Real-Time Sensor Data (Smart Devices)  
Message Types:  
    - TEMPERATURE_UPDATE – Sends real-time temperature readings.
    - MOTION_DETECTED – Security camera detects movement.
    - DEVICE_HEALTH_CHECK – IoT device reports its health.

    **Use Case**: Smart home automation, monitoring energy consumption, and security alerts.  


### 🥬 What is Lettuce?

**Lettuce** is a **scalable**, **high-performance**, and **non-blocking** Redis client for Java. It is also a **reactive and thread-safe** Redis client built on `Netty`. It supports both **synchronous and asynchronous (reactive)** interactions with Redis.  

#### 🚀 Key Features of Lettuce
- Supports Asynchronous & Reactive APIs (using CompletableFuture, RxJava, and Project Reactor).
- Thread-Safe Connections (multiple threads can use a single connection).
- Automatic Reconnection & Cluster Support.
- Built on Netty (High Performance, Non-Blocking IO).
- Supports Redis Pub/Sub & Redis Streams.

This project uses `LettuceConnectionFactory` to configure Redis connections with shared **ClientResources**. This connection factory is used by `RedisTemplate` to interact with Redis, enabling efficient message publishing and retrieval. The `RedisPublisher` service is responsible for publishing messages to the appropriate Redis channels (**PAYMENT_SUCCESS** and **PAYMENT_FAILED**).  


### 🛒 Order Payment Service

The **Order Payment Service** provides **REST APIs** to create and process payment transactions. The main flow involves:
1. Validating the Request – Ensuring the order exists, amount is valid, etc.
2. Calling Payment Gateway – Fetching transaction details (e.g., transactionId, paymentStatus).
3. Persisting Order Payment – Storing payment transaction details in the database.
4. Publishing Payment Status:
    - If `paymentStatus = SUCCESS`, publish to `PAYMENT_SUCCESS` channel.
    - If an exception occurs during processing, publish to `PAYMENT_FAILED` channel with an error message.

The `processPayment` method determines the flow based on paymentMethod (**CREDIT_CARD**, **PAYPAL**, **BANK_TRANSFER**), handling specific conditions for each type.

---

## 🤖 Tech Stack
The technology used in this project are:  
- `Spring Boot Starter Web` – Provides essential components for building RESTful APIs.
- `Redis with Lettuce` – Message publishing
---

## 🏗️ Project Structure
The project is organized into the following package structure:  
```bash
redis-publisher-lettuce/
│── src/main/java/com/yoanesber/spring/redis_publisher_lettuce/
│   ├── 📂config/                # Configuration classes for Redis
│   ├── 📂controller/            # REST controllers handling API requests
│   ├── 📂dto/                   # Data Transfer Objects for requests and responses
│   ├── 📂entity/                # Entity classes representing Order Payment data
│   ├── 📂service/               # Business logic layer
│   │   ├── 📂impl/              # Implementation of services
│   ├── 📂redis/                 # Redis-related classes (RedisPublisher)
```
---

## ⚙ Environment Configuration
Configuration values are stored in `.env.development` and referenced in `application.properties`.  
Example `.env.development` file content:  
```properties
# Application properties
APP_PORT=8081
SPRING_PROFILES_ACTIVE=development

# Redis properties
REDIS_HOST=localhost
REDIS_PORT=6379
REDIS_USERNAME=default
REDIS_PASSWORD=your_password
REDIS_TIMEOUT=5
REDIS_CONNECT_TIMEOUT=3
REDIS_LETTUCE_SHUTDOWN_TIMEOUT=10
```

Example `application.properties` file content:  
```properties
# Application properties
spring.application.name=redis-publisher-lettuce
server.port=${APP_PORT}
spring.profiles.active=${SPRING_PROFILES_ACTIVE}

# Redis properties
spring.data.redis.host=${REDIS_HOST}
spring.data.redis.port=${REDIS_PORT}
spring.data.redis.username=${REDIS_USERNAME}
spring.data.redis.password=${REDIS_PASSWORD}
spring.data.redis.timeout=${REDIS_TIMEOUT}
spring.data.redis.connect-timeout=${REDIS_CONNECT_TIMEOUT}
spring.data.redis.lettuce.shutdown-timeout=${REDIS_LETTUCE_SHUTDOWN_TIMEOUT}
```
---

## 🛠️ Installation & Setup
A step by step series of examples that tell you how to get a development env running.  
1. Clone the repository
```bash
git clone https://github.com/yoanesber/Spring-Boot-Redis-Publisher-Lettuce.git
cd Spring-Boot-Redis-Publisher-Lettuce
```

2. Ensure Redis is installed and running:
```bash
redis-server
```

3. (Optional) If you want to add a specific user with access to a specific channel, you can run the following command in Redis CLI:
```bash
ACL SETUSER your_user +CHANNEL~your_channel on >your_password
```

4. Set up Redis password in `.env.development` file:
```properties
# Redis properties
REDIS_PASSWORD=your_password
```

5. Build and run the application
```bash
mvn spring-boot:run
```

6. Use API endpoints to test payment processing.

---

## 🌐 API Endpoints
### Order Service
`POST http://localhost:8081/api/v1/order-payment` - Create a new order payment and trigger payment processing.  

**Body Request (CREDIT_CARD):**
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

**Successful Response (CREDIT_CARD):**
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

**Body Request (PAYPAL):**
```json
{
    "orderId":"ORD123456789",
    "amount":"199.99",
    "currency":"USD",
    "paymentMethod":"PAYPAL",
    "paypalEmail":"my@email.com"
}
```

**Successful Response (PAYPAL):**
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

**Body Request (BANK_TRANSFER):**
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

**Successful Response (BANK_TRANSFER):**
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

**Invalid Request:**
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

In the JSON request above, we use `"paymentMethod":"CREDITCARD"`, which should be `"paymentMethod":"CREDIT_CARD"`, to test an invalid request.  
This ensures the API correctly validates input and returns an appropriate error response for unsupported or incorrectly formatted values.  

**Invalid Response:**
```json
{
    "statusCode": 500,
    "timestamp": "2025-03-17T09:55:11.906453600Z",
    "message": "Invalid payment method: CREDITCARD",
    "data": null
}
```

## 🔗 Related Repositories
For the Redis Subscriber implementation, check out [Spring Boot Redis Subscriber with Lettuce](https://github.com/yoanesber/Spring-Boot-Redis-Subscriber-Lettuce).