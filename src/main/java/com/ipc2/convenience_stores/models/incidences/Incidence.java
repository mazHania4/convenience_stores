package com.ipc2.convenience_stores.models.incidences;

import com.ipc2.convenience_stores.models.Product;
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
public class Incidence {

    @Getter private int id;
    @Getter private Store store;
    @Getter private User storeUser;
    @Getter private Shipment shipment;
    @Getter @Setter private IncidenceState state;
    @Getter private LocalDateTime dateTime;
    @Getter @Setter private String solution;
    @Getter private List<Product> products = new ArrayList<>();



}
