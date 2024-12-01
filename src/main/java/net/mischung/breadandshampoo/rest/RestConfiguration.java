package net.mischung.breadandshampoo.rest;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;
import net.mischung.breadandshampoo.service.InMemoryManagedListItemRepository;

@Dependent
public class RestConfiguration {

    @Produces
    @Default
    @Singleton
    public InMemoryManagedListItemRepository listItemRepository() {
        return new InMemoryManagedListItemRepository();
    }

}
