# Product Service

**Product Service** is a microservice responsible for managing products in the **MicroServiceGrid** ecosystem.  
It is built with **Spring Boot**, stores data in **MongoDB**, and integrates with other services via **Kafka** (for asynchronous communication) and REST APIs.

---

## 🚀 Features
- Create new products
- Update and delete existing products
- Fetch all products or a specific product by ID
- Publishes domain events (e.g., product created/updated) to **Kafka**

---

## 🛠️ Tech Stack
- **Java 17**
- **Spring Boot 3**
- **Spring Data MongoDB**
- **Apache Kafka**
- **Lombok**
- **Maven/Gradle** (depending on your build tool)
- **Docker** (for containerization)

---

## 📂 Project Structure
       
       product-service/
      ├── src/main/java/com/andrij72/product
      │ ├── controller # REST-контролери
      │ ├── model # Моделі даних
      │ ├── repository # Репозиторії MongoDB
      │ └── service # Бізнес-логіка
      └── src/main/resources
      └── application.

---

## ⚙️ Running Locally
1. Clone the repository:
   ```bash
   git clone https://github.com/Andrij72/product-service.git
   cd product-service


---

## ⚙️ Running Locally
1. Clone the repository:
   ```bash
   git clone https://github.com/Andrij72/product-service.git
   cd product-service
Start MongoDB (e.g., with Docker):

    docker run -d -p 27017:27017 --name mongodb mongo:latest
Start Kafka (via Docker Compose or Confluent Platform).

Run the service:

    ./mvnw spring-boot:run
or

    ./gradlew bootRun

| Method | Endpoint             | Description                |
| ------ | -------------------- | -------------------------- |
| POST   | `/api/products`      | Create a new product       |
| GET    | `/api/products`      | Get all products           |
| GET    | `/api/products/{id}` | Get a product by ID        |
| PUT    | `/api/products/{id}` | Update an existing product |
| DELETE | `/api/products/{id}` | Delete a product           |


## 📌 Roadmap
Add request validation (Spring Validation)

Add tests (JUnit + Testcontainers)

Integrate with Order Service using the SAGA pattern

Service monitoring with Prometheus + Grafana

## 👨‍💻 Author

Andrij72 — demo project exploring microservice 
architecture with Spring Boot, Kafka and Kubernetes.
