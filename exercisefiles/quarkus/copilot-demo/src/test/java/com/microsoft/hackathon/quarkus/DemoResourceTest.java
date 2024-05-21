package com.microsoft.hackathon.quarkus;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.ws.rs.client.*;
import jakarta.ws.rs.core.Response;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import jakarta.ws.rs.core.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

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
        // Arrange
        String inputUrl = "http://example.com:8080/test/path?param=value";
        UriInfo uriInfo = mock(UriInfo.class);
        MultivaluedMap<String, String> queryParams = new MultivaluedHashMap<>();
        queryParams.add("url", inputUrl);
        when(uriInfo.getQueryParameters()).thenReturn(queryParams);

        DemoResource demoResource = new DemoResource();
        demoResource.uriInfo = uriInfo;

        // Act
        String result = demoResource.parseUrl(); // Call without parameters

        // Assert
        JsonObject jsonObject = JsonParser.parseString(result).getAsJsonObject();
        assertEquals("http", jsonObject.get("protocol").getAsString());
        assertEquals("example.com", jsonObject.get("host").getAsString());
        assertEquals(8080, jsonObject.get("port").getAsInt());
        assertEquals("/test/path", jsonObject.get("path").getAsString());
        assertEquals("param=value", jsonObject.get("query").getAsString());
    }

}