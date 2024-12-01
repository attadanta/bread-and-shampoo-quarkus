package net.mischung.breadandshampoo;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import net.mischung.breadandshampoo.model.ListItem;
import net.mischung.breadandshampoo.rest.ListItems;
import net.mischung.breadandshampoo.service.InMemoryManagedListItemRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class ApplicationTest {
    @Inject
    private InMemoryManagedListItemRepository repository;

    @AfterEach
    void clearRepository() {
        this.repository.reset();
    }

    @Test
    void smokeTest() {
    }

    @Test
    void exchangeEmptyList() {
        given()
        .when()
            .get("/a/list")
        .then()
            .statusCode(200)
            .and()
            .body("items.size()", is(0));
    }

    @Test
    void insertItem() {
        ListItem expectedItem = new ListItem(1, "bread");

        ListItem listItem = given()
            .contentType(ContentType.JSON).body("{ \"item\": \"bread\" }")
            .when()
                .post("/a/list")
            .then()
                .statusCode(200)
                .extract()
                .as(ListItem.class);

        assertEquals(expectedItem, listItem);

        ListItems listItems =
            given()
                .get("/a/list")
            .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract()
                .as(ListItems.class);

        assertEquals(1, listItems.getSize());
        assertEquals(Collections.singletonList(expectedItem), listItems.getItems());
    }

    @Test
    void insertItemMangledInput() {
        given()
            .contentType(ContentType.JSON)
            .body("{ \"tutti\": \"frutti\" }")
        .when()
            .post("/a/list")
        .then()
            .statusCode(400);
    }

    @Test
    void insertLargeItem() {
        String requestBody = String.format("{ \"item\": \"%s\"", longString());

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
        .when()
            .post("/a/list")
        .then()
            .statusCode(400);
    }

    @Test
    void updateItem() {
        ListItem listItem =
            given()
                .contentType(ContentType.JSON)
                .body("{ \"item\": \"bread\" }")
            .when()
                .post("/a/list")
            .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract()
                .as(ListItem.class);

        given()
            .contentType(ContentType.JSON)
            .body("{ \"item\": \"shampoo\" }")
        .when()
            .put("/a/list/items/" + listItem.getId())
        .then()
            .contentType(ContentType.JSON)
            .statusCode(200)
            .body("id", is(listItem.getId()))
            .body("item", is("shampoo"));

        given()
        .when()
            .get("/a/list")
        .then()
            .statusCode(200)
            .and()
            .body("items[0].item", is("shampoo"));
    }

    @Test
    void updateAnItemWithLongEntry() {
        ListItem listItem = given()
            .contentType(ContentType.JSON)
            .body("{ \"item\": \"bread\" }")
        .when().
            post("/a/list")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .extract()
            .as(ListItem.class);

        String content = String.format("{ \"item\": \"%s\" }", longString());

        given()
            .contentType(ContentType.JSON)
            .body(content)
        .when()
                .put("/a/list/items/" + listItem.getId())
        .then()
            .statusCode(400);
    }

    String longString() {
        RandomStringUtils insecure = RandomStringUtils.insecure();
        return insecure.next(666, true, false);
    }

}
