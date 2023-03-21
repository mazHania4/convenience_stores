package com.ipc2.convenience_stores.models.shipments;

public enum ShipmentState {
    DISPATCHED("Dispatched"),
    RECEIVED("Received");
    private String nameDB;

    ShipmentState(String nameDB){
        this.nameDB = nameDB;
    }

    public String getNameDB(){
        return nameDB;
    }


}
