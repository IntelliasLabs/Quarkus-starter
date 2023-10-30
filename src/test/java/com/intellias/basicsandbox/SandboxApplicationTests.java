package com.intellias.basicsandbox;

import com.intellias.basicsandbox.controller.ItemController;
import com.intellias.basicsandbox.controller.dto.ItemDTO;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.*;

@QuarkusTest
class SandboxApplicationTests {

    @TestHTTPResource(ItemController.API_VERSION + ItemController.PATH)
    String itemsUrl;

    @Test
    void whenGetRequestWithIdThenItemReturned() {
        ItemDTO itemToCreate = new ItemDTO(null, "Item name", null, "UAH");

        ItemDTO expectedItem = given()
                .body(itemToCreate)
                .post(itemsUrl)
                .then()
                .statusCode(201)
                .extract().as(ItemDTO.class);

        given()
                .get(itemsUrl + "/" + expectedItem.getId())
                .then()
                .statusCode(200)
                .body("id", equalTo(expectedItem.getId()));
    }

    @Test
    void whenPostRequestThenItemCreated() {
        ItemDTO expectedItem = new ItemDTO(null, "Item name", null, "UAH");

        given()
                .body(expectedItem)
                .when()
                .post(ItemController.API_VERSION + ItemController.PATH)
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("name", equalTo(expectedItem.getName()));
    }

    @Test
    void whenPutRequestThenItemUpdated() {
        ItemDTO itemToCreate = new ItemDTO(null, "Item name", null, "UAH");

        ItemDTO createdItem = given()
                .body(itemToCreate)
                .when()
                .post(ItemController.API_VERSION + ItemController.PATH)
                .then()
                .statusCode(201)
                .extract().as(ItemDTO.class);

        createdItem.setName("Updated name");

        given()
                .body(createdItem)
                .when()
                .put(ItemController.API_VERSION + ItemController.PATH)
                .then()
                .statusCode(200)
                .body("name", equalTo(createdItem.getName()));
    }

    @Test
    void whenDeleteRequestThenItemDeleted() {
        ItemDTO itemToCreate = new ItemDTO(null, "Item name", null, "UAH");

        ItemDTO createdItem = given()
                .body(itemToCreate)
                .when()
                .post(ItemController.API_VERSION + ItemController.PATH)
                .then()
                .statusCode(201)
                .extract().as(ItemDTO.class);

        given()
                .when()
                .delete(ItemController.API_VERSION + ItemController.PATH + "/" + createdItem.getId())
                .then()
                .statusCode(204);

        given()
                .when()
                .get(ItemController.API_VERSION + ItemController.PATH + "/" + createdItem.getId())
                .then()
                .statusCode(404)
                .body("message", equalTo("An item with ID " + createdItem.getId() + " not found."));
    }

}
