package com.intellias.basicsandbox.controller;

import com.intellias.basicsandbox.persistence.entity.ItemEntity;
import com.intellias.basicsandbox.service.ItemService;
import com.intellias.basicsandbox.service.exception.ItemNotFoundException;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class ItemControllerTests {

    @InjectMock
    ItemService itemService;

    @Test
    void whenGetItemNotExistingThenShouldReturn404() {
        var itemId = UUID.randomUUID();
        when(itemService.getById(itemId)).thenThrow(ItemNotFoundException.class);

        given()
                .when().get(ItemController.API_VERSION + ItemController.PATH + "/" + itemId)
                .then().statusCode(404);
    }

    @Test
    void whenGetItemByIdFoundThenReturn200() {
        var itemId = UUID.fromString("55fd4dd7-3da4-40c8-a940-10c9c3c75e04");
        var item = new ItemEntity(itemId, "Item name", null, null);
        when(itemService.getById(itemId)).thenReturn(item);

        given()
                .when().get(ItemController.API_VERSION + ItemController.PATH + "/" + itemId)
                .then().statusCode(200)
                .body("id", equalTo(itemId.toString()))
                .body("name", equalTo("Item name"));
    }

    @Test
    void whenGetItemByIdLocalizedFoundThenReturnLocalizedCurrency() {
        var itemId = UUID.fromString("55fd4dd7-3da4-40c8-a940-10c9c3c75e04");
        var item = new ItemEntity(itemId, "Item name", "credit card", "UAH");
        when(itemService.getById(itemId)).thenReturn(item);

        given()
                .header("Accept-Language", "uk_UA")
                .when().get(ItemController.API_VERSION + ItemController.PATH + "/" + itemId)
                .then().statusCode(200)
                .body(containsString("Гривня ₴"));
    }

    @Test
    void whenDeleteItemThenReturn204() {
        var itemId = UUID.fromString("55fd4dd7-3da4-40c8-a940-10c9c3c75e04");

        doNothing().when(itemService).delete(itemId);
        given()
                .when().delete(ItemController.API_VERSION + ItemController.PATH + "/" + itemId)
                .then().statusCode(204);
    }
}
