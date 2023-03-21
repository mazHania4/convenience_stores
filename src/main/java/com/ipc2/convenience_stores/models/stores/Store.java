package com.ipc2.convenience_stores.models.stores;

import com.ipc2.convenience_stores.models.users.User;
import lombok.*;

@ToString
@AllArgsConstructor
public class Store {

    @Getter private int id;
    @Getter @Setter private User warehouseUser;
    @Getter @Setter private String address;
    @Getter @Setter private StoreType type;

    public Store(User warehouseUser, String address, StoreType type) {
        this.warehouseUser = warehouseUser;
        this.address = address;
        this.type = type;
    }
}
