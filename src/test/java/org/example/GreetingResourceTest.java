package org.example;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

@QuarkusTest
class GreetingResourceTest {

    @Test
    void testBerandaEndpoint() {
        given()
          .when().get("/beranda.html")
          .then()
             .statusCode(200)
             // Check for a unique string in your HTML to verify it loaded
             .body(containsString("<title>VizLyst - Dashboard</title>"))
             .body(containsString("Selamat Datang!"));
    }

    @Test
    void testHelloEndpoint() {
        given()
          .when().get("/hello")
          .then()
             .statusCode(200)
             .body(containsString("Hello")); 
    }
}