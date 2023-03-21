package com.ipc2.convenience_stores.models.shipments;

import com.ipc2.convenience_stores.models.Product;
import com.ipc2.convenience_stores.models.requisitions.Requisition;
import com.ipc2.convenience_stores.models.stores.Store;
import com.ipc2.convenience_stores.models.users.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ToString
@AllArgsConstructor
public class Shipment {

    @Getter private int id;
    @Getter private Store store;
    @Getter private User warehouseUser;
    @Getter private Requisition requisition;
    @Getter @Setter private ShipmentState state;
    @Getter private LocalDateTime dispatchedDateTime;
    @Getter private LocalDateTime receivedDateTime;
    private double cost;
    @Getter private List<Product> products = new ArrayList<>();

    public Shipment(Store store, Requisition requisition, ShipmentState state, LocalDateTime dateTime, List<Product> products) {
        this.store = store;
        this.warehouseUser = store.getWarehouseUser();
        this.requisition = requisition;
        this.state = state;
        this.products = products;
        this.receivedDateTime = dateTime;
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
