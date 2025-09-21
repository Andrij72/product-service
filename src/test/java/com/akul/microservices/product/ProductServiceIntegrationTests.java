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
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.TestcontainersConfiguration;

import static io.restassured.RestAssured.given;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductServiceIntegrationTests {

    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0.5");

    @LocalServerPort
    private Integer port;

    static {
        mongoDBContainer.start();
    }

    @BeforeEach
    void cleanDatabase() {
        try (MongoClient mongoClient = MongoClients.create(mongoDBContainer.getReplicaSetUrl())) {
            long countBefore = mongoClient.getDatabase("test")
                    .getCollection("products")
                    .countDocuments();
            System.out.println("Documents in 'products' before test: " + countBefore);

            mongoClient.getDatabase("test").drop();

            long countAfterDrop = mongoClient.getDatabase("test")
                    .getCollection("products")
                    .countDocuments();
            System.out.println("Documents in 'products' after drop: " + countAfterDrop);
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
                  {
                       "name": "iPhone 1415 Pro",
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
                .body("id", Matchers.notNullValue())

                .body("name", Matchers.equalTo("iPhone 1415 Pro"))
                .body("description", Matchers.equalTo("Apple smartphone with OLED display"))
                .body("price", Matchers.equalTo(1350.0f));
    }

    @Test
    void shouldCreateProductsBatchAndReturnThem() {
        String requestBody = """
                [
                  { "name": "Samsung A90", "description": "Smartphone", "price": 1000 },
                  { "name": "iPhone 14 Pro", "description": "Apple smartphone with OLED display", "price": 1350 }
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
                .body("[0].id", Matchers.notNullValue())
                .body("[0].name", Matchers.equalTo("Samsung A90"))
                .body("[1].name", Matchers.equalTo("iPhone 14 Pro"));
    }

    @Test
    void shouldReturnAllProductsAfterBatchInsert() {

        String requestBody = """
                [
                  { "name": "Dell XPS 13", "description": "Compact ultrabook", "price": 1800.0 },
                  { "name": "Sony WH-1000XM5", "description": "Wireless headphones", "price": 500.0 }
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
                .body("name", Matchers.hasItems("Dell XPS 13", "Sony WH-1000XM5"));
    }

    @Test
    void shouldFindProductByName() {

        String requestBody = """
                [
                  { "name": "Asus ROG Strix G15", "description": "Gaming laptop", "price": 2200.0 }
                ]
                """;

        given()
                .contentType("application/json")
                .body(requestBody)
                .post("/api/v1/products/batch")
                .then()
                .statusCode(201);


        given()
                .queryParam("name", "Asus ROG Strix G15")
                .when()
                .get("/api/v1/products/search")
                .then()
                .statusCode(200)
                .body("name", Matchers.equalTo("Asus ROG Strix G15"))
                .body("description", Matchers.equalTo("Gaming laptop"))
                .body("price", Matchers.equalTo(2200.0f));
    }

}
