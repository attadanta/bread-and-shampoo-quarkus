package net.mischung.breadandshampoo.rest;

import net.mischung.breadandshampoo.model.ListItem;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;
import java.util.Objects;

@Schema(name = "ShoppingList", description = "The user's shopping list")
public class ListItems {

    private List<ListItem> items;
    private int size;

    public ListItems(List<ListItem> items) {
        this.items = Objects.requireNonNull(items);
        this.size = items.size();
    }

    public List<ListItem> getItems() {
        return items;
    }

    public void setItems(List<ListItem> items) {
        this.items = items;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

}
