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
        System.setProperty("spring.data.mongodb.uri", mongoDBContainer.getReplicaSetUrl());
    }

    @BeforeEach
    void cleanDatabase() {
        try (MongoClient mongoClient = MongoClients.create(mongoDBContainer.getReplicaSetUrl())) {
            mongoClient.getDatabase("test").drop();
        }

        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @AfterAll
    static void stopContainer() {
        mongoDBContainer.stop();
    }

    @Test
    void shouldCreateProduct() {
        String requestBody = """
                { "sku": "iPhone-14-Pro",
                  "name": "iPhone 14 Pro",
                  "description": "Apple smartphone with OLED display",
                  "price": 1350.0
                }
                """;

        given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/v1/products")
                .then()
                .statusCode(201)
                .body("sku", Matchers.equalTo("iPhone-14-Pro"))
                .body("name", Matchers.equalTo("iPhone 14 Pro"))
                .body("description", Matchers.equalTo("Apple smartphone with OLED display"))
                .body("price", Matchers.equalTo(1350.0f));
    }

    @Test
    void shouldCreateProductsBatchAndReturnThem() {
        String requestBody = """
                [
                  { "sku": "Samsung-A90","name": "Samsung A90", "description": "Smartphone", "price": 1000 },
                  { "sku": "iPhone-14-Pro","name": "iPhone 14 Pro", "description": "Apple smartphone with OLED display", "price": 1350 }
                ]
                """;

        given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/v1/products/batch")
                .then()
                .statusCode(201)
                .body("size()", Matchers.equalTo(2))
                .body("[0].sku", Matchers.equalTo("Samsung-A90"))
                .body("[1].sku", Matchers.equalTo("iPhone-14-Pro"));
    }

    @Test
    void shouldReturnAllProductsAfterBatchInsert() {
        String requestBody = """
                [
                  { "sku": "Dell-XPS-13", "name": "Dell XPS 13", "description": "Compact ultrabook", "price": 1800.0 },
                  { "sku": "Sony-WH-1000XM5", "name": "Sony WH-1000XM5", "description": "Wireless headphones", "price": 500.0 }
                ]
                """;

        given()
                .contentType("application/json")
                .body(requestBody)
                .post("/api/v1/products/batch")
                .then()
                .statusCode(201);

        given()
                .when()
                .get("/api/v1/products")
                .then()
                .statusCode(200)
                .body("size()", Matchers.greaterThanOrEqualTo(2))
                .body("sku", Matchers.hasItems("Dell-XPS-13", "Sony-WH-1000XM5"));
    }


    @Test
    void shouldGetProductBySku() {
        String body = """
                { "sku": "MACBOOK-AIR","name": "MacBook Air", "description": "Apple ultrabook", "price": 1200.0 }
                """;

        String sku = given()
                .contentType("application/json")
                .body(body)
                .post("/api/v1/products")
                .then()
                .statusCode(201)
                .extract()
                .path("sku");

        given()
                .when()
                .get("/api/v1/products/{sku}", sku)
                .then()
                .statusCode(200)
                .body("sku", Matchers.equalTo("MACBOOK-AIR"))
                .body("name", Matchers.equalTo("MacBook Air"))
                .body("description", Matchers.equalTo("Apple ultrabook"))
                .body("price", Matchers.equalTo(1200.0f));
    }

    @Test
    void shouldDeleteProductBySku() {
        String body = """
                { "sku": "TEST-DELETE","name": "Test Delete", "description": "To be deleted", "price": 50.0 }
                """;

        String sku = given()
                .contentType("application/json")
                .body(body)
                .post("/api/v1/products")
                .then()
                .statusCode(201)
                .extract()
                .path("sku");

        given()
                .when()
                .delete("/api/v1/products/{sku}", sku)
                .then()
                .statusCode(200)
                .body(Matchers.equalTo("Product successfully deleted!"));

        given()
                .when()
                .get("/api/v1/products/{sku}", sku)
                .then()
                .statusCode(404);
    }

    @Test
    void shouldUpdateProduct() {
        String body = """
                { "sku": "OLD-PRODUCT","name": "Old Name", "description": "Old Desc", "price": 100.0 }
                """;

        String sku = given()
                .contentType("application/json")
                .body(body)
                .post("/api/v1/products")
                .then()
                .statusCode(201)
                .extract()
                .path("sku");

        String updateBody = """
                { "sku": "OLD-PRODUCT","name": "New Name", "description": "New Desc", "price": 150.0 }
                """;

        given()
                .contentType("application/json")
                .body(updateBody)
                .when()
                .put("/api/v1/products/{sku}", sku)
                .then()
                .statusCode(200)
                .body("sku", Matchers.equalTo("OLD-PRODUCT"))
                .body("name", Matchers.equalTo("New Name"))
                .body("description", Matchers.equalTo("New Desc"))
                .body("price", Matchers.equalTo(150.0f));
    }

    @Test
    void shouldReturn404ForNonExistingProduct() {
        given()
                .when()
                .get("/api/v1/products/{sku}", "non-existing-sku")
                .then()
                .statusCode(404);

        given()
                .when()
                .delete("/api/v1/products/{sku}", "non-existing-sku")
                .then()
                .statusCode(404);
    }
}
