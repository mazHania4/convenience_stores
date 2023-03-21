package com.ipc2.convenience_stores.models;

import lombok.*;

@ToString
@AllArgsConstructor

public class Product {

    @Getter private  int id = 0;
    @Getter @Setter private  String name;
    @Getter @Setter private int stock;
    @Getter @Setter private double cost;
    @Getter @Setter private double price;
    @Getter @Setter private Object reason;

    public Product(String name, int stock, double cost, double price) {
        this.name = name;
        this.stock = stock;
        this.cost = cost;
        this.price = price;
    }

    public Product(int id, String name, int stock, double cost, double price) {
        this.id = id;
        this.name = name;
        this.stock = stock;
        this.cost = cost;
        this.price = price;
        reason = null;
    }
}
