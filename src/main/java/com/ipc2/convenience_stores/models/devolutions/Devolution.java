package com.ipc2.convenience_stores.models.devolutions;

import com.ipc2.convenience_stores.models.Product;
import com.ipc2.convenience_stores.models.incidences.IncidenceState;
import com.ipc2.convenience_stores.models.shipments.Shipment;
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
public class Devolution {

    @Getter
    private int id;
    @Getter private Store store;
    @Getter private User storeUser;
    @Getter private Shipment shipment;
    @Getter @Setter private DevolutionState state;
    @Getter private LocalDateTime dateTime;
    private double cost;
    @Getter private List<Product> products = new ArrayList<>();

    public double getCost() {
        cost = 0;
        for (Product product : products) {
            cost += (product.getCost()* product.getStock());
        }
        return cost;
    }

}
