package net.mischung.breadandshampoo.rest;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;

@Schema(description = "A new or updated list item.")
public class ItemWrite {

    @NotBlank
    @Length(min = 1,max = 255)
    private String item;

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    @Override
    public String toString() {
        return "InsertItem{" +
                "item='" + item + '\'' +
                '}';
    }

}
