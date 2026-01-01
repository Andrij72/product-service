# Product Service

**Product Service** is a microservice responsible for managing products in the **MicroServiceGrid** ecosystem.  
It is built with **Spring Boot** and stores data in **MongoDB**.

---

## 🚀 Features
* Public endpoints to view products
* Admin endpoints to create, update, delete, enable/disable products
* Batch operations: create multiple products, delete multiple products
* Fetch all products or a specific product by SKU

---

## 🛠️ Tech Stack
- **Java 21**
- **Spring Boot 3**
- **Spring Data MongoDB**
- **Lombok**
- **Maven/Gradle** (depending on your build tool)
- **Docker** (for containerization)

---

## 📂 Project Structure
    PRODUCT-SERVICE/
    ├── .github/workflows         # CI/CD configurations
    ├── .idea                     # IDE settings
    ├── .mvn/wrapper              # Maven Wrapper
    ├── docker-compose-examples   # docker-compose files for local setup
    ├── src/
    │   ├── main/
    │   │   ├── java/com/akul/microservices/product
    │   │   │   ├── controller   # REST controllers (Public + Admin)
    │   │   │   ├── dto          # Data Transfer Objects
    │   │   │   ├── exception    # custom exceptions
    │   │   │   ├── model        # entities/models
    │   │   │   ├── repository   # MongoDB repositories
    │   │   │   └── service      # business logic
    │   │   └── resources        # application.properties, configurations
    │   └── test/
    │       └── java/com/akul/microservices/product

---

## ⚙️ Running Locally
1️⃣ Clone the repository:
```bash
git clone https://github.com/Andrij72/product-service.git
```
2️⃣ Start MongoDB (local, for tests):
```bash
docker run -d -p 27017:27017 --name mongodb mongo:latest
```
Or use the prepared docker-compose files:

    docker-compose-examples/
    ├── docker-compose.local.yml       # local MongoDB + build from local Dockerfile
    ├── docker-compose.override.yml    # additional local settings for IntelliJ Run
    ├── docker-compose.dev-latest.yml  # development: MongoDB + latest dev image
    └── docker-compose.prod.yml        # production: MongoDB + verified release image
3️⃣ Run the service:
```bash
./mvnw spring-boot:run
````
or
```bash
./gradlew bootRun
```
---
## 📌 REST API Endpoints
Here is used SKU (Stock Keeping Unit) – a unique identifier for each product.
It helps track inventory, sales, and product details easily.

### =====Public API=====
| Method | Endpoint                   | Description                       |
|--------|----------------------------|-----------------------------------|
| GET    | `/api/v1/products/{sku}`   | Get product by SKU                |
| GET    | `/api/v1/products`         | Get paginated list of products    |
### =====Admin API=====
| Method   | Endpoint                  | Description                       |
|----------|---------------------------|-----------------------------------|
| POST     | `/api/v1/admin/products`  | Create a new product              |
| POST     | `/api/v1/admin/products/batch`| Create multiple products in batch |
| PUT      | `/api/v1/admin/products/{sku}`  | Update product by sku             |
| PATCH    | `/api/v1/admin/products/{sku}/disable` | disable product (soft delete)     |
| PATCH    | `/api/v1/admin/products/{sku}/enable`  | Enable previously disabled product|
| DELETE   | `/api/v1/admin/products/batch`| Delete products by list of SKUs (hard delete)|

##### Notes

* ***Public API*** returns only enabled products.
* 
* ***Admin API*** allows full product lifecycle management.
* 
* Batch delete expects a JSON array of SKUs:
```json
["SKU123", "SKU456", "SKU789"]
```
----------
### 📬 Postman Collection

To simplify API testing and avoid duplicating request examples in the documentation,
a ready-to-use Postman collection is provided with the project.

📁 Location in repository

The collection file is available in the root of the project:

```bash
Microservices product-service.postman_collection.json
```

---
## 🛠️ Development Workflow

CI/CD via GitHub Actions:

develop branch → builds dev-latest Docker image

main branch → builds latest Docker image

Tags (e.g., v0.0.1) → build release image

Tests run via Maven/Gradle; optionally connect to MongoDB via ENV variable for integration tests

Docker images use SHA tags for reproducibility

----

## 🧪 Integration Tests

Integration tests are implemented with JUnit 5, RestAssured, and Testcontainers (MongoDB 7.0.5).
They cover:

- ✅ Creating a single product

- ✅ Creating multiple products in batch

- ✅ Retrieving all products

- ✅ Searching products by sku

Database is cleaned before each test run, and a dedicated MongoDB container starts automatically.

---
👨‍💻 Author
Andrij72 — demo project exploring microservice architecture with Spring Boot and MongoDB.

---