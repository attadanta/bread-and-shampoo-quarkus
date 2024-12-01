package net.mischung.breadandshampoo.rest;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Produces;
import net.mischung.breadandshampoo.service.InMemoryManagedListItemRepository;
import net.mischung.breadandshampoo.service.ManagedListItemRepository;

@Dependent
public class RestConfiguration {

    @Produces
    public ManagedListItemRepository listItemRepository() {
        return new InMemoryManagedListItemRepository();
    }

}
