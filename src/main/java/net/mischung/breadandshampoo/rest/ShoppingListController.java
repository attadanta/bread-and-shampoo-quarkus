package net.mischung.breadandshampoo.rest;

import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import net.mischung.breadandshampoo.model.ListItem;
import net.mischung.breadandshampoo.service.ManagedListItemRepository;
import net.mischung.breadandshampoo.service.UserShoppingList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/{userName}/list")
@Tag(name = "Shopping List", description = "RESTful interface of Bread and Shampoo, Inc.")
public class ShoppingListController {

    // This is neither an access log, nor the only true way to name this variable, but you should get the idea
    private static final Log accessLog = LogFactory.getLog(ShoppingListController.class);

    private final ManagedListItemRepository itemRepository;

    public ShoppingListController(ManagedListItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @GET
    @Operation(summary = "View shopping list items", description = "Buy this")
    public ListItems listItems(@PathParam("userName") String userName) {
        accessLog.info(String.format("Getting list items for `%s'", userName));
        UserShoppingList shoppingList = getShoppingList(userName);
        return new ListItems(shoppingList.getItems());
    }

    @POST
    @Operation(summary = "Insert an item")
    public ListItem insertItem(@PathParam("userName") String userName,
                               @Valid ItemWrite itemWrite) {
        accessLog.info(String.format("Inserting a list item for `%s'", userName));
        UserShoppingList shoppingList = getShoppingList(userName);
        return shoppingList.insertItem(itemWrite.getItem());
    }

    @Path("/items/{id}")
    @PUT()
    @Operation(summary = "Change an item")
    public ListItem updateItem(@PathParam("userName") String userName,
                               @PathParam("id") Integer id,
                               @Valid ItemWrite itemWrite) {
        accessLog.info(String.format("Updating list item %d for `%s'", id, userName));
        UserShoppingList shoppingList = getShoppingList(userName);
        return shoppingList.updateItem(id, itemWrite.getItem());
    }

    @Path("/items/{id}")
    @DELETE
    @Operation(summary = "Delete an item")
    public Response deleteItem(@PathParam("userName") String userName, @PathParam("id") Integer itemId) {
        accessLog.info(String.format("Deleting list item %d for `%s'", itemId, userName));
        UserShoppingList shoppingList = getShoppingList(userName);
        shoppingList.deleteItem(itemId);
        return Response.noContent().build();
    }

    UserShoppingList getShoppingList(String owner) {
        return new UserShoppingList(this.itemRepository, owner);
    }

}
