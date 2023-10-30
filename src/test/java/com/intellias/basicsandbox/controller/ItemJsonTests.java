package com.intellias.basicsandbox.controller;

import com.intellias.basicsandbox.controller.dto.ItemDTO;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.mapper.ObjectMapperType;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

@QuarkusTest
class ItemJsonTests {

    private static final UUID SAMPLE_UUID = UUID.fromString("55fd4dd7-3da4-40c8-a940-10c9c3c75e04");
    private static final String SAMPLE_NAME = "Item name";
    private static final String SAMPLE_CREDIT_CARD = "credit card";
    private static final String SAMPLE_CURRENCY_CODE = "UAH";

    @Test
    void testSerialize() {
        var itemDTO = new ItemDTO(SAMPLE_UUID, SAMPLE_NAME, SAMPLE_CREDIT_CARD, SAMPLE_CURRENCY_CODE);

        given()
                .contentType("application/json")
                .body(itemDTO, ObjectMapperType.JACKSON_2)
                .when()
                .post("/item")  // Assuming "/item" is the endpoint to save an item
                .then()
                .statusCode(200)
                .body("id", equalTo(SAMPLE_UUID.toString()))
                .body("name", equalTo(SAMPLE_NAME));
    }

    @Test
    void testDeserialize() {
        var content = """
                {
                    "id": "%s",
                    "name": "%s",
                    "creditCard": "%s",
                    "currencyCode": "%s"
                }
                """.formatted(SAMPLE_UUID, SAMPLE_NAME, SAMPLE_CREDIT_CARD, SAMPLE_CURRENCY_CODE);

        given()
                .contentType("application/json")
                .body(content)
                .when()
                .post("/item")  // Assuming "/item" is the endpoint to save an item
                .then()
                .statusCode(200)
                .body("id", equalTo(SAMPLE_UUID.toString()))
                .body("name", equalTo(SAMPLE_NAME));
    }
}
