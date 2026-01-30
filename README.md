# Product Service

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3-brightgreen) ![Java](https://img.shields.io/badge/Java-21-blue) ![MongoDB](https://img.shields.io/badge/MongoDB-7.0.5-success) ![Docker](https://img.shields.io/badge/Docker-ready-lightgrey)

**Product Service** is a microservice responsible for managing products in the **MicroServiceGrid** ecosystem.  
Built with **Spring Boot 3** and **MongoDB**, it provides public and admin endpoints with pagination, sorting, and batch operations.

---

## 🚀 Features

- Public API to fetch **enabled products only**
- Admin API to **create, update, delete, enable/disable products**
- Batch operations for **create/delete**
- Pagination and sorting support
- Soft delete via **enable/disable**
- Separate endpoint for **product image upload**

---

## 🛠️ Tech Stack

- **Java 21**
- **Spring Boot 3**
- **Spring Data MongoDB**
- **Lombok**
- **Maven/Gradle**
- **Docker** for containerization

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

**SKU (Stock Keeping Unit)** – unique identifier for each product.  
Used to track inventory, sales, and product details.

---

### ===== Public API =====

| Method | Endpoint                 | Description                        |
|--------|--------------------------|------------------------------------|
| GET    | `/api/v1/products/{sku}` | Get product by SKU                 |
| GET    | `/api/v1/products`       | Get paginated list of enabled products |

**Query Parameters for GET `/api/v1/products`:**

- `page` – page number (optional, default = 0)
- `size` – page size (optional, default = 12)

**Sorting:** fixed by `sku`, `name`, `price`, `createdAt` ascending

---

### ===== Admin API =====

| Method  | Endpoint                                     | Description                                         |
|---------|----------------------------------------------|-----------------------------------------------------|
| POST    | `/api/v1/admin/products`                     | Create a new product (JSON, no file)               |
| POST    | `/api/v1/admin/products/batch`               | Create multiple products in batch (JSON array, **no file upload**) |
| PUT     | `/api/v1/admin/products/{sku}`               | Update product by SKU (JSON, no file)             |
| PUT     | `/api/v1/admin/products/{sku}/image`         | Add/Update product image (multipart/form-data)    |
| PATCH   | `/api/v1/admin/products/{sku}/disable`       | Disable product (soft delete)                     |
| PATCH   | `/api/v1/admin/products/{sku}/enable`        | Enable previously disabled product                |
| DELETE  | `/api/v1/admin/products/batch`               | Delete products by list of SKUs (hard delete)     |

---

### 🔹 Notes

- **Public API** returns only enabled products
- **Admin API** allows full product lifecycle management
- **Batch delete** expects a JSON array of SKUs:

```json
["SKU123", "SKU456", "SKU789"]
```
Batch create expects a JSON array of ProductRequest objects (no file upload allowed)

Image upload uses a dedicated endpoint:

```
PUT /api/v1/admin/products/{sku}/image

PATCH /enable and /disable implement soft delete/restore
```
📬 Postman Collection

Ready-to-use Postman collection to simplify API testing:

**Location in repository:** root folder
```
Microservices product-service.postman_collection.json
```
---
## 🛠️ Development Workflow

* CI/CD via GitHub Actions
* develop → builds dev-latest Docker image
* main → builds latest Docker image
* Tests via Maven/Gradle; optional MongoDB ENV
* Docker images use SHA tags
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
## 👨‍💻 Author

*Andrij Kulynych* — demo project exploring microservice architecture with Spring Boot and MongoDB.  

#### 📅  Version: 2.0
