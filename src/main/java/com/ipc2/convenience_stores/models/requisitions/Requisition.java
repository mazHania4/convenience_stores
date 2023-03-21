package com.ipc2.convenience_stores.models.requisitions;

import com.ipc2.convenience_stores.models.Product;
import com.ipc2.convenience_stores.models.stores.Store;
import com.ipc2.convenience_stores.models.users.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ToString
@AllArgsConstructor
public class Requisition {

    @Getter private int id;
    @Getter private Store store;
    @Getter private User storeUser;
    @Getter @Setter private RequisitionState state;
    private double cost;
    @Getter private LocalDateTime dateTime;
    @Getter private List<Product> products = new ArrayList<>();

    public Requisition(Store store, User storeUser, RequisitionState state, LocalDateTime dateTime, List<Product> products) {
        this.store = store;
        this.storeUser = storeUser;
        this.state = state;
        this.products = products;
        this.dateTime = dateTime;
        id = 0;
        getCost();
    }

    public void addProduct(Product product){
        if (product != null) {
            products.add(product);
        }
    }

    public double getCost() {
        cost = 0;
        for (Product product : products) {
            cost += (product.getCost()* product.getStock());
        }
        return cost;
    }
}
