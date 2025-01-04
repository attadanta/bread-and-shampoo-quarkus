package net.mischung.breadandshampoo.service;

import net.mischung.breadandshampoo.model.ListItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryManagedListItemRepositoryTest {

    @Test
    public void listEmptyRepositoryItems() {
        InMemoryManagedListItemRepository repo = new InMemoryManagedListItemRepository();
        Assertions.assertTrue(repo.listUserItems("admin").isEmpty());
    }

    @Test
    public void insertItem() {
        InMemoryManagedListItemRepository repo = new InMemoryManagedListItemRepository(new AtomicInteger(), Collections.emptyMap());
        Assertions.assertEquals(new ManagedListItem(new ListItem(1, "bread"), "a", false), repo.insertItem("a", "bread"));
        Assertions.assertEquals(new ManagedListItem(new ListItem(2, "shampoo"), "a", false), repo.insertItem("a", "shampoo"));
        Assertions.assertEquals(2, repo.listUserItems("a").size());
    }

    @Test
    public void listFiltersByOwnerAndDeletedFlag() {
        Map<Integer, ManagedListItem> store = new HashMap<>();
        store.put(1, new ManagedListItem(new ListItem(1, "bread"), "a", false));
        store.put(2, new ManagedListItem(new ListItem(2, "chips"), "b", false));
        store.put(3, new ManagedListItem(new ListItem(3, "coke"), "a", true));
        store.put(4, new ManagedListItem(new ListItem(4, "shampoo"), "a", false));

        InMemoryManagedListItemRepository repo = new InMemoryManagedListItemRepository(new AtomicInteger(), store);
        List<ManagedListItem> managedListItems = repo.listUserItems("a");

        Assertions.assertEquals(2, managedListItems.size());
        Assertions.assertEquals(new ManagedListItem(new ListItem(1, "bread"), "a", false), managedListItems.get(0));
        Assertions.assertEquals(new ManagedListItem(new ListItem(4, "shampoo"), "a", false), managedListItems.get(1));
    }

    @Test
    public void updateExistingItemOwnedByUser() {
        Map<Integer, ManagedListItem> store = new HashMap<>();
        store.put(1, new ManagedListItem(new ListItem(1, "bread"), "b", false));
        store.put(2, new ManagedListItem(new ListItem(2, "butter"), "a", false));

        InMemoryManagedListItemRepository repo = new InMemoryManagedListItemRepository(new AtomicInteger(), store);
        ManagedListItem result = repo.updateItem("a", 2, "shampoo");

        ManagedListItem expectedItem = new ManagedListItem(new ListItem(2, "shampoo"), "a", false);
        Assertions.assertEquals(result, expectedItem);

        List<ManagedListItem> managedListItems = repo.listUserItems("a");

        Assertions.assertEquals(1, managedListItems.size());
        Assertions.assertEquals(managedListItems.getFirst(), expectedItem);
    }

    @Test
    public void updateNonExistingItem() {
        Map<Integer, ManagedListItem> store = new HashMap<>();
        store.put(1, new ManagedListItem(new ListItem(1, "bread"), "a", false));

        InMemoryManagedListItemRepository repo = new InMemoryManagedListItemRepository(new AtomicInteger(), store);
        Assertions.assertThrows(ItemDoesNotExistException.class, () -> repo.updateItem("a", 2, "shampoo"));
    }

    @Test
    public void updateItemOfAnotherUser() {
        Map<Integer, ManagedListItem> store = new HashMap<>();
        store.put(1, new ManagedListItem(new ListItem(1, "bread"), "a", false));

        InMemoryManagedListItemRepository repo = new InMemoryManagedListItemRepository(new AtomicInteger(), store);
        Assertions.assertThrows(WrongItemOwnerException.class, () -> repo.updateItem("b", 1, "shampoo"));
    }

    @Test
    public void deleteExistingItemOwnedByUser() {
        Map<Integer, ManagedListItem> store = new HashMap<>();
        store.put(1, new ManagedListItem(new ListItem(1, "bread"), "a", false));
        store.put(2, new ManagedListItem(new ListItem(2, "shampoo"), "a", false));

        InMemoryManagedListItemRepository repo = new InMemoryManagedListItemRepository(new AtomicInteger(), store);
        ManagedListItem result = repo.deleteItem("a", 2);

        ManagedListItem deletedItem = new ManagedListItem(new ListItem(2, "shampoo"), "a", true);
        Assertions.assertEquals(result, deletedItem);

        List<ManagedListItem> managedListItems = repo.listUserItems("a");

        Assertions.assertEquals(1, managedListItems.size());
        Assertions.assertEquals(new ManagedListItem(new ListItem(1, "bread"), "a", false), managedListItems.getFirst());
    }

    @Test
    public void deleteNonExistingItem() {
        InMemoryManagedListItemRepository repo = new InMemoryManagedListItemRepository(new AtomicInteger(), Collections.emptyMap());
        Assertions.assertThrows(ItemDoesNotExistException.class, () -> repo.deleteItem("a", 1));
    }

    @Test
    public void deleteItemOfAnotherUser() {
        Map<Integer, ManagedListItem> store = new HashMap<>();
        store.put(1, new ManagedListItem(new ListItem(1, "bread"), "a", false));

        InMemoryManagedListItemRepository repo = new InMemoryManagedListItemRepository(new AtomicInteger(), store);
        Assertions.assertThrows(WrongItemOwnerException.class, () -> repo.deleteItem("b", 1));
    }

}
