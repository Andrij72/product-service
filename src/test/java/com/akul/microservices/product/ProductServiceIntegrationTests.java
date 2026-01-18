package com.akul.microservices.product;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

import static io.restassured.RestAssured.given;

@Import(org.testcontainers.utility.TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductServiceIntegrationTests {

    static MongoDBContainer mongoDBContainer =
            new MongoDBContainer(DockerImageName.parse("mongo:7.0.5"));

    @LocalServerPort
    private Integer port;

    static {
        mongoDBContainer.start();
        System.setProperty(
                "spring.data.mongodb.uri",
                mongoDBContainer.getReplicaSetUrl()
        );
    }

    @BeforeEach
    void setup() {
        try (MongoClient mongoClient =
                     MongoClients.create(mongoDBContainer.getReplicaSetUrl())) {
            mongoClient.getDatabase("test").drop();
        }
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @AfterAll
    static void shutdown() {
        mongoDBContainer.stop();
    }

    // ================= ADMIN =================

    @Test
    void shouldCreateProduct() {
        given()
                .contentType("application/json")
                .body("""
                        {
                          "sku": "ADMIN-1",
                          "name": "Admin Product",
                          "description": "Created by admin",
                          "price": 1000
                        }
                        """)
                .post("/api/v1/admin/products")
                .then()
                .statusCode(201)
                .body("sku", Matchers.equalTo("ADMIN-1"))
                .body("enabled", Matchers.equalTo(true))
                .body("createdAt", Matchers.notNullValue());
    }

    @Test
    void shouldCreateProductsBatch() {
        given()
                .contentType("application/json")
                .body("""
                        [
                          { "sku": "BATCH-1", "name": "One", "price": 10 },
                          { "sku": "BATCH-2", "name": "Two", "price": 20 }
                        ]
                        """)
                .post("/api/v1/admin/products/batch")
                .then()
                .statusCode(201)
                .body("size()", Matchers.equalTo(2));
    }

    @Test
    void shouldUpdateProduct() {
        given()
                .contentType("application/json")
                .body("""
                        { "sku": "UPD-1", "name": "Old", "price": 50.0 }
                        """)
                .post("/api/v1/admin/products");

        given()
                .contentType("application/json")
                .body("""
                        { "name": "New", "price": 99.0 }
                        """)
                .put("/api/v1/admin/products/UPD-1")
                .then()
                .statusCode(200)
                .body("name", Matchers.equalTo("New"))
                .body("price", Matchers.equalTo(99.0f));
    }

    @Test
    void shouldDeleteProductsBatch() {

        given()
                .contentType("application/json")
                .body("""
                [
                  {
                    "sku": "DEL-1",
                    "name": "P1",
                    "description": "D1",
                    "price": 10
                  },
                  {
                    "sku": "DEL-2",
                    "name": "P2",
                    "description": "D2",
                    "price": 20
                  }
                ]
                """)
                .post("/api/v1/admin/products/batch")
                .then()
                .statusCode(201);

        given()
                .contentType("application/json")
                .body("""
                ["DEL-1", "DEL-2"]
                """)
                .when()
                .delete("/api/v1/admin/products/batch")
                .then()
                .statusCode(204);
    }


    // ================= PUBLIC =================

    @Test
    void shouldReturnPublicProduct() {
        given()
                .contentType("application/json")
                .body("""
                        { "sku": "PUB-1", "name": "Public", "price": 100.0 }
                        """)
                .post("/api/v1/admin/products");

        given()
                .get("/api/v1/products/PUB-1")
                .then()
                .statusCode(200)
                .body("sku", Matchers.equalTo("PUB-1"));
    }

    @Test
    void shouldReturnAllEnabledProducts() {
        given()
                .contentType("application/json")
                .body("""
                        [
                          { "sku": "PUB-A", "name": "A", "price": 10.0 },
                          { "sku": "PUB-B", "name": "B", "price": 20.0 }
                        ]
                        """)
                .post("/api/v1/admin/products/batch");

        given()
                .get("/api/v1/products")
                .then()
                .statusCode(200)
                .body("content.sku", Matchers.hasItems("PUB-A", "PUB-B"));
    }


    @Test
    void shouldNotReturnDisabledProduct() {
        given()
                .contentType("application/json")
                .body("""
                        { "sku": "HIDDEN-1", "name": "Hidden", "price": 10 }
                        """)
                .post("/api/v1/admin/products");

        given()
                .patch("/api/v1/admin/products/HIDDEN-1/disable")
                .then()
                .statusCode(204);
    }

    @Test
    void shouldCreateProductsBatchAndReturnPaginatedResult() {

        given()
                .contentType("application/json")
                .body("""
                    [
                      { "sku": "P-001", "name": "Product 1", "price": 10 },
                      { "sku": "P-002", "name": "Product 2", "price": 20 },
                      { "sku": "P-003", "name": "Product 3", "price": 30 },
                      { "sku": "P-004", "name": "Product 4", "price": 40 },
                      { "sku": "P-005", "name": "Product 5", "price": 50 }
                    ]
                    """)
                .when()
                .post("/api/v1/admin/products/batch")
                .then()
                .statusCode(201)
                .body("size()", Matchers.equalTo(5))
                .body("sku", Matchers.hasItems(
                        "P-001", "P-002", "P-003", "P-004", "P-005"
                ));

        given()
                .queryParam("page", 0)
                .queryParam("size", 3)
                .when()
                .get("/api/v1/products")
                .then()
                .statusCode(200)
                .body("content.size()", Matchers.equalTo(3))
                .body("totalElements", Matchers.equalTo(5))
                .body("totalPages", Matchers.equalTo(2))
                .body("number", Matchers.equalTo(0))
                .body("content.sku", Matchers.hasItems(
                        "P-001", "P-002", "P-003"
                ));

        given()
                .queryParam("page", 1)
                .queryParam("size", 3)
                .when()
                .get("/api/v1/products")
                .then()
                .statusCode(200)
                .body("content.size()", Matchers.equalTo(2))
                .body("number", Matchers.equalTo(1))
                .body("content.sku", Matchers.hasItems(
                        "P-004", "P-005"
                ));
    }
}
