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
```
product-service/
├── src/main/java/com/andrij72/product
│ ├── controller # REST controllers
│ ├── model # Data models
│ ├── repository # MongoDB repositories
│ └── service # Business logic
└── src/main/resources
└── application.properties
```
---

## ⚙️ Running Locally

### 1️⃣ Clone the repository
```bash
git clone https://github.com/Andrij72/product-service.git
```

2️⃣ Start dependencies

 - **MongoDB** (standalone for tests):
```bash
docker run -d -p 27017:27017 --name mongodb mongo:latest
```
 - **Kafka + Zookeeper** (for event-driven features):
```bash
docker-compose -f docker-compose/kafka.yml up -d
```

- Or use **docker-compose examples** provided:

`````
docker-compose-examples/
  ├── mongo-only.yml           # MongoDB standalone
  ├── build-local.yml          # MongoDB + build product-service locally
  ├── dev-latest.yml           # MongoDB + dev-latest Docker image from Hub
  └── release.yml              # MongoDB + release Docker image from Hub
`````

3️⃣ Run the service
``` bash
./mvnw spring-boot:run
``` 
or
``` bash
./gradlew bootRun
``` 

``` bash
./gradlew bootRun
```
or
``` bash
./gradlew bootRun
````
.
_______

## 📌 REST API Endpoints

| Method | Endpoint             | Description                |
| ------ | -------------------- | -------------------------- |
| POST   | `/api/products`      | Create a new product       |
| GET    | `/api/products`      | Get all products           |
| GET    | `/api/products/{id}` | Get a product by ID        |
| PUT    | `/api/products/{id}` | Update an existing product |
| DELETE | `/api/products/{id}` | Delete a product           |

_______

## 🛠️ Development Workflow


CI/CD via GitHub Actions:

- develop branch → builds dev-latest Docker image

- main branch → builds latest Docker image

- Tags (e.g., v0.0.2) → build release image

- Tests run via Maven/Gradle; optionally connect to MongoDB via ENV variable for integration tests.

- Docker images use SHA tags for reproducibility.
_______

## 📌 Roadmap

- Add request validation (Spring Validation)

- Add tests (JUnit + Testcontainers)

- Integrate with Order Service using the SAGA pattern

- Service monitoring with Prometheus + Grafana
_______

## 👨‍💻 Author

Andrij72 — demo project exploring microservice
architecture with Spring Boot, Kafka, and Kubernetes.
