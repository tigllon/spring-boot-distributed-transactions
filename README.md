# spring-boot-distributed-transactions
This repo contains code of the micro-services performing distributed transaction by leveraging Kafka's transactional capabilities.

## Miroservice Architecture
![Architecture](https://github.com/user-attachments/assets/5bbfd1fc-63a6-43d8-80c6-c8aae0788c8f)


## Usage
First of all we need to start the zookeeper service followed by the kafka server service. Follow the step 1 & 2 in the [documentation](https://kafka.apache.org/quickstart) in order to run these services.

Now we need to start the microservices present in our repo i.e payment-ms, order-ms & stock-ms respectively.
Once they get stated , now we can hit the payment microservice's controller with /api/orders

![hitting order-ms's controller with postman at /api/orders](https://github.com/user-attachments/assets/b19b0918-e604-4f23-8fcd-407aa1cb8cfc)


As soon as we hit, the payment-ms pushes events to the apache kafka with whom the other microservices connects and thus the distributed transactions take place.
