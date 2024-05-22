package com.microsoft.hackathon.quarkus;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
public class DemoResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/hello?key=world")
          .then()
             .statusCode(200)
             .body(is("hello world"));
    }

    // new test if key is not passed
    @Test
    public void testHelloEndpointKeyNotPassed() {
        given()
          .when().get("/hello")
          .then()
             .statusCode(200)
             .body(is("key not passed"));
    }

    @Test
    public void testDiffDatesEndpointWithTwoDates() {
            given()
                .when().get("/diffdates?date1=01-01-2021&date2=01-02-2021")
                .then()
                     .statusCode(200)
                     .body(is("Difference in days: 31"));
    }

    @Test
    public void testDiffDatesEndpointWithOneDate() {
            given()
                .when().get("/diffdates?date1=01-01-2021")
                .then()
                     .statusCode(200)
                     .body(is("Both dates must be provided"));
    }

    @Test
    public void testDiffDatesEndpointWithNoDates() {
            given()
                .when().get("/diffdates")
                .then()
                     .statusCode(200)
                     .body(is("Both dates must be provided"));
    }

    @Test
    public void testValidatePhoneNumberEndpointWithValidNumber() {
        given()
          .when().get("/validatephonenumber?phone=+34600112233")
          .then()
             .statusCode(200)
             .body(is("true"));
    }

    @Test
    public void testValidatePhoneNumberEndpointWithInvalidNumber() {
        given()
          .when().get("/validatephonenumber?phone=+34112233")
          .then()
             .statusCode(200)
             .body(is("false"));
    }

    @Test
    public void testValidatePhoneNumberEndpointWithoutNumber() {
        given()
          .when().get("/validatephonenumber")
          .then()
             .statusCode(200)
             .body(is("false"));
    }

    @Test
    public void testValidateDNIEndpointWithValidDNI() {
            given()
                .when().get("/validatedni?dni=12345678A")
                .then()
                     .statusCode(200)
                     .body(is("true"));
    }

    @Test
    public void testValidateDNIEndpointWithInvalidDNI() {
            given()
                .when().get("/validatedni?dni=1234567A")
                .then()
                     .statusCode(200)
                     .body(is("false"));
    }

    @Test
    public void testValidateDNIEndpointWithoutDNI() {
            given()
                .when().get("/validatedni")
                .then()
                     .statusCode(200)
                     .body(is("false"));
    }

    // create a test case for this statement: given the name of the color as path parameter, return the hexadecimal code. If the color is not found, return 404
    @Test
    public void testGetHexColorEndpoint() {
        given()
          .when().get("/getHexColor?color=red")
          .then()
             .statusCode(200)
             .body(is("#FF0000"));
    }

    @Test
    public void testGetHexColorEndpointWithInvalidColor() {
        given()
          .when().get("/getHexColor?color=invalidColor")
          .then()
             .statusCode(200)
             .body(is("Color not found"));
    }

    // test getJoke
    @Test
    public void testGetJokeEndpoint() {
        given()
          .when().get("/joke")
          .then()
             .statusCode(200);
    }

    @Test
    public void testParseUrl() {
        DemoResource demoResource = new DemoResource();

        // Test with a valid URL
        String validUrl = "https://example.com:8080/test?param=value";
        String validResult = demoResource.parseUrl(validUrl);
        assertTrue(validResult.contains("\"protocol\":\"https\""));
        assertTrue(validResult.contains("\"host\":\"example.com\""));
        assertTrue(validResult.contains("\"port\":8080"));
        assertTrue(validResult.contains("\"path\":\"/test\""));
        assertTrue(validResult.contains("\"query\":\"param=value\""));

        // Test with an invalid URL
        String invalidUrl = "not a valid url";
        String invalidResult = demoResource.parseUrl(invalidUrl);
        assertEquals("{\"error\": \"Invalid URL\"}", invalidResult);
    }

    @Test
    public void testListFilesAndFolders() {
        DemoResource demoResource = new DemoResource();

        // Test with a valid path
        String validPath = System.getProperty("user.dir"); // This will get the current working directory
        String validResult = demoResource.listFilesAndFolders(validPath);
        assertTrue(validResult.contains(validPath)); // The current directory itself should be in the result

        // Test with an invalid path
        String invalidPath = "not a valid path";
        String invalidResult = demoResource.listFilesAndFolders(invalidPath);
        assertEquals("{\"error\": \"Path does not exist\"}", invalidResult);
    }

    @Test
    public void testCountWord() {
        DemoResource demoResource = new DemoResource();

        // Test with a valid file path and a word
        String validPath = System.getProperty("user.dir") + "/src/main/resources/colors.json"; // This will get the path to the test file

        System.out.println(validPath);

        String word = "255";
        String validResult = demoResource.countWord(validPath, word);
        assertTrue(validResult.contains("\"count\": 8")); // The word "test" appears twice in the test file

        // Test with an invalid file path
        String invalidPath = "not a valid path";
        String invalidResult = demoResource.countWord(invalidPath, word);
        assertEquals("{\"error\": \"File does not exist\"}", invalidResult);
    }

    @Test
    public void testZipFolder() {
        DemoResource demoResource = new DemoResource();

        // Test with a valid folder path
        String validPath = System.getProperty("user.dir"); // This will get the current working directory
        Response validResponse = demoResource.zipFolder(validPath);
        assertEquals(200, validResponse.getStatus()); // The status should be 200 (OK)

        // Test with an invalid folder path
        String invalidPath = "not a valid path";
        Response invalidResponse = demoResource.zipFolder(invalidPath);
        assertEquals(400, invalidResponse.getStatus()); // The status should be 400 (Bad Request)
    }

}