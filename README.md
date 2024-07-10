# spring-boot-distributed-transactions
This repo contains code of the micro-services performing distributed transaction by leveraging Kafka's transactional capabilities.

## Miroservice Architecture
![Architecture](https://github.com/tigllon/spring-boot-distributed-transactions/assets/56904319/ab91c8dc-ec18-49b8-931e-5876d4d609af)

## Usage
First of all we need to start the zookeeper service followed by the kafka server service. Follow the step 1 & 2 in the [documentation](https://kafka.apache.org/quickstart) in order to run these services.

Now we need to start the microservices present in our repo i.e payment-ms, order-ms & stock-ms respectively.
Once they get stated , now we can hit the payment microservice's controller with /api/orders

![hitting payment-ms's controller with postman at /api/orders](https://github.com/tigllon/spring-boot-distributed-transactions/assets/56904319/7a88ed77-8ab4-4133-849f-f8a03d349f75)

As soon as we hit, the payment-ms pushes events to the apache kafka with whom the other microservices connects and thus the distributed transactions take place.
