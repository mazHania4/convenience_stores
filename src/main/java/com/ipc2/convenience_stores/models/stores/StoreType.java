package com.ipc2.convenience_stores.models.stores;

public enum StoreType {

    SUPERVISED("Supervised"),
    NOT_SUPERVISED("Not supervised");

    private String name;

    StoreType(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

}

