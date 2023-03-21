package com.ipc2.convenience_stores.models;

import com.ipc2.convenience_stores.models.stores.Store;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@ToString
@AllArgsConstructor
public class Catalog {

    @Getter private Store store;
    @Getter @Setter private List<Product> products = new ArrayList<Product>();

    public Catalog(Store store) {
        this.store = store;
    }

    public void addProduct(Product product){
        if (product != null) {
            products.add(product);
        }
    }

}
