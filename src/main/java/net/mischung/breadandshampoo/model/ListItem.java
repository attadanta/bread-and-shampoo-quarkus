package net.mischung.breadandshampoo.model;

import java.util.Objects;

/**
 * A shopping list entry.
 */
public class ListItem {

    private int id;
    private String item;

    public ListItem() {
    }

    public ListItem(int id, String itemName) {
        this.id = id;
        this.item = Objects.requireNonNull(itemName);
    }

    public int getId() {
        return id;
    }

    public String getItem() {
        return item;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public ListItem withItem(String item) {
        return new ListItem(this.id, item);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListItem listItem = (ListItem) o;
        return id == listItem.id && item.equals(listItem.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, item);
    }

    @Override
    public String toString() {
        return "ListItem{" +
                "id=" + id +
                ", item='" + item + '\'' +
                '}';
    }

}
