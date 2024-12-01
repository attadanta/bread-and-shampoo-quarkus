package net.mischung.breadandshampoo;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import net.mischung.breadandshampoo.model.ListItem;
import net.mischung.breadandshampoo.rest.ListItems;
import net.mischung.breadandshampoo.service.ManagedListItemRepository;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class ApplicationTest {
    @Inject
    private ManagedListItemRepository repository;

    @Test
    void smokeTest() {
    }

    @Test
    void exchangeEmptyList() throws Exception {
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

        ListItem listItem = given().contentType(ContentType.JSON).body("{ \"item\": \"bread\" }").when()
                .post("/a/list")
                .then()
                .statusCode(200)
                .extract()
                .as(ListItem.class);

        assertEquals(expectedItem, listItem);

        ListItems listItems = given().get("/a/list")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract().as(ListItems.class);

        assertEquals(1, listItems.getSize());
        assertEquals(Collections.singletonList(expectedItem), listItems.getItems());
    }

}
