# saga-microservices-orchestrator

A reference implementation of the **Saga Pattern** using **Netflix/Orkes Conductor**, **Spring Boot**, and **Kafka** to orchestrate distributed transactions across multiple microservices.

This project demonstrates how to manage **distributed transactions** via **Orchestration**, where each microservice performs a local transaction and communicates state changes through **events**. If any step fails, compensating transactions roll back previously successful steps, ensuring overall data consistency.

## Features

- **Orchestration-Based Saga**: A centralized **Orchestrator** (using Netflix/Orkes Conductor) controls the saga flow across microservices.  
- **Microservices**: Separate services for **Order**, **Inventory**, **Payment**, **Shipping**, **Notification**, etc.  
- **Kafka Messaging**: Asynchronous event-driven communication via Kafka.  
- **CQRS**: A separate **Query Service** listens to events and maintains a read-optimized view.  
- **Local Transactions & Compensations**: Each service uses a local ACID transaction; compensating commands are sent on failure.

## Project Structure
```
saga-microservices-orchestrator
├── common
│   └── src/main/java/com/distributedtransactions/common/dto   # Shared DTOs and event classes
├── orchestrator
│   └── src/main/java/com/distributedtransactions/orchestrator  # Orchestration logic using Conductor
├── order-service
│   └── src/main/java/com/distributedtransactions/orderservice  # Handles order creation and cancellation
├── inventory-service
│   └── src/main/java/com/distributedtransactions/inventoryservice  # Manages inventory reservations/releases
├── payment-service
│   └── src/main/java/com/distributedtransactions/paymentservice    # Processes payments and rollbacks
├── shipping-service
│   └── src/main/java/com/distributedtransactions/shippingservice   # Ships orders and handles failures
├── notification-service
│   └── src/main/java/com/distributedtransactions/notificationservice # Sends success/failure notifications
├── query-service
│   └── src/main/java/com/distributedtransactions/queryservice      # Maintains CQRS read model
└── docker-compose.yml                                              # Spins up Kafka, Conductor, etc.
```
## Getting Started

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/felixojiambo/saga-microservices-orchestrator.git
   cd saga-microservices-orchestrator
   ```

2. **Set Up Infrastructure**:
   - **Kafka** and **Orkes Conductor** can be run via Docker Compose:
     ```bash
     docker-compose up -d
     ```
   - Verify Kafka is accessible at `localhost:9092` and Conductor at `http://localhost:8080`.

3. **Configure Conductor Credentials** (Optional):
   - If using **Orkes Conductor Cloud**, update `application.properties` in the Orchestrator module with your **Access Key** and **Secret**:
     ```properties
     conductor.server.url=https://<orkes-conductor-cloud-url>/api
     conductor.security.client.key-id=<YOUR_KEY>
     conductor.security.client.secret=<YOUR_SECRET>
     ```
   - If using OSS Conductor locally, ensure the server is up on port 8080.

4. **Build & Run**:
   ```bash
   mvn clean install
   # Start each service in its own terminal
   mvn spring-boot:run -f orchestrator/pom.xml
   mvn spring-boot:run -f order-service/pom.xml
   mvn spring-boot:run -f inventory-service/pom.xml
   # ... etc. for each service
   ```

5. **Test the Saga Flow**:
   - Send a `CREATE_ORDER` command to the **Order Service** (via a REST endpoint or Kafka producer).
   - Observe events in each service’s logs.
   - Confirm compensating actions occur on failures (e.g., payment refusal triggers `RELEASE_INVENTORY`, `CANCEL_ORDER`).

## Usage & Architecture

- **Saga Orchestrator**: Coordinates the entire flow, listening to domain events (e.g., `ORDER_CREATED`, `INVENTORY_RESERVED`, etc.) and issuing next-step commands (e.g., `PROCESS_PAYMENT`).
- **Local Transactions**: Each microservice has its own DB and transaction boundaries.
- **Compensation**: If a microservice step fails, the Orchestrator instructs other microservices to roll back prior successful actions.

## License

This project is licensed under the [MIT License](LICENSE), granting permission to use, modify, and distribute under typical open-source terms.

## Contributing

1. **Fork** the repository.  
2. **Create** a new feature branch (`git checkout -b feature/my-new-feature`).  
3. **Commit** your changes (`git commit -m 'Add some feature'`).  
4. **Push** to the branch (`git push origin feature/my-new-feature`).  
5. **Open** a Pull Request.

## Contact

- **Author**: Ojiambo

Feel free to open issues or submit pull requests for improvements and bug fixes.

---

Enjoy building your **orchestrated Saga microservices** with **Netflix/Orkes Conductor** and **Kafka**!
